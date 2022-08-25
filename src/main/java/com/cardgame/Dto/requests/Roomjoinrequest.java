package com.cardgame.Dto.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class Roomjoinrequest {



    private Long userid;
    private Long mastertableid;


    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getMastertableid() {
        return mastertableid;
    }

    public void setMastertableid(Long mastertableid) {
        this.mastertableid = mastertableid;
    }

}
