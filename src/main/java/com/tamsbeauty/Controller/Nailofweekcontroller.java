package com.tamsbeauty.Controller;


import com.tamsbeauty.Dto.Request.Nailofweekrequest;
import com.tamsbeauty.Security.CurrentUser;
import com.tamsbeauty.Security.UserPrincipal;
import com.tamsbeauty.Service.Nailoweekservice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/nailofweek")
public class Nailofweekcontroller {

    private final Nailoweekservice nailoweekservice;

    public Nailofweekcontroller(Nailoweekservice nailoweekservice) {
        this.nailoweekservice = nailoweekservice;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
//    public Object addnailofeek(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Nailofweekrequest nailofweekrequest, @RequestParam("file") MultipartFile multipartFile){
        public Object addnailofeek(@CurrentUser UserPrincipal currentUser,
                                   @RequestParam("file") MultipartFile multipartFile,
                                   @RequestParam("cost")BigDecimal cost,
                                   @RequestParam("description") String  description){

        return nailoweekservice.addnail(currentUser,multipartFile,cost,description);
    }

    @GetMapping("/fetch")
//    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_USER')or hasRole('ROLE_SUPER_ADMIN')")
    public  Object nailofweekresponse(){
        return nailoweekservice.nailofweekresponse();
    }
    @PostMapping("/delete/{nailid}")
       @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
        public  Object deletenailoftheweek(@CurrentUser UserPrincipal currentUser, @PathVariable("nailid") Long nailid){
            return nailoweekservice.deletenailoftheweek(currentUser,nailid);
        }
}
