package com.tamsbeauty.Controller;

import com.tamsbeauty.Dto.Page.PagedResponse;
import com.tamsbeauty.Dto.Response.Fetchdbservicesresponse;
import com.tamsbeauty.Dto.Request.Saveattendantrequest;
import com.tamsbeauty.Dto.Request.Servicesrequest;
import com.tamsbeauty.Dto.Response.Saveattendantresponse;
import com.tamsbeauty.Dto.Response.Serviceimageresponse;
import com.tamsbeauty.Dto.Response.Servicesimageesresponse;
import com.tamsbeauty.Dto.Response.Servicesresponse;
import com.tamsbeauty.Security.CurrentUser;
import com.tamsbeauty.Security.UserPrincipal;
import com.tamsbeauty.Service.Servicesservice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.tamsbeauty.config.ApiUtils.DEFAULT_PAGE_NUMBER;
import static com.tamsbeauty.config.ApiUtils.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/services")

public class ServicesController {



    private  Servicesservice servicesservice;

    public ServicesController(Servicesservice servicesservice) {
        this.servicesservice = servicesservice;
    }

    @PostMapping("/createservice")
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    public Servicesresponse createservice(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Servicesrequest servicesrequest){
        return servicesservice.createservice(currentUser,servicesrequest);
    }
   
    @PostMapping("/service-images-upload/{serviceid}")
    public ResponseEntity<?> uploadserviceimages(@PathVariable("serviceid") Long serviceid, @RequestParam("files") MultipartFile[] multipartFiles){

        return ResponseEntity.ok(servicesservice.uploadfiles(serviceid,multipartFiles));
    }
    @GetMapping("/services-get")
    public Object fetchavailableservices(){
        return ResponseEntity.ok(servicesservice.fetchavailableservices());
    }

    @GetMapping("/services-get-with-images")
    public PagedResponse<Servicesimageesresponse> getservicesandimages(@RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                            @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size){

        return servicesservice.getservicesandimages(page, size);
    }


    @PostMapping("/service-image-get/{serviceimageid}")
    public Serviceimageresponse serviceimageidget(@PathVariable("serviceimageid") Long serviceimageid){

        return servicesservice.serviceimageidget(serviceimageid);
    }


    @GetMapping("/get-attendants")
    public Object getattendants(){
        return ResponseEntity.ok(servicesservice.getattendants());
    }
    @PostMapping("/save-attendant")
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    public Saveattendantresponse saveattendant(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Saveattendantrequest  saveattendantrequest){
        return servicesservice.saveattendant(currentUser, saveattendantrequest);
    }
    @PostMapping("/service-update-isonoffer/{serviceid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    public Object  serviceupdateisonoffer(@CurrentUser UserPrincipal currentUser,@PathVariable("serviceid") Long serviceid ){
        return servicesservice.serviceupdateisonoffer(currentUser, serviceid);
    }

    @PostMapping("/delete-service/{serviceid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    public Object  deleteservice(@CurrentUser UserPrincipal currentUser,@PathVariable("serviceid") Long serviceid ){
        return servicesservice.deleteservice(currentUser, serviceid);
    }



}
