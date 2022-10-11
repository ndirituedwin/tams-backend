package com.cardgame.Service.mapper;

import com.cardgame.Dto.responses.*;
import com.cardgame.Entity.*;

public class ModelMapper {





    public static UserCardResponse mapusercardstousercardsresponse(UserCard userCard) {
    UserCardResponse userCardResponse=new UserCardResponse();
    userCardResponse.setId(userCard.getId());
    userCardResponse.setUser(userCard.getUser().getId());
    userCardResponse.setCardduplicate(userCard.getCardduplicate().getId());
    userCardResponse.setPack(userCard.getUnopenedpack().getId());
    return userCardResponse;

    }

    public static CardFeeResponse mapcardfeetocardfeeresponse(Usercardfee usercardfee) {
        CardFeeResponse userCardResponse=new CardFeeResponse();
        userCardResponse.setId(usercardfee.getId());
        userCardResponse.setUser(usercardfee.getUser().getId());
        userCardResponse.setFeeamount(usercardfee.getFeeamount());
        userCardResponse.setUserCard(usercardfee.getId());
        return userCardResponse;

    }

    public static PackFeeResponse mapPackfeetopackfeeresponse(PackPricelisting packPricelisting) {
        PackFeeResponse userCardResponse=new PackFeeResponse();
        userCardResponse.setUser(packPricelisting.getUser());
        userCardResponse.setId(packPricelisting.getId());
        userCardResponse.setFeeamount(packPricelisting.getFeeamount());
        userCardResponse.setUnopenedpack(packPricelisting.getUnopenedpack());
        return userCardResponse;

    }
    

    public static GameRoomResponse mapgameroomtogameroomresponse(Gameroommaster gameRoom) {
        GameRoomResponse gameRoomResponse=new GameRoomResponse();
        gameRoomResponse.setId(gameRoom.getId());
        gameRoomResponse.setCreateddate(gameRoom.getCreateddate());
        gameRoomResponse.setMinimumamount(gameRoom.getAmount());
        gameRoomResponse.setNumberofusers(gameRoom.getParticipants());
        return gameRoomResponse;
    }

    public static GameRoomUsersResponse mapgameroomusestogameroomusersresponse(GameRoomTable gameroomusers) {
        GameRoomUsersResponse gameRoomUsersResponse=new GameRoomUsersResponse();
        gameRoomUsersResponse.setId(gameroomusers.getId());
        gameRoomUsersResponse.setGameroommaster(gameroomusers.getGameroommaster());
        gameRoomUsersResponse.setNumberofusers(gameroomusers.getNumberofusers());
        gameRoomUsersResponse.setUidone(gameroomusers.getUidone());
        gameRoomUsersResponse.setUidtwo(gameroomusers.getUidtwo());
        gameRoomUsersResponse.setUidthree(gameroomusers.getUidthree());
        gameRoomUsersResponse.setUidfour(gameroomusers.getUidfour());
        gameRoomUsersResponse.setUidfive(gameroomusers.getUidfive());
        return gameRoomUsersResponse;

    }

    public static RoomTableusersresponse maptableuserstotableuserresponse(User user,Long tableid) {
        RoomTableusersresponse roomTableusersresponse=new RoomTableusersresponse();
        roomTableusersresponse.setUserwallet(user.getUserwallet());
        roomTableusersresponse.setBuyIn(user.getBuyIns().stream().filter(buyIn -> buyIn.getGameRoomTable().getId()==tableid).findFirst().orElse(null));
        roomTableusersresponse.setUserbestcardList(user.getUserbestcards());
        return roomTableusersresponse;
    }

    public static Buyintableresponse mapbuyinstobuyinresponse(BuyIn buyIn1) {
        Buyintableresponse buyintableresponse=new Buyintableresponse();
        buyintableresponse.setAmount(buyIn1.getAmount());
        buyintableresponse.setBuyinid(buyIn1.getId());
        buyintableresponse.setGameroommasterid(buyIn1.getGameroommaster().getId());
        buyintableresponse.setGameroomtable(buyIn1.getGameRoomTable().getId());
        buyintableresponse.setUid(buyIn1.getUser().getUID());
        return buyintableresponse;
    }


    public static Cardsownedbycurrentplayerresponse Cardsownedbycurrentplayerresponse(UserCard userCard, User user) {
//        public static Cardsownedbycurrentplayerresponse Cardsownedbycurrentplayerresponse(UserCard userCard) {
        Cardsownedbycurrentplayerresponse cardsownedbycurrentplayerresponse =new Cardsownedbycurrentplayerresponse();

        cardsownedbycurrentplayerresponse.setUid(user.getUID());
        cardsownedbycurrentplayerresponse.setUsername(user.getUsername());
//        cardsownedbycurrentplayerresponse.setUserwallet(user.getUserwallet());
        cardsownedbycurrentplayerresponse.setUserCard(userCard);
        return cardsownedbycurrentplayerresponse;

    }

    public static GetUserBest30cardsresponse mapuserbestcardstouserbestcardsresponse(User user, Userbestcard userbestcard) {

        GetUserBest30cardsresponse getUserBest30cardsresponse=new GetUserBest30cardsresponse();
        getUserBest30cardsresponse.setUid(user.getUID());
        getUserBest30cardsresponse.setUsername(user.getUsername());
        getUserBest30cardsresponse.setUserbestcard(userbestcard);
//        getUserBest30cardsresponse.setUserbestcardList(userbestcard);

   return getUserBest30cardsresponse;
    }

    public static Marketplacecardsresponse mapmarketplacepackstomarketplaceresponse(Pack pack) {
        Marketplacecardsresponse marketplacecardsresponse=new Marketplacecardsresponse();
//        marketplacecardsresponse.setPack(pack);
        marketplacecardsresponse.setId(pack.getId());
        marketplacecardsresponse.setNumberofcards(pack.getNumberofcards());
        marketplacecardsresponse.setGold(pack.getGold());
        marketplacecardsresponse.setSilver(pack.getSilver());
        marketplacecardsresponse.setBronze(pack.getBronze());
        marketplacecardsresponse.setTrainer(pack.getTrainer());
        marketplacecardsresponse.setImage(pack.getImage());
        marketplacecardsresponse.setPrice(pack.getPrice());
        marketplacecardsresponse.setPacktype(pack.getPacktype());
        marketplacecardsresponse.setPackname(pack.getPackname());
        return marketplacecardsresponse;
    }


//    public static RoomTableusersresponse maptableuserstotableuserresponse(User user) {
//        RoomTableusersresponse roomTableusersresponse=new RoomTableusersresponse();
//        roomTableusersresponse.setUser(user);
//        return roomTableusersresponse;
//    }
}
