package com.tamsbeauty.Service;



import com.tamsbeauty.Dto.Request.Bookingrequest;
import com.tamsbeauty.Dto.Response.Bookingresponse;
import com.tamsbeauty.Dto.Response.Fetchdbservicesresponse;
import com.tamsbeauty.Entity.Attendant;
import com.tamsbeauty.Entity.Booking;
import com.tamsbeauty.Entity.Services;
import com.tamsbeauty.Entity.User;
import com.tamsbeauty.Exceptions.NotFoundException;
import com.tamsbeauty.Repo.Attendantrepo;
import com.tamsbeauty.Repo.Bookingrepo;
import com.tamsbeauty.Repo.Servicesrepo;
import com.tamsbeauty.Repo.Userrepo;
import com.tamsbeauty.Security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@Transactional
public class Bookingservice {

 private  final Bookingrepo bookingrepo;
 private  final Userrepo userrepo;
 private  final Servicesrepo servicerepo;
 private  final Attendantrepo attendantrepo;

    public Bookingservice(Bookingrepo bookingrepo, Userrepo userrepo, Servicesrepo servicerepo, Attendantrepo attendantrepo) {
        this.bookingrepo = bookingrepo;
        this.userrepo = userrepo;
        this.servicerepo = servicerepo;
        this.attendantrepo = attendantrepo;
    }


    // create a booking and Mark as trasactional
 @Transactional
    public Bookingresponse book(UserPrincipal currentUser, Bookingrequest bookingrequest) {
        if (currentUser==null){
            return new Bookingresponse("You are not authenticated",false);
        }
        if (bookingrequest==null  || bookingrequest.getServicetime()==null || Objects.equals(bookingrequest.getServicetime(), "") ||  bookingrequest.getAttendant_id()==null|| bookingrequest.getServices_id()==null||bookingrequest.getAmountrequired()==null||bookingrequest.getAmount()==null|| Objects.equals(bookingrequest.getMobile(), "")){
            return new Bookingresponse("Request body may not be null or blank",false);
        }
        try {
            User user=userrepo.findById(currentUser.getId()).orElseThrow(() -> new UsernameNotFoundException("user with that id could not be found"));
            Services service=servicerepo.findById(bookingrequest.getServices_id()).orElseThrow(() -> new NotFoundException("Service not found"));
            boolean bookingexi=bookingrepo.existsByUserAndServices(user, service);
            if (bookingexi){
                return new Bookingresponse("You have already booked the service",false);

            }
            Attendant attendant=attendantrepo.findById(bookingrequest.getAttendant_id()).orElseThrow(() -> new NotFoundException("The Attendant was  not found"));
            Booking booking=new Booking();
            booking.setUser(user);
            booking.setAttendant(attendant);
            booking.setAmountRequired(bookingrequest.getAmountrequired());
            booking.setAmount(bookingrequest.getAmount());
            booking.setServicetime(bookingrequest.getServicetime());
            booking.setTimepaid(bookingrequest.getTimepaid());
            booking.setMobile(bookingrequest.getMobile());
            booking.setServices(service);
            Booking savedbooking=bookingrepo.save(booking);
            return new Bookingresponse("Booking with id "+savedbooking.getId()+" created! ",true);

        }catch (Exception e){
            return new Bookingresponse("An exception has occurred while making a booking "+e,false);
        }
    }
    public Fetchdbservicesresponse fetchdbbookings(UserPrincipal currentUser) {
        if (currentUser==null){
            return Fetchdbservicesresponse.builder().status(false).message("YOu are not authenticated").build();
        }
        try {
            List<Booking> bookings=bookingrepo.findAll();
            return Fetchdbservicesresponse.builder().status(true).servicesList(bookings).build();
        }catch (Exception e){
            return Fetchdbservicesresponse.builder().message("An error has occurred while getting services "+e.getMessage()).status(false).build();
        }
    }
}
