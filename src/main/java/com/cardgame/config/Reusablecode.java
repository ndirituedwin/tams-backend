//    public JoinRoomResponse joinRoom1(Roomjoinrequest roomjoinrequest){
/** public ResponseEntity<?> joinRoom1(Roomjoinrequest roomjoinrequest) {

 log.info("roomjoinrequest get amount, gameroomid,user id  {}    {}    {}", roomjoinrequest.getAmount(), roomjoinrequest.getGameroomid(), roomjoinrequest.getUserid());

 try {

 //           BigDecimal bigDecimal=new BigDecimal(roomjoinrequest.getMinimumamount());
 ////           log.info("bigdecima {}",bigDecimal);
 User user = loginrepo.findByUID(roomjoinrequest.getUserid()).orElseThrow(() -> new UserNotFoundException("user with the provided uid not found"));
 Userwallet userwallet = userwalletrepo.findByUserid(user.getUID()).orElseThrow(() -> new UserWalletNotFoundException("user wallet nt found"));
 if (roomjoinrequest.getAmount().compareTo(userwallet.getTotalwalletbalance()) == -1) {
 List<GameRoom> gameroomList = gameRoomrepo.findAll();
 if (gameroomList.isEmpty()) {
 GameRoom gameRoom = new GameRoom();
 gameRoom.setStatus(true);
 //               gameRoom.setMinimumamount(new BigDecimal(roomjoinrequest.getMinimumamount()));
 gameRoom.setMinimumamount(roomjoinrequest.getAmount());
 gameRoom.setCreateddate(Instant.now());

 gameRoom.setNumberofusers(gameRoom.getNumberofusers() + 1);
 GameRoom savedgameroom = gameRoomrepo.save(gameRoom);
 Gameroomusers gameroomusers = new Gameroomusers();
 gameroomusers.setGameRoom(savedgameroom);
 gameroomusers.setUser(user);
 gameroomusersrepo.save(gameroomusers);
 } else {

 List<GameRoom> gameroomList1 = gameRoomrepo.findAll();
 GameRoom agam = gameroomList1.stream()
 //                       .peek(gameRoom -> log.info("gameroom{} {} {} {}",gameRoom.getCreateddate(),gameRoom.getId(),gameRoom.getNumberofusers(),gameRoom.getMinimumamount()))
 //                       .filter(gameRoom -> gameRoom.getNumberofusers()<5 && gameRoom.getMinimumamount().toString().split("[.]")[0].equals(roomjoinrequest.getMinimumamount()))
 .filter(gameRoom -> gameRoom.getNumberofusers() < 5)
 .findFirst()
 .orElse(null);
 if (agam == null) {
 GameRoom gameRoom1 = new GameRoom();
 gameRoom1.setStatus(true);
 //                   gameRoom1.setMinimumamount(new BigDecimal(roomjoinrequest.getMinimumamount()));
 gameRoom1.setMinimumamount(roomjoinrequest.getAmount());
 gameRoom1.setCreateddate(Instant.now());

 gameRoom1.setNumberofusers(gameRoom1.getNumberofusers() + 1);
 GameRoom savedgameroom1 = gameRoomrepo.save(gameRoom1);
 Gameroomusers gameroomusers1 = new Gameroomusers();
 gameroomusers1.setGameRoom(savedgameroom1);
 gameroomusers1.setUser(user);
 gameroomusersrepo.save(gameroomusers1);
 //                   log.info("here instead");
 } else {
 Gameroomusers gameroomusers = new Gameroomusers();
 gameroomusers.setUser(user);
 gameroomusers.setGameRoom(agam);
 Long useridd = gameroomusersrepo.save(gameroomusers).getUser().getUID();
 agam.setNumberofusers(agam.getNumberofusers() + 1);
 Integer nou = gameRoomrepo.save(agam).getNumberofusers();
 //buy in

 try {
 //                       log.info("goes through here {}",roomjoinrequest);

 if (roomjoinrequest.getGameroomid() == null || roomjoinrequest.getAmount() == null) {
 //                           return new ro("one of the values of the request body is null");
 }
 User user_not_foun = loginrepo.findByUID(useridd).orElseThrow(() -> new UserNotFoundException("user not foun"));
 GameRoom room = gameRoomrepo.findById(agam.getId()).orElseThrow(() -> new GameNotFoundException("Game room not found"));
 boolean existsbyuseridandgameroomid = buyinrepo.existsByUserAndGameRoom(user, room);
 if (existsbyuseridandgameroomid) {
 System.out.println("Buy in exists buy the user and game room ");
 //                           return new BUyinresponse("buy in exists buy the user and game room ");
 }
 BuyIn buyIn = new BuyIn();
 buyIn.setGameRoom(room);
 buyIn.setUser(user_not_foun);
 buyIn.setCreatedDate(Instant.now());
 buyIn.setAmount(roomjoinrequest.getAmount());
 buyinrepo.save(buyIn);
 System.out.println("BUy in of amount " + roomjoinrequest.getAmount() + " and for the user " + user_not_foun.getUsername() + " saved successfully!");
 //                       return new BUyinresponse("BUy in of amount "+buyinrequest.getAmount()+" and for the user "+user.getUsername()+" saved successfully!");
 } catch (Exception e) {
 return ResponseEntity.ok("An exception has occurred while saving buyin " + e.getMessage());

 //                       return new BUyinresponse("An exception has occurred while saving buyin "+e.getMessage());
 }
 //end
 //                   log.info("agam {} {} {}",agam.getId(),agam.getCreateddate(),agam.getNumberofusers());
 }

 }
 }

 return new ResponseEntity<>("game room saven", HttpStatus.OK);
 //        return ResponseEntity.ok(this.userbestthirtycards(roomjoinrequest));


 } catch (Exception e) {
 return ResponseEntity.ok("an exception has occurred when joining game room " + e.getMessage());
 //           return new JoinRoomResponse("an exception has occurred when joining game room "+e.getMessage());
 }
 }
 */
