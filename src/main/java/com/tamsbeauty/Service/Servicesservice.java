package com.tamsbeauty.Service;


import com.tamsbeauty.Dto.Page.PagedResponse;
import com.tamsbeauty.Dto.Request.Saveattendantrequest;
import com.tamsbeauty.Dto.Request.Servicesrequest;
import com.tamsbeauty.Dto.Response.*;
import com.tamsbeauty.Entity.Attendant;
import com.tamsbeauty.Entity.ServiceImage;
import com.tamsbeauty.Entity.Services;
import com.tamsbeauty.Exceptions.NotFoundException;
import com.tamsbeauty.Repo.Attendantrepo;
import com.tamsbeauty.Repo.ServiceImagerepo;
import com.tamsbeauty.Repo.Servicesrepo;
import com.tamsbeauty.Security.CurrentUser;
import com.tamsbeauty.Security.UserPrincipal;
import com.tamsbeauty.config.Fileupload.FileUtil;
import com.tamsbeauty.mapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static com.tamsbeauty.config.ApiUtils.FOLDER_PATH;
import static com.tamsbeauty.config.ApiUtils.MAX_PAGE_SIZE;

@Transactional
@Slf4j
@Service

public class Servicesservice {


     private final Servicesrepo servicesrepo;
     private final ServiceImagerepo serviceImagerepo;
     private final Attendantrepo attendantrepo;

    public Servicesservice(Servicesrepo servicesrepo, ServiceImagerepo serviceImagerepo, Attendantrepo attendantrepo) {
        this.servicesrepo = servicesrepo;
        this.serviceImagerepo = serviceImagerepo;
        this.attendantrepo = attendantrepo;
    }


    public Servicesresponse createservice(UserPrincipal currentUser, Servicesrequest servicesrequest) {
          if(currentUser==null){
            return Servicesresponse.builder().message("You are not authenticated").status(false).build();

          }
        if (servicesrequest.getCost()==null || servicesrequest.getDescription()==null){
            return Servicesresponse.builder().message("invalid request body").status(false).build();
        }
        if (servicesrequest.getCost().toString()=="" || servicesrequest.getDescription()==""){
            return Servicesresponse.builder().message("invalid request body provided").status(false).build();
        }
        try {
            boolean serviceexists=servicesrepo.existsByDescription(servicesrequest.getDescription());
            if (serviceexists){
                return Servicesresponse.builder().message("service with the given name "+servicesrequest.getDescription()+" exists").status(false).build();
            }
            Services services=new Services();
            services.setDescription(servicesrequest.getDescription());
            services.setCost(servicesrequest.getCost());
            services.setIsonoffer(servicesrequest.isIsonoffer());
            Services services1=servicesrepo.save(services);
            return Servicesresponse.builder().status(true).message("service created! "+services1.getDescription()).id(services1.getId()).status(true).build();

        }catch (Exception e){
            return Servicesresponse.builder().message("An exception has occurred while creating service").status(false).build();
        }
    }

    public ResponseEntity<FileUploadresponse> uploadfiles(Long serviceid, MultipartFile[] multipartFiles) {
        try {
            System.out.println("Long serviceid "+serviceid);
            Path uploadPath= Paths.get(FOLDER_PATH);


            Services service1=servicesrepo.findById(serviceid).orElseThrow(()-> new NotFoundException("service not found"));
        //    Services servicess=servicesrepo.findById(serviceid).orElseThrow(() -> new NotFoundException("service not found"));

            try {
                createdirsifnotexist();

                List<String >  filenames=new ArrayList<>(0);
                Arrays.asList(multipartFiles).stream().forEach(multipartFile -> {
                    byte[] bytes=new byte[0];
                    try {
                        String filename=multipartFile.getOriginalFilename();
                        InputStream inputStream=multipartFile.getInputStream();
                        Path filePath=uploadPath.resolve(filename);
                        Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);

                        ServiceImage serviceImage=new ServiceImage();
                        serviceImage.setService(service1);
                        serviceImage.setImage(filename);
                        serviceImagerepo.save(serviceImage);
//                    filenames.add(multipartFile.getOriginalFilename());
                        filenames.add(filename);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                });
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileUploadresponse("files uploaded "+filenames, true));

            }catch (Exception e){
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FileUploadresponse("An exception has occurred while uploading files "+e.getMessage(),false));

            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileUploadresponse("An exception has occurred while creating service "+e.getMessage(),false));

        }
    }
    public Object fetchavailableservices(){
        try {
            List<Services>  services=servicesrepo.findAllByOrderByIdDesc();
            return services;
        }catch (Exception e){
            return "An exception has occurred while fetching services "+e.getMessage();
        }
    }

    void createdirsifnotexist() {
//        create directory to save files
        File directory =new File(FileUtil.folderPath);
        if (!directory.exists()){
            directory.mkdir();
        }
    }

    public PagedResponse<Servicesimageesresponse> getservicesandimages(int page, int size) {
        this.validatePagenumberandSize(page,size);

        try {

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            Page<Services> services = servicesrepo.findAll(pageable);
//              log.info("usercards {}",services);


            if (services.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), services.getNumber(), services.getSize(), services.getTotalElements(), services.getTotalPages(), services.isLast());
            }
            List<Servicesimageesresponse> gameroomsresponses = services.map(ModelMapper::mapservicestoserviceresponse).getContent();
