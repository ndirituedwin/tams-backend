package com.cardgame.Service;


import com.cardgame.Dto.requests.*;
import com.cardgame.Dto.responses.*;
import com.cardgame.Dto.responses.Page.PagedResponse;
import com.cardgame.Entity.*;
import com.cardgame.Entity.Carduserchangetracking;
import com.cardgame.Exceptions.*;
import com.cardgame.Repo.*;
import com.cardgame.Service.mapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.cardgame.config.ApiUtils.*;

@Service
@Transactional
@Slf4j
public class CardService {



    private final Cardrepo cardrepo;
    private final Cardduplicaterepo cardduplicaterepo;
    private final Userrepo userrepo;
    private final UserCardRepo userCardRepo;
    private final Userbestcardrepo userbestcardrepo;
    private final Packrepo packrepo;
    private final Unopenedpackrepo unopenedpackrepo;
    private final Userwalletrepo userwalletrepo;
    private final Userdepositrepo userdepositrepo;
    private final Changingwalletbalancerepo changingwalletbalancerepo;
    private final Carduserchangetrackingrepo carduserchangetrackingrepo;
    private final Usercardfeerepo usercardfeerepo;

    private final Boughtpacktrackingrepo boughtpacktrackingrepo;
    private final Packfeelistingrepo packfeelistingrepo;
    private final Gameroomtablerepo gameroomtablerepo;
    private final  Gameroommasterrepo gameroommasterrepo;

    private final CardMarketplacelogrepo cardMarketplacelogrepo;
    private final PackdMarketplacelogrepo packdMarketplacelogrepo;

    public CardService(Cardrepo cardrepo, Cardduplicaterepo cardduplicaterepo, Userrepo userrepo, UserCardRepo userCardRepo, Userbestcardrepo userbestcardrepo, Packrepo packrepo, Unopenedpackrepo unopenedpackrepo, Userwalletrepo userwalletrepo, Userdepositrepo userdepositrepo, Changingwalletbalancerepo changingwalletbalancerepo, Carduserchangetrackingrepo carduserchangetrackingrepo, Usercardfeerepo usercardfeerepo, Boughtpacktrackingrepo boughtpacktrackingrepo, Packfeelistingrepo packfeelistingrepo, Gameroomtablerepo gameroomtablerepo, Gameroommasterrepo gameroommasterrepo, CardMarketplacelogrepo cardMarketplacelogrepo, PackdMarketplacelogrepo packdMarketplacelogrepo) {
        this.cardrepo = cardrepo;
        this.cardduplicaterepo = cardduplicaterepo;
        this.userrepo = userrepo;
        this.userCardRepo = userCardRepo;
        this.userbestcardrepo = userbestcardrepo;
        this.packrepo = packrepo;
        this.unopenedpackrepo = unopenedpackrepo;
        this.userwalletrepo = userwalletrepo;
        this.userdepositrepo = userdepositrepo;
        this.changingwalletbalancerepo = changingwalletbalancerepo;
        this.carduserchangetrackingrepo = carduserchangetrackingrepo;
        this.usercardfeerepo = usercardfeerepo;
        this.boughtpacktrackingrepo = boughtpacktrackingrepo;
        this.packfeelistingrepo = packfeelistingrepo;
        this.gameroomtablerepo = gameroomtablerepo;
        this.gameroommasterrepo = gameroommasterrepo;

        this.cardMarketplacelogrepo = cardMarketplacelogrepo;
        this.packdMarketplacelogrepo = packdMarketplacelogrepo;
    }