/**  @Transactional
public ResponseEntity<?> joinRoo(Roomjoinrequest roomjoinrequest) {
if ( roomjoinrequest.getAmount() == null || roomjoinrequest.getUserid() == null) {
return ResponseEntity.ok("one of the values of the request body is null");
}

try {
Gameroomusers gameroomuser;
GameRoom savedgameroom;
//            check if the game room exists.If it does not exists no moving on
GameRoom gameRoom = gameRoomrepo.findById(roomjoinrequest.getGameroomid()).orElseThrow(() -> new GameNotFoundException("game room not found"));
//        check if user exists
User user = loginrepo.findByUID(roomjoinrequest.getUserid()).orElseThrow(() -> new UserNotFoundException("user with the provided uid not found"));
//            getting user wallet balance
Userwallet userwallet = userwalletrepo.findByUserid(user.getUID()).orElseThrow(() -> new UserWalletNotFoundException("user wallet nt found"));
//            check if the walet balance is greater than wamount user wants to deposit.
if (roomjoinrequest.getAmount().compareTo(userwallet.getTotalwalletbalance()) < 0) {
if (roomjoinrequest.getAmount().compareTo(gameRoom.getMinimumamount()) > 0) {

List<GameRoom> gameroomList1 = gameRoomrepo.findAll();
GameRoom agam = gameroomList1.stream()
//                       .peek(gameRoom -> log.info("gameroom{} {} {} {}",gameRoom.getCreateddate(),gameRoom.getId(),gameRoom.getNumberofusers(),gameRoom.getMinimumamount()))
//                       .filter(gameRoom -> gameRoom.getNumberofusers()<5 && gameRoom.getMinimumamount().toString().split("[.]")[0].equals(roomjoinrequest.getMinimumamount()))
.filter(gameRoom2 -> gameRoom2.getNumberofusers() < 5)
.findFirst()
.orElse(null);
if (agam == null) {
return ResponseEntity.ok("All game rooms are full!!");
} else {
//              check game room users if there is a user with same game room and same user id
//                    boolean existsByUseridAndGameRoom = gameroomusersrepo.existsByUseridAndGameRoom(user.getUID(), gameRoom);
boolean existsByUseridAndGameRoom = gameroomusersrepo.existsByUserAndGameRoom(user, gameRoom);
if (existsByUseridAndGameRoom) {
return ResponseEntity.ok("You cannot join the game room twice ");
} else {
//                     if game room user does not existwith same user id and game room
Gameroomusers gameroomusers = new Gameroomusers();
gameroomusers.setUser(user);
gameroomusers.setGameRoom(agam);
gameroomusers.setUserwallet(userwallet);
gameroomuser = gameroomusersrepo.save(gameroomusers);
agam.setNumberofusers(agam.getNumberofusers() + 1);
agam.setStatus(true);
savedgameroom = gameRoomrepo.save(agam);

//                    GameRoom room = gameRoomrepo.findById(agam.getId()).orElseThrow(() -> new GameNotFoundException("Game room not found"));
boolean existsbyuseridandgameroomid = buyinrepo.existsByUserAndGameRoom(user, agam);
if (existsbyuseridandgameroomid) {
System.out.println("Buy in exists buy the user and game room ");
//                           return new BUyinresponse("buy in exists buy the user and game room ");
}
BuyIn buyIn = new BuyIn();
buyIn.setGameRoom(agam);
buyIn.setUser(user);
buyIn.setCreatedDate(Instant.now());
buyIn.setAmount(roomjoinrequest.getAmount());
buyinrepo.save(buyIn);
userwallet.setTotalwalletbalance(userwallet.getTotalwalletbalance().subtract(roomjoinrequest.getAmount()));
userwalletrepo.save(userwallet);
System.out.println("BUy in of amount " + roomjoinrequest.getAmount() + " and for the user " + user.getUsername() + " saved successfully!");
//                       return new BUyinresponse("BUy in of amount "+buyinrequest.getAmount()+" and for the user "+user.getUsername()+" saved successfully!");


}
}

} else {
return ResponseEntity.ok("the amount you entered is less than the minimum amount for the game room : the game room minimum amoun for the game room is: " + gameRoom.getMinimumamount());
}

} else {
return ResponseEntity.ok("You do not have enough money on your wallet to join the game room, your balance is: " + userwallet.getTotalwalletbalance());
}

simpMessagingTemplate.convertAndSend("/gameroom/successfully-joined/" + gameRoom.getId(), gameroomuser);
return ResponseEntity.ok("game room saved");
//            return ResponseEntity.ok(this.userbestthirtycards(roomjoinrequest.getUserid()));


} catch (Exception e) {
return ResponseEntity.ok("an exception has occurred when joining game room " + e.getMessage());
//           return new JoinRoomResponse("an exception has occurred when joining game room "+e.getMessage());
}
}

    gameRoomTable.setUidone(Objects.equals(longs.get(0), "") ?null: Long.valueOf(longs.get(0)));
            gameRoomTable.setUidtwo(Long.parseLong(longs.get(1)));
            gameRoomTable.setUidthree(Long.parseLong(longs.get(2)));
            gameRoomTable.setUidfour(Long.parseLong(longs.get(3)));
            gameRoomTable.setUidfive(Long.parseLong(longs.get(4)));*/


