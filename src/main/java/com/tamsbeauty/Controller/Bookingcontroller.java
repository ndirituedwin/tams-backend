package com.tamsbeauty.Controller;



import com.tamsbeauty.Dto.Request.Bookingrequest;
import com.tamsbeauty.Dto.Response.Fetchdbservicesresponse;
import com.tamsbeauty.Security.CurrentUser;
import com.tamsbeauty.Security.UserPrincipal;
import com.tamsbeauty.Service.Bookingservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api/booking")
public class Bookingcontroller {


    private final Bookingservice bookingservice;

    public Bookingcontroller(Bookingservice bookingservice) {
        this.bookingservice = bookingservice;
    }
    @PostMapping("/book-service")
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> bookservice(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Bookingrequest bookingrequest, BindingResult result) {
        return new ResponseEntity<>(bookingservice.book(currentUser,bookingrequest),HttpStatus.CREATED);
    }
    @GetMapping("/fetch-bookings")
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    public Fetchdbservicesresponse fetchservice(@CurrentUser UserPrincipal currentUser){
        return bookingservice.fetchdbbookings(currentUser);
    }
    }
