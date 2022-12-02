package com.tamsbeauty.Service;


import com.tamsbeauty.Dto.Response.Nailofweekresponse;
import com.tamsbeauty.Entity.WeekNail;
import com.tamsbeauty.Exceptions.NotFoundException;
import com.tamsbeauty.Repo.Nailofweekrepo;
import com.tamsbeauty.Security.UserPrincipal;
import com.tamsbeauty.config.Fileupload.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.tamsbeauty.config.ApiUtils.FOLDER_PATH;
import static com.tamsbeauty.config.ApiUtils.FOLDER_PATH_TWO;

@Service
@Slf4j
public class Nailoweekservice {


    private final Nailofweekrepo nailofweekrepo;
    private final Servicesservice servicesservice;

    public Nailoweekservice(Nailofweekrepo nailofweekrepo, Servicesservice servicesservice) {
        this.nailofweekrepo = nailofweekrepo;
        this.servicesservice = servicesservice;
    }

    public Nailofweekresponse addnail(UserPrincipal currentUser, MultipartFile multipartFile, BigDecimal cost, String  description) {
        if (currentUser==null){
            return Nailofweekresponse.builder().status(false).message("you are not authenticated").build();
        }
        try {
            Path uploadPath= Paths.get(FOLDER_PATH_TWO);
            createdirsifnotexist();
            InputStream inputStream=multipartFile.getInputStream();
            String filename=multipartFile.getOriginalFilename();

            Path filePath=uploadPath.resolve(filename);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);

            WeekNail nail=new WeekNail();
            nail.setDescription(description);
            nail.setImage(filename);
            nail.setPrice(cost);
            WeekNail nail1=nailofweekrepo.save(nail);
            return Nailofweekresponse.builder().status(true).description(nail1.getDescription()).message("Nail of week saved").build();
        }catch (Exception e){
            return Nailofweekresponse.builder().description("An error").build();
        }
    }

    public Nailofweekresponse nailofweekresponse(){
//        if (currentUser==null){
//            return Nailofweekresponse.builder().message("You are not authenticated").status(false).build();
//        }
        try {
            List<WeekNail> weekNailList=nailofweekrepo.findAll();
             return Nailofweekresponse.builder().status(true).weekNailList(weekNailList).build();
        }catch (Exception e){
            return Nailofweekresponse.builder().description("An error").build();

        }

    }

    void createdirsifnotexist() {
//        create directory to save files
        File directory =new File(FileUtil.folderPathtwo);
        if (!directory.exists()){
            directory.mkdir();
        }
    }

    public Nailofweekresponse deletenailoftheweek(UserPrincipal currentUser, Long nailid) {

        if (currentUser==null){
            return Nailofweekresponse.builder().status(false).message("you are not authenticated").build();
        }
        try {
            WeekNail nail=nailofweekrepo.findById(nailid).orElseThrow(() -> new NotFoundException("nail not found"));
            
            nailofweekrepo.deleteById(nail.getId());
            return Nailofweekresponse.builder().status(true).message("Nail deleted").build();
            
        } catch (Exception e) {
            return Nailofweekresponse.builder().description("An error").build();

        }
    }
}
