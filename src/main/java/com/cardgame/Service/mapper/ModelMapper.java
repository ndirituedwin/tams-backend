package com.cardgame.Service.mapper;

import com.cardgame.Dto.responses.CardFeeResponse;
import com.cardgame.Dto.responses.PackFeeResponse;
import com.cardgame.Dto.responses.UserCardResponse;
import com.cardgame.Entity.PackPricelisting;
import com.cardgame.Entity.UserCard;
import com.cardgame.Entity.Usercardfee;

public class ModelMapper {


    public static UserCardResponse mapusercardstousercardsresponse(UserCard userCard) {
    UserCardResponse userCardResponse=new UserCardResponse();
    userCardResponse.setId(userCard.getId());
    userCardResponse.setUser(userCard.getUser().getId());
    userCardResponse.setCardduplicate(userCard.getCardduplicate().getId());
    userCardResponse.setPack(userCard.getPack().getId());
    return userCardResponse;

    }

    public static CardFeeResponse mapcardfeetocardfeeresponse(Usercardfee usercardfee) {
        CardFeeResponse userCardResponse=new CardFeeResponse();
        userCardResponse.setId(usercardfee.getId());
        userCardResponse.setUser(usercardfee.getUser().getId());
        userCardResponse.setFeeamount(usercardfee.getFeeamount());
        userCardResponse.setUserCard(usercardfee.getUserCard().getId());
        return userCardResponse;

    }

    public static PackFeeResponse mapPackfeetopackfeeresponse(PackPricelisting packPricelisting) {
        PackFeeResponse userCardResponse=new PackFeeResponse();
        userCardResponse.setUser(packPricelisting.getUser().getId());
        userCardResponse.setPackname(packPricelisting.getPack().getPackname());
        userCardResponse.setAmount(packPricelisting.getFeeamount());
        userCardResponse.setPacktype(packPricelisting.getPack().getPacktype());
        return userCardResponse;

    }
}