    public Object testcontroller(){
        User user= userrepo.findById(1L).orElseThrow(() -> new UserNotFoundException("user not found"));
        return user;
    }
    public Cardduplicateresponse duplicatecard(Cardduplicaterequest cardduplicaterequest) {


       long cardid=Long.parseLong(cardduplicaterequest.getCardid());
       long numberofreplications=Long.parseLong(cardduplicaterequest.getNumberofreplications());
        boolean cardexists=cardrepo.existsById(cardid);
        if (!cardexists){
            return new Cardduplicateresponse("Card with the given id "+cardduplicaterequest.getCardid()+" not found !");
//            return new Cardduplicateresponse("Card with the given id "+cardduplicaterequest.getCardid()+" not found !");
        }
        Card card=cardrepo.findById(cardid).orElseThrow(() -> new CardNotFoundException("card with the given id could not be found "+cardduplicaterequest.getCardid()));
       try {

           long i=1;
           while (i<=numberofreplications){
               cardduplicaterepo.save(new Cardduplicate(card));
               i++;
           }

           return new Cardduplicateresponse("card duplicated "+cardduplicaterequest.getNumberofreplications()+" times");
       }catch (Exception e){
           return new Cardduplicateresponse("An exception has occurred while duplicating card "+e.getMessage());
       }

    }

