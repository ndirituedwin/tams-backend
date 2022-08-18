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
    private final Loginrepo loginrepo;
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

    public CardService(Cardrepo cardrepo, Cardduplicaterepo cardduplicaterepo, Loginrepo loginrepo, UserCardRepo userCardRepo, Userbestcardrepo userbestcardrepo, Packrepo packrepo, Unopenedpackrepo unopenedpackrepo, Userwalletrepo userwalletrepo, Userdepositrepo userdepositrepo, Changingwalletbalancerepo changingwalletbalancerepo, Carduserchangetrackingrepo carduserchangetrackingrepo, Usercardfeerepo usercardfeerepo, Boughtpacktrackingrepo boughtpacktrackingrepo, Packfeelistingrepo packfeelistingrepo) {
        this.cardrepo = cardrepo;
        this.cardduplicaterepo = cardduplicaterepo;
        this.loginrepo = loginrepo;
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
          long countusers=loginrepo.count();
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

    public SelectPackRequestResponse usercards(Selectpackrequest selectpackrequest) {

//        int pack=Integer.parseInt(selectpackrequest.getPackid());
        Pack pack1=packrepo.findById(Long.parseLong(selectpackrequest.getPackid())).orElseThrow(() -> new PackNotFoundException("pack with the provided id could not be found "+selectpackrequest.getPackid()));
        int pack=(int)pack1.getNumberofcards();

        List<Cardduplicate> cardduplicates=cardduplicaterepo.findAllByIsTakenEquals(false);
////        log.info("the cards {}",cardduplicates.size());
        User user=loginrepo.findByUID(selectpackrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user with the given id could not be found "));

        Collections.shuffle(cardduplicates);

        try {
            for(int j=0; j<pack; j++){
                userCardRepo.save(new UserCard(user,cardduplicates.get(j),pack1,false));
                Cardduplicate cardduplicate= new Cardduplicate();
                cardduplicate.setId(cardduplicates.get(j).getId());
                cardduplicate.setCard(cardduplicates.get(j).getCard());
                cardduplicate.setTaken(true);
                cardduplicaterepo.save(cardduplicate);
            }
            return new SelectPackRequestResponse("user cards saved for pack "+selectpackrequest.getPackid());

        }catch (Exception e){
            return new SelectPackRequestResponse("an exception has occurred while trying to select pack "+e.getMessage());
        }

    }

    public UserbestCardResponse userbest30cards(Userbestcardrequest userbestcardrequest) {
       String thebest30cards=userbestcardrequest.getUserbestcards();
       String[] split30bestcards=thebest30cards.split(",");
       List<Long> converttolist=new ArrayList<>(0);
        Arrays.stream(split30bestcards).forEach(s1 -> converttolist.add(Long.parseLong(s1)));
       User user =loginrepo.findByUID(userbestcardrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user with given id could not be found "));
        List<UserCard> userCardList=userCardRepo.findAllByUser(user);
        List<Long> usercardids=new ArrayList<>(0);
        userCardList.forEach(userCard -> usercardids.add(userCard.getId()));
//        log.info("the ids {}",usercardids);
//        log.info("the converttolist {}",converttolist);
       try {
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
           return new  UserbestCardResponse("exception has occurred while fetching user best thirty cards \"+e.getMessage()");

       }

    }

//    public UnopenedPackRequestResponse unopenedpacks(Unopenedpackrequest unopenedpackrequest) {
//        try {
//            this.usercardsopen(unopenedpackrequest);
//            return new UnopenedPackRequestResponse("your  opened cards have been saved");
//       }catch (Exception e){
//           return new UnopenedPackRequestResponse("An exception has occurred while opening packs "+e.getMessage());
//       }
//    }



    public UnopenedPackRequestResponse unopenedpacks(Unopenedpackrequest selectpackrequest) {

        Pack pack1=packrepo.findById(Long.parseLong(selectpackrequest.getPackid())).orElseThrow(() -> new PackNotFoundException("pack with the provided id could not be found "+selectpackrequest.getPackid()));
        int pack=(int)pack1.getNumberofcards();

        User user=loginrepo.findByUID(selectpackrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user with the given id could not be found "));


        try {

            if (selectpackrequest.getOpenpack()==1){
                Unopenedpack unopenedpack=unopenedpackrepo.findByPack(pack1).orElseThrow(() -> new PackNotFoundException("Unopened pack wit the given pack id could not be found "+pack));
                unopenedpack.setUser(user);
                unopenedpack.setPack(pack1);
                unopenedpack.setIsopen(true);
                unopenedpackrepo.save(unopenedpack);
                List<UserCard> userCardList=userCardRepo.findAllByPackAndUser(pack1,user);
                userCardList.forEach(userCard -> {
                    boolean exiss=userCardRepo.existsById(userCard.getId());
                    if (exiss){
                        UserCard userCard1=userCardRepo.findById(userCard.getId()).orElseThrow(() -> new UserCardNotFoundException("user card with id "+userCard.getId()+" could not be found"));
                      userCard1.setOpenedcard(true);
                       userCardRepo.save(userCard1);
                    }else{

                    }


                });
                return new UnopenedPackRequestResponse("pack opened "+selectpackrequest.getPackid());
                }else if (selectpackrequest.getOpenpack()==0){
                boolean unopenedpackexist=unopenedpackrepo.existsByPack(pack1);
                if (unopenedpackexist){
                    return new UnopenedPackRequestResponse("the pack already exists");
                }
                Unopenedpack unopenedpack=new Unopenedpack();
                 unopenedpack.setIsopen(false);
                 unopenedpack.setPack(pack1);
                 unopenedpack.setUser(user);
                unopenedpackrepo.save(unopenedpack);

                List<Cardduplicate> cardduplicates=cardduplicaterepo.findAllByIsTakenEquals(false);
                Collections.shuffle(cardduplicates);

                for(int j=0; j<pack; j++){
                    userCardRepo.save(new UserCard(user,cardduplicates.get(j),pack1,false));
                    Cardduplicate cardduplicate= new Cardduplicate();
                    cardduplicate.setId(cardduplicates.get(j).getId());
                    cardduplicate.setCard(cardduplicates.get(j).getCard());
                    cardduplicate.setTaken(true);
                    cardduplicaterepo.save(cardduplicate);
                }
                return new UnopenedPackRequestResponse("unopened pack created "+selectpackrequest.getPackid());
            }else{
                return new UnopenedPackRequestResponse("invalid open pack value provided");

            }


        }catch (Exception e){
            return new UnopenedPackRequestResponse("an exception has occurred while trying to select pack "+e.getMessage());
        }

    }


    @Transactional
    public Buycardresponse buycard(BuyCardRequest buyCardRequest) {

        if (buyCardRequest.getAmounttobuy().compareTo(BigDecimal.ZERO)<1){
            return new Buycardresponse("Invalid buy amount entered");
        }
        User user=loginrepo.findByUID(buyCardRequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));
        try {
            UserCard userCard = userCardRepo.findById(Long.parseLong(buyCardRequest.getCardid())).orElseThrow(() -> new UserCardNotFoundException("The card could not be found"));
            Usercardfee usercardfee = usercardfeerepo.findByUserCard(userCard).orElseThrow(() -> new CardNotFoundException("The card fee has not been set for the given user card"));
            if (usercardfee.getStatus().equalsIgnoreCase("ACTIVE")) {

            Long orginalcarduser = userCard.getUser().getId();
            userCard.setUser(user);
            Long newuserid = userCardRepo.save(userCard).getUser().getId();
//            after changing th user update the Carduserchange table
            Carduserchangetracking carduserchangetracking = new Carduserchangetracking();
            carduserchangetracking.setAmountbought(buyCardRequest.getAmounttobuy());
            carduserchangetracking.setOriginaluser(orginalcarduser);
            carduserchangetracking.setNewuser(newuserid);
            carduserchangetracking.setCreatedat(Instant.now());
            carduserchangetrackingrepo.save(carduserchangetracking);
            usercardfee.setStatus(CARD_SOLD);
            usercardfeerepo.save(usercardfee);

//            now add the money to the original user wallet
            boolean userexistsinuserwallet = userwalletrepo.existsByUserid(orginalcarduser);
            if (userexistsinuserwallet) {
                Userwallet userwallet = userwalletrepo.findByUserid(orginalcarduser).orElseThrow(() -> new UserWalletNotFoundException("user wallet not found"));
                BigDecimal originalbaance = userwallet.getTotalwalletbalance();
                userwallet.setTotalwalletbalance(originalbaance.add(buyCardRequest.getAmounttobuy()));
                userwallet.setUserid(user.getId());
                Userwallet userwallet1 = userwalletrepo.save(userwallet);
//                now update the deposits table
                Userdeposit userdeposit = new Userdeposit();
                userdeposit.setUserwallet(userwallet1);
                userdeposit.setUserid(user.getId());
                userdeposit.setAmountadded(buyCardRequest.getAmounttobuy());
                userdeposit.setOrderid(buyCardRequest.getOrderid());
                userdeposit.setPaymentid(buyCardRequest.getPaymentid());
                userdepositrepo.save(userdeposit);
//                now update user wallet changes table
                Changingwalletbalance changingwalletbalance = new Changingwalletbalance();
                changingwalletbalance.setPreviousbalance(originalbaance);
                changingwalletbalance.setNewbalance(userwallet1.getTotalwalletbalance());
                changingwalletbalance.setAction("BOUGHTCARD");
                changingwalletbalance.setUserid(user.getId());
                changingwalletbalance.setUserwallet(userwallet1);
                changingwalletbalancerepo.save(changingwalletbalance);

                return new Buycardresponse("You have successfully bought the card for " + buyCardRequest.getAmounttobuy());
            } else {
                return new Buycardresponse("The user you are buying from does not exist");
            }
        }else{
                return new Buycardresponse("The card fee is inactive or already sold");
            }
        }catch (Exception e){
            return new Buycardresponse("An exception has occurred while buying the card "+e.getMessage());
        }

    }

    public PagedResponse<UserCardResponse> findallcardsbyuser(int page, int size,FindAllCardsByUserrequest findAllCardsByUserrequest) {
        validatePagenumberandSize(page, size);

        User user=loginrepo.findByUID(findAllCardsByUserrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));

        try {

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            Page<UserCard> usercards = userCardRepo.findAllByUserrr(user,pageable);
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
        User user=loginrepo.findByUID(cardfeeRequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found "));
        UserCard userCard=userCardRepo.findById(cardfeeRequest.getCardid()).orElseThrow(() -> new UserCardNotFoundException("user card not found"));

        try {

            Usercardfee usercardfee=new Usercardfee();
            usercardfee.setUserCard(userCard);
            usercardfee.setUser(user);
            usercardfee.setFeeamount(cardfeeRequest.getCardfee());
            usercardfee.setCreatedat(Instant.now());
            usercardfee.setStatus(ACTIVE);
            Usercardfee savedusercardfee=usercardfeerepo.save(usercardfee);
            return new Cardfeerequestresponse("The card "+savedusercardfee.getUserCard().getId()+" with fee of "+savedusercardfee.getFeeamount()+" has been saved succesfully");



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
        User user=loginrepo.findByUID(1L).orElseThrow(() -> new UserNotFoundException("user not found "));

        try {
           Pack pack=packrepo.findById(packfeeRequest.getPackid()).orElseThrow(() ->  new PackNotFoundException("pack not found"));
            PackPricelisting packPricelisting=new PackPricelisting();
             packPricelisting.setCreatedat(Instant.now());
             packPricelisting.setUser(user);
             packPricelisting.setFeeamount(packfeeRequest.getPackfee());
             packPricelisting.setStatus(ACTIVE);
             packPricelisting.setPack(pack);

            PackPricelisting savedusercardfee=packfeelistingrepo.save(packPricelisting);
            return new Packfeerequestresponse("The pack "+savedusercardfee.getPack().getId()+" with pack fee of "+savedusercardfee.getFeeamount()+" has been saved succesfully");



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
            Usercardfee usercardfee1=usercardfeerepo.findById(cardStatusupdate.getCardid()).orElseThrow(() -> new CardNotFoundException("card not found "));
            usercardfee1.setStatus(cardStatusupdate.getStatus());
            usercardfeerepo.save(usercardfee1);
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
            PackPricelisting usercardfee1=packfeelistingrepo.findById(cardStatusupdate.getPackid()).orElseThrow(() -> new CardNotFoundException("card not found "));
            usercardfee1.setStatus(cardStatusupdate.getStatus());
            packfeelistingrepo.save(usercardfee1);
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

    public BuyPackresponse buypack(BuyPackRequest buyPackRequest) {

        if (buyPackRequest.getPackid()==null|| buyPackRequest.getPackid()==0L){
            return new BuyPackresponse("Invalid pack details "+buyPackRequest.getPackid());
        }
        AtomicReference<String> response= new AtomicReference<>("");

        try {
            User user = loginrepo.findByUID(buyPackRequest.getUid()).orElseThrow(() -> new UserNotFoundException("User Not found exception "));
            Pack pack = packrepo.findById(buyPackRequest.getPackid()).orElseThrow(() -> new PackNotFoundException("pack not found"));

            Unopenedpack unopenedpack=unopenedpackrepo.findByPack(pack).orElseThrow(() -> new PackNotFoundException("pack not found"));
            PackPricelisting packPricelisting =packfeelistingrepo.findByPack(unopenedpack).orElseThrow(() -> new PackNotFoundException("pack fee not found") );
            if (!unopenedpack.isIsopen()) {
            List<UserCard> userCard = userCardRepo.findAllByPack(pack);
            userCard.forEach(userCard1 -> {
                Long originaluser = userCard1.getUser().getId();
                userCard1.setUser(user);
                Long newuserid = userCardRepo.save(userCard1).getUser().getId();
//            after changing th user update the Carduserchange table
                Carduserchangetracking carduserchangetracking = new Carduserchangetracking();
                carduserchangetracking.setAmountbought(buyPackRequest.getAmount());
                carduserchangetracking.setOriginaluser(originaluser);
                carduserchangetracking.setNewuser(newuserid);
                carduserchangetracking.setCreatedat(Instant.now());
                carduserchangetrackingrepo.save(carduserchangetracking);

//            now add the money to the original user wallet
                boolean userexistsinuserwallet = userwalletrepo.existsByUserid(originaluser);
                if (userexistsinuserwallet) {
                    Userwallet userwallet = userwalletrepo.findByUserid(originaluser).orElseThrow(() -> new UserWalletNotFoundException("user wallet not found"));
                    BigDecimal originalbaance = userwallet.getTotalwalletbalance();
                    userwallet.setTotalwalletbalance(originalbaance.add(buyPackRequest.getAmount()));
                    Userwallet userwallet1 = userwalletrepo.save(userwallet);
//                now update the deposits table
                    Userdeposit userdeposit = new Userdeposit();
                    userdeposit.setUserwallet(userwallet1);
                    userdeposit.setUserid(user.getId());
                    userdeposit.setAmountadded(buyPackRequest.getAmount());
                    userdeposit.setOrderid(buyPackRequest.getOrderid());
                    userdeposit.setPaymentid(buyPackRequest.getPaymentid());
                    userdepositrepo.save(userdeposit);
//                now update user wallet changes table
                    Changingwalletbalance changingwalletbalance = new Changingwalletbalance();
                    changingwalletbalance.setPreviousbalance(originalbaance);
                    changingwalletbalance.setNewbalance(userwallet1.getTotalwalletbalance());
                    changingwalletbalance.setAction("BOUGHTPACK");
                    changingwalletbalance.setUserid(user.getId());
                    changingwalletbalance.setUserwallet(userwallet1);
                    changingwalletbalancerepo.save(changingwalletbalance);
                    unopenedpack.setIsopen(true);
                    unopenedpackrepo.save(unopenedpack);

                    response.set("You have successfully bought the pack for " + buyPackRequest.getAmount());

//                       return new BuyPackresponse("You have successfully bought the pack for "+buyPackRequest.getAmount());
                } else {
//                       return new BuyPackresponse("The user you are buying from does not exist");
                }


            });
                Boughtpacktracking boughtpacktracking=new Boughtpacktracking();
                boughtpacktracking.setNewuser(user.getId());
                boughtpacktracking.setAmount(buyPackRequest.getAmount());
                boughtpacktracking.setCreatedat(Instant.now());
                boughtpacktracking.setPackStatus(PackStatus.SOLD);
                boughtpacktracking.setUnopenedpack(unopenedpack);
                boughtpacktrackingrepo.save(boughtpacktracking);
                packPricelisting.setStatus(PACK_SOLD);
                packfeelistingrepo.save(packPricelisting);
            }else{
                response.set("NO unopened pack");
            }
               return new BuyPackresponse(response.get());
//               return new BuyPackresponse("response.get()");

        }catch (Exception e){
            return new BuyPackresponse("An exception has occurred while trying to buy pack "+e.getMessage());
        }


    }
}