/**
public BuyPackresponse buypack(BuyPackRequest buyPackRequest) {

    /**    if (buyPackRequest.getPackid()==null|| buyPackRequest.getPackid()==0L || buyPackRequest.getUid()==null){
        return new BuyPackresponse("Invalid pack details "+buyPackRequest.getPackid());
        }
        AtomicReference<String> response= new AtomicReference<>("");

        try {
        User user = userrepo.findById(buyPackRequest.getUid()).orElseThrow(() -> new UserNotFoundException("User Not found exception "));
        System.out.println("the user "+user);
        Unopenedpack unopenedpack=unopenedpackrepo.findById(buyPackRequest.getPackid()).orElseThrow(() -> new PackNotFoundException("purchase pack not found"));
//            Pack pack = packrepo.findById(unopenedpack.getPack().getId()).orElseThrow(() -> new PackNotFoundException("pack not found"));

        Userwallet userwalletbalance=userwalletrepo.findByUserid(user.getUID()).orElseThrow(() -> new UserWalletNotFoundException("user wallet not found"));
//            PackPricelisting packPricelisting =packfeelistingrepo.findByUnopenedpack(unopenedpack).orElseThrow(() -> new PackNotFoundException("pack fee not found") );



        boolean packcardexistsbyusserandunopenedcard=packfeelistingrepo.existsByUserAndUnopenedpack(user,unopenedpack);
//            boolean packcardexistsbyusserandunopenedcard=packfeelistingrepo.existsByUserAndUnopenedpackAndStatus(user,unopenedpack,ACTIVE);
        boolean packPricelistingexists =packfeelistingrepo.existsByUnopenedpackAndStatus(unopenedpack,ACTIVE);

        if (packPricelistingexists){
        PackPricelisting packPricelisting =packfeelistingrepo.findByUnopenedpackAndStatus(unopenedpack,ACTIVE).orElseThrow(() -> new PackNotFoundException("pack fee not found") );
        System.out.println("pack price listing "+packPricelisting.getStatus());
        if (unopenedpack.isIsopen()){
        if (packcardexistsbyusserandunopenedcard){
        return new BuyPackresponse("You cannot buy sam pack twice");
        }
        if (userwalletbalance.getTotalwalletbalance().compareTo(packPricelisting.getFeeamount())<0){
        return new BuyPackresponse("You do not have enough money on your wallet to purchase the pack");
        }
        if (packPricelisting.getStatus().equalsIgnoreCase(ACTIVE)){
        List<UserCard> userCard = userCardRepo.findAllByUnopenedpack(unopenedpack);
        userCard.forEach(userCard1 -> {
        Long originaluser = userCard1.getUser().getUID();
        userCard1.setUser(user);
        Long newuserid = userCardRepo.save(userCard1).getUser().getUID();
//            after changing th user update the Carduserchange table
        Carduserchangetracking carduserchangetracking = new Carduserchangetracking();
        carduserchangetracking.setAmountbought(packPricelisting.getFeeamount());
        carduserchangetracking.setOriginaluser(originaluser);
        carduserchangetracking.setNewuser(newuserid);
        carduserchangetracking.setCreatedat(Instant.now());
        carduserchangetrackingrepo.save(carduserchangetracking);

//            now add the money to the original user wallet
        boolean userexistsinuserwallet = userwalletrepo.existsByUserid(originaluser);
        if (userexistsinuserwallet) {
        Userwallet userwallet = userwalletrepo.findByUserid(originaluser).orElseThrow(() -> new UserWalletNotFoundException("user wallet not found"));
        BigDecimal originalbaance = userwallet.getTotalwalletbalance();
//                    userwallet.setTotalwalletbalance(originalbaance.add(buyPackRequest.getAmount()));
        userwallet.setTotalwalletbalance(originalbaance.add(packPricelisting.getFeeamount()));
        Userwallet userwallet1 = userwalletrepo.save(userwallet);
        userwalletbalance.setTotalwalletbalance(userwalletbalance.getTotalwalletbalance().subtract(packPricelisting.getFeeamount()));
        userwalletrepo.save(userwalletbalance);
        //                now update the deposits table
        Userdeposit userdeposit = new Userdeposit();
        userdeposit.setUserwallet(userwallet1);
        userdeposit.setUserid(user.getUID());
        userdeposit.setAmountadded(packPricelisting.getFeeamount());
        userdeposit.setOrderid(buyPackRequest.getOrderid());
        userdeposit.setPaymentid(buyPackRequest.getPaymentid());
        userdepositrepo.save(userdeposit);
//                now update user wallet changes table
        Changingwalletbalance changingwalletbalance = new Changingwalletbalance();
        changingwalletbalance.setPreviousbalance(originalbaance);
        changingwalletbalance.setNewbalance(userwallet1.getTotalwalletbalance());
        changingwalletbalance.setAction(BOUGHT_PACK);
        changingwalletbalance.setUserid(user.getUID());
        changingwalletbalance.setUserwallet(userwallet1);
        changingwalletbalancerepo.save(changingwalletbalance);
        unopenedpack.setIsopen(true);
        unopenedpack.setUser(user);
        unopenedpackrepo.save(unopenedpack);

        response.set("You have successfully bought the pack for " +packPricelisting.getFeeamount());

//                       return new BuyPackresponse("You have successfully bought the pack for "+buyPackRequest.getAmount());
        } else {
//                       return new BuyPackresponse("The user you are buying from does not exist");
        }


        });
        Boughtpacktracking boughtpacktracking=new Boughtpacktracking();
        boughtpacktracking.setNewuser(user.getUID());
        boughtpacktracking.setAmount(packPricelisting.getFeeamount());
        boughtpacktracking.setCreatedat(Instant.now());
        boughtpacktracking.setPackStatus(PackStatus.SOLD);
        boughtpacktracking.setUnopenedpack(unopenedpack);
        boughtpacktrackingrepo.save(boughtpacktracking);
        packPricelisting.setStatus(PACK_SOLD);
//                packPricelisting.setUser(user);
        packfeelistingrepo.save(packPricelisting);




        }else{
        return new BuyPackresponse("The pack is inactive or has already been sold");

        }
        }else{
        return new BuyPackresponse("No unopened pack");
        }

        }else{
        return new BuyPackresponse("This pack is inactive or has already been sold");

        }*/

        /**          if (userwalletbalance.getTotalwalletbalance().compareTo(packPricelisting.getFeeamount()) <0){
         return new BuyPackresponse("You do not have enough money on your wallet to purchase the pack");
         }

         if (unopenedpack.isIsopen()) {
         if (packcardexistsbyusserandunopenedcard){
         return new BuyPackresponse("you cannot buy same pack twice");
         }


         if (packPricelisting.getStatus().equalsIgnoreCase(ACTIVE)) {

         List<UserCard> userCard = userCardRepo.findAllByUnopenedpack(unopenedpack);
         userCard.forEach(userCard1 -> {
         Long originaluser = userCard1.getUser().getUID();
         userCard1.setUser(user);
         Long newuserid = userCardRepo.save(userCard1).getUser().getUID();
         //            after changing th user update the Carduserchange table
         Carduserchangetracking carduserchangetracking = new Carduserchangetracking();
         carduserchangetracking.setAmountbought(packPricelisting.getFeeamount());
         carduserchangetracking.setOriginaluser(originaluser);
         carduserchangetracking.setNewuser(newuserid);
         carduserchangetracking.setCreatedat(Instant.now());
         carduserchangetrackingrepo.save(carduserchangetracking);

         //            now add the money to the original user wallet
         boolean userexistsinuserwallet = userwalletrepo.existsByUserid(originaluser);
         if (userexistsinuserwallet) {
         Userwallet userwallet = userwalletrepo.findByUserid(originaluser).orElseThrow(() -> new UserWalletNotFoundException("user wallet not found"));
         BigDecimal originalbaance = userwallet.getTotalwalletbalance();
         //                    userwallet.setTotalwalletbalance(originalbaance.add(buyPackRequest.getAmount()));
         userwallet.setTotalwalletbalance(originalbaance.add(packPricelisting.getFeeamount()));
         Userwallet userwallet1 = userwalletrepo.save(userwallet);
         userwalletbalance.setTotalwalletbalance(userwalletbalance.getTotalwalletbalance().subtract(packPricelisting.getFeeamount()));
         userwalletrepo.save(userwalletbalance);
         //                now update the deposits table
         Userdeposit userdeposit = new Userdeposit();
         userdeposit.setUserwallet(userwallet1);
         userdeposit.setUserid(user.getUID());
         userdeposit.setAmountadded(packPricelisting.getFeeamount());
         userdeposit.setOrderid(buyPackRequest.getOrderid());
         userdeposit.setPaymentid(buyPackRequest.getPaymentid());
         userdepositrepo.save(userdeposit);
         //                now update user wallet changes table
         Changingwalletbalance changingwalletbalance = new Changingwalletbalance();
         changingwalletbalance.setPreviousbalance(originalbaance);
         changingwalletbalance.setNewbalance(userwallet1.getTotalwalletbalance());
         changingwalletbalance.setAction(BOUGHT_PACK);
         changingwalletbalance.setUserid(user.getUID());
         changingwalletbalance.setUserwallet(userwallet1);
         changingwalletbalancerepo.save(changingwalletbalance);
         unopenedpack.setIsopen(true);
         unopenedpack.setUser(user);
         unopenedpackrepo.save(unopenedpack);

         response.set("You have successfully bought the pack for " +packPricelisting.getFeeamount());

         //                       return new BuyPackresponse("You have successfully bought the pack for "+buyPackRequest.getAmount());
         } else {
         //                       return new BuyPackresponse("The user you are buying from does not exist");
         }


         });
         Boughtpacktracking boughtpacktracking=new Boughtpacktracking();
         boughtpacktracking.setNewuser(user.getUID());
         boughtpacktracking.setAmount(packPricelisting.getFeeamount());
         boughtpacktracking.setCreatedat(Instant.now());
         boughtpacktracking.setPackStatus(PackStatus.SOLD);
         boughtpacktracking.setUnopenedpack(unopenedpack);
         boughtpacktrackingrepo.save(boughtpacktracking);
         packPricelisting.setStatus(PACK_SOLD);
         //                packPricelisting.setUser(user);
         packfeelistingrepo.save(packPricelisting);
         }else {
         return new BuyPackresponse("The pack is inactive or has already been sold");
         }
         }else{
         response.set("NO unopened pack");
         }
         */

        /**return new BuyPackresponse(response.get());
//               return new BuyPackresponse("response.get()");

        }catch (Exception e){
        return new BuyPackresponse("An exception has occurred while trying to buy pack "+e.getMessage());
        }


        }*/


