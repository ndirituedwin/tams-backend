package com.tamsbeauty.Dto.Response.Auth;


import com.tamsbeauty.Entity.Booking;
import com.tamsbeauty.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentUserResponse {

    private Long id;
    private String  username;
    private String  name;
    private String mobilenumber;

    private String  message;

    private List<Booking> booking;
    private Collection<Role> roles;
  
}