    public Cardduplicateresponse duplicatecardbyratio(Cardbyratioduplicaterequest cardbyratioduplicaterequest) {
        long cardid=Long.parseLong(cardbyratioduplicaterequest.getCardid());
        String numberorusers=cardbyratioduplicaterequest.getRatio().split(":")[0];
        String replications=cardbyratioduplicaterequest.getRatio().split(":")[1];
        long numberofusers=Long.parseLong(numberorusers);
        long replicationtologng=Long.parseLong(replications);
          long countusers= userrepo.count();
        boolean cardexists=cardrepo.existsById(cardid);
        if (!cardexists){
            return new Cardduplicateresponse("Card with the given id "+cardbyratioduplicaterequest.getCardid()+" not found !");
        }
        Card card=cardrepo.findById(cardid).orElseThrow(() -> new CardNotFoundException("card with the given id could not be found "+cardbyratioduplicaterequest.getCardid()));
        try {
            int counter =1;
            for (int i=1;i<=countusers;i++){
                System.out.println("i "+i);
                if (counter==numberofusers){
                    for (int s=1;s<=replicationtologng;s++){
                        cardduplicaterepo.save(new Cardduplicate(card));
                    }
                    counter=1;
                    }else{
                    counter++;
                }

            }
            return new Cardduplicateresponse("card duplicated by user ratio "+replications+" times");

        }catch (Exception e){
            return new Cardduplicateresponse("An exception "+e.getMessage());
        }



    }

//    @Transactional
    public SelectPackRequestResponse usercards(Selectpackrequest selectpackrequest) {



//        boolean existsbyid=unopenedpackrepo.existsById(Long.parseLong(selectpackrequest.getPackid()));
//        if (existsbyid){



            try {
//                Unopenedpack unopenedpack1=unopenedpackrepo.findById(Long.parseLong(selectpackrequest.getPackid())).orElseThrow(() -> new PackNotFoundException("purchased pack with the provided id could not be found"));

                Pack pack1=packrepo.findById(Long.parseLong(selectpackrequest.getPackid())).orElseThrow(() -> new PackNotFoundException("pack with the provided id could not be found "+selectpackrequest.getPackid()));
                int pack=(int)pack1.getNumberofcards();
                List<Cardduplicate> cardduplicates=cardduplicaterepo.findAllByIsTakenEquals(false);
//                log.info("the cards {} {}",cardduplicates.size());
                Collections.shuffle(cardduplicates);
                User user= userrepo.findById(selectpackrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user with the given id could not be found "));
                if (cardduplicates.isEmpty()){
                    return new SelectPackRequestResponse("you need to first duplicate the cards");
                }

                Unopenedpack unopenedpack=new Unopenedpack();
                unopenedpack.setUser(user);
                unopenedpack.setPack(pack1);
                unopenedpack.setIsopen(selectpackrequest.getOpenpack()==1);
                Unopenedpack unopenedpack1=unopenedpackrepo.save(unopenedpack);
                for(int j=0; j<pack; j++) {
//                    if (cardduplicates.size()==1){
//                        break;
//                    }
                userCardRepo.save(new UserCard(user, cardduplicates.get(j), unopenedpack1, selectpackrequest.getOpenpack() == 1));
                Cardduplicate cardduplicate = new Cardduplicate();
                cardduplicate.setId(cardduplicates.get(j).getId());
                cardduplicate.setCard(cardduplicates.get(j).getCard());
                cardduplicate.setTaken(true);
//                cardduplicates.remove(cardduplicates.get(j));
                cardduplicaterepo.save(cardduplicate);


            }
            return new SelectPackRequestResponse("user cards saved for pack "+selectpackrequest.getPackid());

        }catch (Exception e){
            return new SelectPackRequestResponse("an exception has occurred while trying to select pack "+e.getMessage());
        }
//        }else{
//            return new SelectPackRequestResponse("pack id not  found");
//        }

    }

    public UserbestCardResponse userbest30cards(Userbestcardrequest userbestcardrequest) {
       String thebest30cards=userbestcardrequest.getUserbestcards();
       String[] split30bestcards=thebest30cards.split(",");
       System.out.println("the best user 30 cards "+split30bestcards);
       List<Long> converttolist=new ArrayList<>(0);
        Arrays.stream(split30bestcards).forEach(s1 -> converttolist.add(Long.parseLong(s1)));
        System.out.println("split30bestcards "+converttolist);

        try {
            User user = userrepo.findById(userbestcardrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user with given id could not be found "));
            List<UserCard> userCardList=userCardRepo.findAllByUser(user);
            System.out.println("size of user card list "+userCardList.size());
            List<Long> usercardids=new ArrayList<>(0);
            userCardList.forEach(userCard -> usercardids.add(userCard.getId()));
            System.out.println("usercardid "+usercardids);

            AtomicInteger counter = new AtomicInteger();
           converttolist.stream().forEach(aLong -> {
               if (usercardids.contains(aLong)){
                   counter.getAndIncrement();
                   Userbestcard userbestcard=new Userbestcard();
                   userbestcard.setUser(user);
                   userbestcard.setUserbestcard(aLong);
                   userbestcardrepo.save(userbestcard);
               }
           });
           return new  UserbestCardResponse("user best cards of size "+counter+" saved ");
       }catch (Exception e){
           return new  UserbestCardResponse("exception has occurred while fetching user best thirty cards "+e.getMessage());

       }

    }





    public UnopenedPackRequestResponse unopenedpacks(Unopenedpackrequest selectpackrequest) {
        try {
            Unopenedpack unopenedpack=unopenedpackrepo.findById(Long.parseLong(selectpackrequest.getPackid())).orElseThrow(() -> new PackNotFoundException("purchased pack wit the given  id could not be found "+selectpackrequest.getPackid()));

            User user= userrepo.findById(selectpackrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user with the given id could not be found "));
             if (unopenedpack.getUser().getUID() !=user.getUID()){
                 return new UnopenedPackRequestResponse("you can only updated your purchased packs");
             }
            if (selectpackrequest.getOpenpack()==1){
                unopenedpack.setUser(user);
//                unopenedpack.setPack(pack1);
                unopenedpack.setIsopen(true);
                unopenedpackrepo.save(unopenedpack);
                List<UserCard> userCardList=userCardRepo.findAllByUnopenedpackAndUser(unopenedpack,user);
                userCardList.forEach(userCard -> {
                    boolean exiss=userCardRepo.existsById(userCard.getId());
                    if (exiss){
                        if (userCard.getUser().getUID()==user.getUID()){

                            UserCard userCard1=userCardRepo.findById(userCard.getId()).orElseThrow(() -> new UserCardNotFoundException("user card with id "+userCard.getId()+" could not be found"));
                            userCard1.setOpenedcard(true);
                            userCardRepo.save(userCard1);
                        }
                    }else{

                    }


                });
                return new UnopenedPackRequestResponse("pack opened "+selectpackrequest.getPackid());
                   }else{
                return new UnopenedPackRequestResponse("invalid open pack value provided");
            }

        }catch (Exception e){
            return new UnopenedPackRequestResponse("an exception has occurred while trying to select pack "+e.getMessage());
        }

    }



    public PagedResponse<UserCardResponse> findallcardsbyuser(int page, int size,FindAllCardsByUserrequest findAllCardsByUserrequest) {
        validatePagenumberandSize(page, size);


        try {
            User user= userrepo.findById(findAllCardsByUserrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
//            Page<UserCard> usercards = userCardRepo.findAllByUserrr(user,pageable);
            Page<UserCard> usercards = userCardRepo.findAllByUserAndOpenedcardEquals(user,true,pageable);
//            log.info("usercards {}",usercards);


            if (usercards.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), usercards.getNumber(), usercards.getSize(), usercards.getTotalElements(), usercards.getTotalPages(), usercards.isLast());
            }
            List<UserCardResponse> usercardsresponses = usercards.map(ModelMapper::mapusercardstousercardsresponse).getContent();
//            log.info("logging usercardsresponses {}", usercardsresponses);
            return new PagedResponse<>(usercardsresponses, usercards.getNumber(), usercards.getSize(), usercards.getTotalElements(), usercards.getTotalPages(), usercards.isLast());

        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching cards "+e.getMessage());
//            return new ResponseEntity<>("an exception has occured while fetching user cards"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Cardfeerequestresponse setcardfee(CardfeeRequest cardfeeRequest) {

        if (cardfeeRequest.getCardfee().compareTo(BigDecimal.ZERO)<1){
            return new Cardfeerequestresponse("You have provided invalid card fee "+cardfeeRequest.getCardfee());
        }
        if (cardfeeRequest.getCardid()==null || cardfeeRequest.getCardid().equals(0L)){
            return new Cardfeerequestresponse("invalid card id provided "+cardfeeRequest.getCardid());
        }

        try {
            User user= userrepo.findById(cardfeeRequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found "));
            UserCard userCard=userCardRepo.findById(cardfeeRequest.getCardid()).orElseThrow(() -> new UserCardNotFoundException("user card not found"));
          if (user.getUID() !=userCard.getUser().getUID()){
              return new Cardfeerequestresponse("You can only sell the cards that you own.You do not own card  "+userCard.getId());
          }

          /*new code*/

            if (userCard.isOpenedcard()){
                boolean existsbystatusandcardid=usercardfeerepo.existsByUserCardAndStatus(userCard,ACTIVE);
                if (existsbystatusandcardid){
                    return new Cardfeerequestresponse("user card fee with given status and card id exists ");
                }
//                List<Usercardfee> usercardfees =usercardfeerepo.findAllByUserCard(userCard);
//                if (!usercardfees.isEmpty()){
//
//                }
                Usercardfee usercardfee=new Usercardfee();
                usercardfee.setUserCard(userCard);
                usercardfee.setUser(user);
                usercardfee.setFeeamount(cardfeeRequest.getCardfee());
                usercardfee.setCreatedat(Instant.now());
                usercardfee.setStatus(ACTIVE);
                Usercardfee savedusercardfee=usercardfeerepo.save(usercardfee);

                CardMarketplacelog cardMarketplacelog =new CardMarketplacelog();
                cardMarketplacelog.setAction(CARDSTATUSCHANGE);
                cardMarketplacelog.setCreatedDate(Instant.now());
                cardMarketplacelog.setUid(user.getUID());
                cardMarketplacelog.setCardid(usercardfee.getId());
                cardMarketplacelog.setPreviousstatus(NULL);
                cardMarketplacelog.setNewstatus(ACTIVE);
                cardMarketplacelogrepo.save(cardMarketplacelog);

                return new Cardfeerequestresponse("New  card "+savedusercardfee.getId()+" with fee of "+savedusercardfee.getFeeamount()+" has been saved succesfully");


            }else {
                return new Cardfeerequestresponse("You can only sell opened cards.The card with the given id: " + userCard.getId() + " is not opened yet");

            }
            /**end of new code*/


         /*  if (userCard.isOpenedcard()){

               boolean usercardfeeexists=usercardfeerepo.existsByUserCard(userCard);
               if (usercardfeeexists){
                   Usercardfee usercardfee=usercardfeerepo.findByUserCard(userCard).orElseThrow(() -> new UserCardNotFoundException("card fee not found"));
                    if (usercardfee.getStatus().equals(ACTIVE)){
                        return new Cardfeerequestresponse("The card is already active and you cannot sell it again");
                    }else if (usercardfee.getStatus().equals(CARD_SOLD)){

                        Usercardfee usercardfeeu=new Usercardfee();
                        usercardfeeu.setUserCard(userCard);
                        usercardfeeu.setUser(user);
                        usercardfeeu.setFeeamount(cardfeeRequest.getCardfee());
                        usercardfeeu.setCreatedat(Instant.now());
                        usercardfeeu.setStatus(ACTIVE);
                        Usercardfee savedusercardfee=usercardfeerepo.save(usercardfeeu);
                        return new Cardfeerequestresponse("The card "+savedusercardfee.getId()+" with fee of "+savedusercardfee.getFeeamount()+" has been set to market place again");

                    }else{

                    }
               }


               Usercardfee usercardfee=new Usercardfee();
               usercardfee.setUserCard(userCard);
               usercardfee.setUser(user);
               usercardfee.setFeeamount(cardfeeRequest.getCardfee());
               usercardfee.setCreatedat(Instant.now());
               usercardfee.setStatus(ACTIVE);
               Usercardfee savedusercardfee=usercardfeerepo.save(usercardfee);
               return new Cardfeerequestresponse("New  card "+savedusercardfee.getId()+" with fee of "+savedusercardfee.getFeeamount()+" has been saved succesfully");

           }else {
               return new Cardfeerequestresponse("You can only sell opened cards.The card with the given id: "+userCard.getId()+" is not opened yet");
           }*/

        }catch (Exception e){
            return new Cardfeerequestresponse("An exception has occurred while trying to set card fee "+e.getMessage());
        }

    }
    public Packfeerequestresponse setpackfee(PackfeeRequest packfeeRequest) {

        if (packfeeRequest.getPackfee().compareTo(BigDecimal.ZERO)<1){
            return new Packfeerequestresponse("You have provided invalid pack fee "+packfeeRequest.getPackfee());
        }
        if (packfeeRequest.getPackid()==null || packfeeRequest.getPackid().equals(0L)){
            return new Packfeerequestresponse("invalid pack id provided "+packfeeRequest.getPackid());
        }

        try {
            User user= userrepo.findById(packfeeRequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found "));
//           Pack pack=packrepo.findById(packfeeRequest.getPackid()).orElseThrow(() ->  new PackNotFoundException("pack not found"));

            Unopenedpack unopenedpack=unopenedpackrepo.findById(packfeeRequest.getPackid()).orElseThrow(() ->  new PackNotFoundException("pack not found"));
            if (unopenedpack.getUser().getUID() !=packfeeRequest.getUid()){
                return new Packfeerequestresponse("You can only sell packs that you own");
            }
            if (unopenedpack.isIsopen()){

                boolean existsby=packfeelistingrepo.existsByUnopenedpackAndStatus(unopenedpack,ACTIVE);
                if (existsby){
                        return new Packfeerequestresponse("The pack is already active and you cannot sell it again");
                }
//                boolean usercardfeeexists=packfeelistingrepo.existsByUnopenedpack(unopenedpack);
//                if (usercardfeeexists){
//                    PackPricelisting usercardfee=packfeelistingrepo.findByUnopenedpack(unopenedpack).orElseThrow(() -> new UserCardNotFoundException("card fee not found"));
//                    if (usercardfee.getStatus().equals(ACTIVE)){
//                        return new Packfeerequestresponse("The pack is already active and you cannot sell it again");
//                    }
//                }
                PackPricelisting packPricelisting=new PackPricelisting();
                packPricelisting.setCreatedat(Instant.now());
                packPricelisting.setUser(user);
                packPricelisting.setFeeamount(packfeeRequest.getPackfee());
                packPricelisting.setStatus(ACTIVE);
                packPricelisting.setUnopenedpack(unopenedpack);

                PackPricelisting savedusercardfee=packfeelistingrepo.save(packPricelisting);

                PackMarketplacelog packMarketplacelog=new PackMarketplacelog();
                packMarketplacelog.setPackid(savedusercardfee.getId());
                packMarketplacelog.setCreatedDate(Instant.now());
                packMarketplacelog.setPreviousstatus(NULL);
                packMarketplacelog.setUid(user.getUID());
                packMarketplacelog.setNewstatus(ACTIVE);
                packMarketplacelog.setAction(PACKSTATUSCHANGE);
                packdMarketplacelogrepo.save(packMarketplacelog);

                return new Packfeerequestresponse("The pack "+savedusercardfee.getUnopenedpack().getId()+" with pack fee of "+savedusercardfee.getFeeamount()+" has been saved succesfully");

            }else{
                return new Packfeerequestresponse("YOu cannot sell packs that are not opened");
            }


        }catch (Exception e){
            return new Packfeerequestresponse("An exception has occurred while trying to set card fee "+e.getMessage());
        }

    }

    public PagedResponse<CardFeeResponse> cardfees(int page, int size) {
        validatePagenumberandSize(page, size);


        try {

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            Page<Usercardfee> usercards = usercardfeerepo.findAll(pageable);


            if (usercards.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), usercards.getNumber(), usercards.getSize(), usercards.getTotalElements(), usercards.getTotalPages(), usercards.isLast());
            }
            List<CardFeeResponse> usercardsresponses = usercards.map(ModelMapper::mapcardfeetocardfeeresponse).getContent();
////            log.info("logging usercardsresponses {}", usercardsresponses);
            return new PagedResponse<>(usercardsresponses, usercards.getNumber(), usercards.getSize(), usercards.getTotalElements(), usercards.getTotalPages(), usercards.isLast());

        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching cards "+e.getMessage());
//            return new ResponseEntity<>("an exception has occured while fetching user cards"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public PagedResponse<PackFeeResponse> packfees(int page, int size) {
        validatePagenumberandSize(page, size);


        try {

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            Page<PackPricelisting> usercards = packfeelistingrepo.findAll(pageable);


            if (usercards.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), usercards.getNumber(), usercards.getSize(), usercards.getTotalElements(), usercards.getTotalPages(), usercards.isLast());
            }
            List<PackFeeResponse> usercardsresponses = usercards.map(ModelMapper::mapPackfeetopackfeeresponse).getContent();
//            log.info("logging usercardsresponses {}", usercardsresponses);
            return new PagedResponse<>(usercardsresponses, usercards.getNumber(), usercards.getSize(), usercards.getTotalElements(), usercards.getTotalPages(), usercards.isLast());

        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching cards "+e.getMessage());
//            return new ResponseEntity<>("an exception has occured while fetching user cards"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Cardfeerequestresponse updatecardfee(CardStatusupdate cardStatusupdate){

        if (cardStatusupdate.getCardid()==null){
            return new Cardfeerequestresponse("Invalid card id");
        }
        try {
            User user= userrepo.findById(cardStatusupdate.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));

            Usercardfee usercardfee1=usercardfeerepo.findById(cardStatusupdate.getCardid()).orElseThrow(() -> new CardNotFoundException("card not found "));
           if (usercardfee1.getUser().getUID() !=user.getUID()){
               return new Cardfeerequestresponse("You can only update your own cards");
           }
           String prevstatus=usercardfee1.getStatus();
            usercardfee1.setStatus(cardStatusupdate.getStatus());
            usercardfeerepo.save(usercardfee1);
            CardMarketplacelog cardMarketplacelog =new CardMarketplacelog();
            cardMarketplacelog.setAction(CARDSTATUSCHANGE);
            cardMarketplacelog.setCreatedDate(Instant.now());
            cardMarketplacelog.setUid(user.getUID());
            cardMarketplacelog.setCardid(usercardfee1.getId());
            cardMarketplacelog.setPreviousstatus(prevstatus);
            cardMarketplacelog.setNewstatus(cardStatusupdate.getStatus());
            cardMarketplacelogrepo.save(cardMarketplacelog);

            return new Cardfeerequestresponse("card fee status update!");

        }catch (Exception e){
            return new Cardfeerequestresponse("an exception has occured while updating card fee "+e.getMessage());
        }

    }
    public Cardfeerequestresponse updatepackfee(Packstatusupdate cardStatusupdate){

        if (cardStatusupdate.getPackid()==null){
            return new Cardfeerequestresponse("Invalid pack id "+cardStatusupdate.getPackid());
        }
        try {
            User user= userrepo.findById(cardStatusupdate.getUid()).orElseThrow(() -> new UserNotFoundException("user not found "));
            PackPricelisting usercardfee1=packfeelistingrepo.findById(cardStatusupdate.getPackid()).orElseThrow(() -> new CardNotFoundException("card not found "));
            if (usercardfee1.getUser().getUID() !=user.getUID()){
                return new Cardfeerequestresponse("YOu can only update your pack fees");
            }
            String pre=usercardfee1.getStatus();
            usercardfee1.setStatus(cardStatusupdate.getStatus());
            packfeelistingrepo.save(usercardfee1);

            PackMarketplacelog packMarketplacelog=new PackMarketplacelog();
            packMarketplacelog.setPackid(usercardfee1.getId());
            packMarketplacelog.setCreatedDate(Instant.now());
            packMarketplacelog.setPreviousstatus(pre);
            packMarketplacelog.setUid(user.getUID());
            packMarketplacelog.setNewstatus(cardStatusupdate.getStatus());
            packMarketplacelog.setAction(PACKSTATUSCHANGE);
            packdMarketplacelogrepo.save(packMarketplacelog);
            return new Cardfeerequestresponse("pack fee status updated!");

        }catch (Exception e){
            return new Cardfeerequestresponse("an exception has occured while updating card fee "+e.getMessage());
        }

    }

    public ApiResponse validatePagenumberandSize(int page, int size) {

        try {
            if (page < 0) {
                return new ApiResponse(false,"page number may not be less than zero",HttpStatus.BAD_REQUEST);

            }
            if (size > MAX_PAGE_SIZE) {
                return new ApiResponse(false,"page size may not be greater than " + MAX_PAGE_SIZE,HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ApiResponse(false,"an exception has occurred while validating page number and size " + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public BoughtpackUpdateStatusResposponse updateboughtpackstatus(BoughtpackStatusRequest boughtpackStatusRequest) {

           if (boughtpackStatusRequest.getCardid()==null){
               return new BoughtpackUpdateStatusResposponse("Card id may not be null");
           }
           PackStatus packStatus =null;
           if (boughtpackStatusRequest.getCardStatus().equalsIgnoreCase(String.valueOf(PackStatus.SOLD))){
               packStatus = PackStatus.SOLD;
           } else if (boughtpackStatusRequest.getCardStatus().equalsIgnoreCase(String.valueOf(PackStatus.ACTIVE))) {
               packStatus = PackStatus.ACTIVE;
           } else if (boughtpackStatusRequest.getCardStatus().equalsIgnoreCase(String.valueOf(PackStatus.DEACTIVE))) {
               packStatus = PackStatus.DEACTIVE;

           }else return new BoughtpackUpdateStatusResposponse("Invelid value entered");


        try {
            Boughtpacktracking boughtpacktracking=boughtpacktrackingrepo.findById(boughtpackStatusRequest.getCardid()).orElseThrow(() -> new CardNotFoundException("card not found "));
            boughtpacktracking.setPackStatus(packStatus);
               boughtpacktrackingrepo.save(boughtpacktracking);
               return new BoughtpackUpdateStatusResposponse("Card status has been successfully updated");


           }catch (Exception e){
               return new BoughtpackUpdateStatusResposponse("An exception has occurred while updating card status "+e.getMessage());

           }
    }


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

    }

    public BuyPackresponse buypack(BuyPackRequest buyPackRequest) {

        if (buyPackRequest.getPackid()==null|| buyPackRequest.getPackid()==0L || buyPackRequest.getUid()==null){
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

            }
                           return new BuyPackresponse(response.get());
//               return new BuyPackresponse("response.get()");

        }catch (Exception e){
            return new BuyPackresponse("An exception has occurred while trying to buy pack "+e.getMessage());
        }


    }

    public PagedResponse<GameRoomUsersResponse> gameroomusers(int page, int size, GameRoomUsersRequest gameRoomUsersRequest) {


        validatePagenumberandSize(page, size);
        if (gameRoomUsersRequest.getGameroommasterid()==null|| gameRoomUsersRequest.getGameroommasterid()==0){
            throw new RuntimeException("invalid request body");
        }


        try {
            Gameroommaster gameRoom=gameroommasterrepo.findById(gameRoomUsersRequest.getGameroommasterid()).orElseThrow(() -> new GameNotFoundException("gameroom not found"));

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

            Page<GameRoomTable> gameroomusers = gameroomtablerepo.findAllByGameroommaster(gameRoom,pageable);
            if (gameroomusers.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), gameroomusers.getNumber(), gameroomusers.getSize(), gameroomusers.getTotalElements(), gameroomusers.getTotalPages(), gameroomusers.isLast());
            }
            List<GameRoomUsersResponse> usercardsresponses = gameroomusers.map(ModelMapper::mapgameroomusestogameroomusersresponse).getContent();
//            log.info("logging usercardsresponses {}", usercardsresponses);
            return new PagedResponse<>(usercardsresponses, gameroomusers.getNumber(), gameroomusers.getSize(), gameroomusers.getTotalElements(), gameroomusers.getTotalPages(), gameroomusers.isLast());





        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching cards "+e.getMessage());
        }
    }
    public PagedResponse<GameRoomUsersResponse> allgameroomusers(int page, int size) {

        validatePagenumberandSize(page, size);
                try {

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

            Page<GameRoomTable> gameroomusers = gameroomtablerepo.findAll(pageable);
            if (gameroomusers.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), gameroomusers.getNumber(), gameroomusers.getSize(), gameroomusers.getTotalElements(), gameroomusers.getTotalPages(), gameroomusers.isLast());
            }
            List<GameRoomUsersResponse> usercardsresponses = gameroomusers.map(ModelMapper::mapgameroomusestogameroomusersresponse).getContent();
//            log.info("logging usercardsresponses {}", usercardsresponses);
            return new PagedResponse<>(usercardsresponses, gameroomusers.getNumber(), gameroomusers.getSize(), gameroomusers.getTotalElements(), gameroomusers.getTotalPages(), gameroomusers.isLast());

        }catch (Exception e){
            throw new RuntimeException("An exception has occurred while fetching cards "+e.getMessage());
        }
    }
}