/*
@Transactional
public Buycardresponse buycard(BuyCardRequest buyCardRequest) {

        try {
//            check if user is logged in
        User user=userrepo.findById(buyCardRequest.getUid()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Userwallet userwallet=userwalletrepo.findByUserid(user.getUID()).orElseThrow(() -> new UserWalletNotFoundException("Your user wallet could not be found"));
        UserCard userCard=userCardRepo.findById(Long.parseLong(buyCardRequest.getCardid())).orElseThrow(() -> new UserCardNotFoundException("User card could not be found"));
        List<Usercardfee> usercardfees=usercardfeerepo.findAllByUserCardAndStatus(userCard,ACTIVE);

        AtomicReference<String> message= new AtomicReference<>("");

        usercardfees.forEach(usercardfee ->{
        BigDecimal originalcardfeeamount=usercardfee.getFeeamount();
        if (userwallet.getTotalwalletbalance().compareTo(usercardfee.getFeeamount()) > 0){
        if (!userCard.isOpenedcard()){
        message.set("the card is not opened");
        }
        if (buyCardRequest.getUid()==userCard.getUser().getUID()){
        message.set("You cannot sell your own cards");
        }


        if (usercardfee.getStatus().equalsIgnoreCase(ACTIVE)) {
        System.out.println("reaching here ");
        Long orginalcarduser = userCard.getUser().getUID();
        userCard.setUser(user);
        Long newuserid = userCardRepo.save(userCard).getUser().getUID();
//            after changing th user update the Carduserchange table
        Carduserchangetracking carduserchangetracking = new Carduserchangetracking();
        carduserchangetracking.setAmountbought(usercardfee.getFeeamount());
        carduserchangetracking.setOriginaluser(orginalcarduser);
        carduserchangetracking.setNewuser(newuserid);
        carduserchangetracking.setCreatedat(Instant.now());
        carduserchangetrackingrepo.save(carduserchangetracking);
        usercardfee.setStatus(CARD_SOLD);
//                    usercardfee.setUser(user);
        usercardfeerepo.save(usercardfee);

//            now add the money to the original user wallet
        boolean userexistsinuserwallet = userwalletrepo.existsByUserid(orginalcarduser);
        if (userexistsinuserwallet) {
        Userwallet userwallet2 = userwalletrepo.findByUserid(orginalcarduser).orElseThrow(() -> new UserWalletNotFoundException("user wallet not found"));
        BigDecimal originalbaance = userwallet2.getTotalwalletbalance();
        BigDecimal newalbaance = originalbaance.add(originalcardfeeamount);
        userwallet2.setTotalwalletbalance(newalbaance);
//                        userwallet2.setUserid(user.getUID());
        Userwallet userwallet1 = userwalletrepo.save(userwallet2);
        userwallet.setTotalwalletbalance(userwallet.getTotalwalletbalance().subtract(usercardfee.getFeeamount()));
        userwalletrepo.save(userwallet);
        //                now update the deposits table
        Userdeposit userdeposit = new Userdeposit();
        userdeposit.setUserwallet(userwallet1);
        userdeposit.setUserid(user.getUID());
        userdeposit.setAmountadded(originalcardfeeamount);
        userdeposit.setOrderid(buyCardRequest.getOrderid());
        userdeposit.setPaymentid(buyCardRequest.getPaymentid());
        userdepositrepo.save(userdeposit);
//                now update user wallet changes table
        Changingwalletbalance changingwalletbalance = new Changingwalletbalance();
        changingwalletbalance.setPreviousbalance(originalbaance);
        changingwalletbalance.setNewbalance(userwallet1.getTotalwalletbalance());
        changingwalletbalance.setAction(BOUGHT_CARD);
        changingwalletbalance.setUserid(user.getUID());
        changingwalletbalance.setUserwallet(userwallet1);
        changingwalletbalancerepo.save(changingwalletbalance);


        //                now update user wallet changes table
        Changingwalletbalance changingwalletbalance11 = new Changingwalletbalance();
        changingwalletbalance11.setPreviousbalance(originalbaance);
        changingwalletbalance11.setNewbalance(newalbaance);
        changingwalletbalance11.setAction(CARD_SOLD);
        changingwalletbalance11.setUserid(orginalcarduser);
        changingwalletbalance11.setUserwallet(userwallet2);
        changingwalletbalancerepo.save(changingwalletbalance11);

        message.set("You have successfully bought the card for " + originalcardfeeamount);
        } else {
        message.set("The user you are buying from does not exist");
        }
        }else{
        message.set("The card fee is inactive or already sold");
        }




        }else{
        message.set("insufficient wallet");

        }
        });

        return new  Buycardresponse(message.get());

        }catch (Exception e){
        return new Buycardresponse("An exception has occurred whilebuying card "+e.getMessage());
        }
*/


