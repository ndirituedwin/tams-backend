package com.tamsbeauty.mapper;

import com.tamsbeauty.Dto.Response.Servicesimageesresponse;
import com.tamsbeauty.Entity.Services;

public class ModelMapper {
    public static Servicesimageesresponse mapservicestoserviceresponse(Services services) {
        Servicesimageesresponse servicesimageesresponse=new Servicesimageesresponse();
        servicesimageesresponse.setServices(services);
        servicesimageesresponse.setServiceImageList(services.getServiceImageList());
        return servicesimageesresponse;

    }
}
