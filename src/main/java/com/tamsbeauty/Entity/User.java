package com.tamsbeauty.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
//    @Size(min = 4,max = 10)
    private String username;

    @NotBlank
    @Size(min = 10,max = 10)
    @Column(unique = true)
    private String mobile;

    @NotBlank
//    @Size(min = 4,max = 50)
    private String password;

    private Instant createddate=Instant.now();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Booking> bookingList=new ArrayList<>(0);


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles=new HashSet<>(0);;

}