/**
 try {
 //            check if the logged in user exists
 User user= userrepo.findById(buyCardRequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));
 //            check if a card they are trying to buy exists
 UserCard userCard = userCardRepo.findById(Long.parseLong(buyCardRequest.getCardid())).orElseThrow(() -> new UserCardNotFoundException("The card could not be found"));
 System.out.println("user card "+userCard.getId());

 boolean usercardfeeexists=usercardfeerepo.existsByUserAndUserCard(user,userCard);


 //            checking whether the user card has a set fee
 Usercardfee usercardfee = usercardfeerepo.findByUserCard(userCard).orElseThrow(() -> new CardNotFoundException("The card fee has not been set for the given user card "+userCard.getId()));
 //            check if the user wallet permits buying this card
 Userwallet userwallet=userwalletrepo.findByUserid(user.getUID()).orElseThrow(() -> new UserWalletNotFoundException("user wallet not found"));
 boolean usercardexistsbyusserandcard=usercardfeerepo.existsByUserAndUserCard(user,userCard);
 BigDecimal loggedinuserwalletbalance=userwallet.getTotalwalletbalance();
 BigDecimal originalcardfeeamount=usercardfee.getFeeamount();
 if (userwallet.getTotalwalletbalance().compareTo(usercardfee.getFeeamount()) > 0){
 if (!userCard.isOpenedcard()){
 return new Buycardresponse("the card is not opened");
 }
 if (usercardexistsbyusserandcard){
 return new Buycardresponse("you cannot buy same card twice");
 }
 if (usercardfee.getStatus().equalsIgnoreCase(ACTIVE)) {
 System.out.println("reaching here ");
 Long orginalcarduser = userCard.getUser().getUID();
 userCard.setUser(user);
 Long newuserid = userCardRepo.save(userCard).getUser().getUID();
 //            after changing th user update the Carduserchange table
 Carduserchangetracking carduserchangetracking = new Carduserchangetracking();
 carduserchangetracking.setAmountbought(usercardfee.getFeeamount());
 carduserchangetracking.setOriginaluser(orginalcarduser);
 carduserchangetracking.setNewuser(newuserid);
 carduserchangetracking.setCreatedat(Instant.now());
 carduserchangetrackingrepo.save(carduserchangetracking);
 usercardfee.setStatus(CARD_SOLD);
 //                    usercardfee.setUser(user);
 usercardfeerepo.save(usercardfee);

 //            now add the money to the original user wallet
 boolean userexistsinuserwallet = userwalletrepo.existsByUserid(orginalcarduser);
 if (userexistsinuserwallet) {
 Userwallet userwallet2 = userwalletrepo.findByUserid(orginalcarduser).orElseThrow(() -> new UserWalletNotFoundException("user wallet not found"));
 BigDecimal originalbaance = userwallet2.getTotalwalletbalance();
 userwallet2.setTotalwalletbalance(originalbaance.add(originalcardfeeamount));
 //                        userwallet2.setUserid(user.getUID());
 Userwallet userwallet1 = userwalletrepo.save(userwallet2);
 userwallet.setTotalwalletbalance(userwallet.getTotalwalletbalance().subtract(usercardfee.getFeeamount()));
 userwalletrepo.save(userwallet);
 //                now update the deposits table
 Userdeposit userdeposit = new Userdeposit();
 userdeposit.setUserwallet(userwallet1);
 userdeposit.setUserid(user.getUID());
 userdeposit.setAmountadded(originalcardfeeamount);
 userdeposit.setOrderid(buyCardRequest.getOrderid());
 userdeposit.setPaymentid(buyCardRequest.getPaymentid());
 userdepositrepo.save(userdeposit);
 //                now update user wallet changes table
 Changingwalletbalance changingwalletbalance = new Changingwalletbalance();
 changingwalletbalance.setPreviousbalance(originalbaance);
 changingwalletbalance.setNewbalance(userwallet1.getTotalwalletbalance());
 changingwalletbalance.setAction(BOUGHT_CARD);
 changingwalletbalance.setUserid(user.getUID());
 changingwalletbalance.setUserwallet(userwallet1);
 changingwalletbalancerepo.save(changingwalletbalance);

 return new Buycardresponse("You have successfully bought the card for " + originalcardfeeamount);
 } else {
 return new Buycardresponse("The user you are buying from does not exist");
 }
 }else{
 return new Buycardresponse("The card fee is inactive or already sold");
 }






 }else{
 return new Buycardresponse("Your wallet balance is "+userwallet.getTotalwalletbalance()+"; The amount is not enough to buy the card worth "+usercardfee.getFeeamount());
 }


 }catch (Exception e){
 return new Buycardresponse("An exception has occurred while buying the card "+e.getMessage());
 }*/

      /**  }
*/