//              log.info("logging gameroomsresponses {}", gameroomsresponses);
            return new PagedResponse<>(gameroomsresponses, services.getNumber(), services.getSize(), services.getTotalElements(), services.getTotalPages(), services.isLast());

        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching services "+e.getMessage());
        }
    }
    public ApiResponse validatePagenumberandSize(int page, int size) {

        try {
            if (page < 0) {
                return new ApiResponse(false, "page number may not be less than zero", HttpStatus.BAD_REQUEST);

            }
            if (size > MAX_PAGE_SIZE) {
                return new ApiResponse(false, "page size may not be greater than " + MAX_PAGE_SIZE, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ApiResponse(false, "an exception has occurred while validating page number and size " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    public Serviceimageresponse serviceimageidget(Long serviceimageid) {


        try {
            ServiceImage image=serviceImagerepo.findById(serviceimageid).orElseThrow(() -> new NotFoundException("Service image with the provided id could not be found"));
             return Serviceimageresponse.builder().message("success").status(true).serviceImage(image).build();
        } catch (Exception e) {
            // TODO: handle exception
            return Serviceimageresponse.builder()
            .message("An exception has occurred while getting the service image")
            .status(false)
            .serviceImage(null)
            .build();

        }
    }

    public Object getattendants() {

        try {
            List<Attendant> attendantList=attendantrepo.findAll();
            return attendantList;
        }catch (Exception e){
            return "An exception has occurred when getting attemdants";
        }
    }
    public Saveattendantresponse saveattendant(@CurrentUser UserPrincipal curentUser, Saveattendantrequest saveattendantrequest){
        if (curentUser==null){
          return Saveattendantresponse.builder().message("You are not authentcated").status(false).build();
        }
        try {
            boolean existsbymobile=attendantrepo.existsByMobile(saveattendantrequest.getMobile());
            if (existsbymobile){
                return Saveattendantresponse.builder().message("The mobile has already been taken").attendant(saveattendantrequest.getMobile()).status(false).build();

            }
            Attendant attendant=new Attendant();
            attendant.setName(saveattendantrequest.getName());
            attendant.setMobile(saveattendantrequest.getMobile());
            Attendant attendant1s=attendantrepo.save(attendant);
            return Saveattendantresponse.builder().attendant(attendant1s.getName()).message("new attendant saved").status(true).build();
        }catch (Exception e){
            return Saveattendantresponse.builder().message("An exception has occurred while trying to save the attendant "+e.getMessage()).status(false).build();
        }
    }


    public Saveattendantresponse serviceupdateisonoffer(UserPrincipal currentUser, Long serviceid) {
    if (currentUser==null){
        return Saveattendantresponse.builder().message("You are not authentcated").status(false).build();

    }
        try {
            Services services=servicesrepo.findById(serviceid).orElseThrow(() -> new NotFoundException("service not found"));
            services.setIsonoffer(!services.isIsonoffer());
            servicesrepo.save(services);
            return Saveattendantresponse.builder().status(true).message("service status changed").build();
        }catch (Exception e){
            return Saveattendantresponse.builder().message("An exception has occurred while trying to save the attendant "+e.getMessage()).status(false).build();

        }

    }

    public Saveattendantresponse deleteservice(UserPrincipal currentUser, Long serviceid) {
        if (currentUser==null){
            return Saveattendantresponse.builder().message("You are not authentcated").status(false).build();

        }
        try {
            Services services=servicesrepo.findById(serviceid).orElseThrow(() -> new NotFoundException("SErvice not found"));
            serviceImagerepo.deleteAllByService(services);
            servicesrepo.deleteById(serviceid);
            return Saveattendantresponse.builder().message("service deleted").status(true).build();

        }catch (Exception e){
            return Saveattendantresponse.builder().message("An exception has occurred whiledeleting service "+e.getMessage()).status(false).build();

        }
    }
}
