package com.cardgame.Service;


import com.cardgame.Dto.requests.Cardbyratioduplicaterequest;
import com.cardgame.Dto.requests.Cardduplicaterequest;
import com.cardgame.Dto.requests.Selectpackrequest;
import com.cardgame.Dto.requests.Userbestcardrequest;
import com.cardgame.Dto.responses.Cardduplicateresponse;
import com.cardgame.Dto.responses.SelectPackRequestResponse;
import com.cardgame.Dto.responses.UserbestCardResponse;
import com.cardgame.Entity.*;
import com.cardgame.Exceptions.CardNotFoundException;
import com.cardgame.Exceptions.UserNotFoundException;
import com.cardgame.Repo.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CardService {



    private final Cardrepo cardrepo;
    private final Cardduplicaterepo cardduplicaterepo;
    private final Loginrepo loginrepo;
    private final UserCardRepo userCardRepo;
    private final Userbestcardrepo userbestcardrepo;
    public Cardduplicateresponse duplicatecard(Cardduplicaterequest cardduplicaterequest) {


       long cardid=Long.parseLong(cardduplicaterequest.getCardid());
       long numberofreplications=Long.parseLong(cardduplicaterequest.getNumberofreplications());
        boolean cardexists=cardrepo.existsById(cardid);
        if (!cardexists){
            return Cardduplicateresponse.builder().message("Card with the given id "+cardduplicaterequest.getCardid()+" not found !").build();
        }
        Card card=cardrepo.findById(cardid).orElseThrow(() -> new CardNotFoundException("card with the given id could not be found "+cardduplicaterequest.getCardid()));
       try {

           long i=1;
           while (i<=numberofreplications){
               cardduplicaterepo.save(new Cardduplicate(card));
               i++;
           }

           return Cardduplicateresponse.builder().message("card duplicated "+cardduplicaterequest.getNumberofreplications()+" times").build();
       }catch (Exception e){
           return Cardduplicateresponse.builder().message("An exception has occurred while duplicating card "+e.getMessage()).build();
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
            return Cardduplicateresponse.builder().message("Card with the given id "+cardbyratioduplicaterequest.getCardid()+" not found !").build();
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
            return Cardduplicateresponse.builder().message("card duplicated by user ratio "+replications+" times").build();

        }catch (Exception e){
            return Cardduplicateresponse.builder().message("An exception "+e.getMessage()).build();
        }



    }

    public SelectPackRequestResponse usercards(Selectpackrequest selectpackrequest) {

        int pack=Integer.parseInt(selectpackrequest.getPack());
        List<Cardduplicate> cardduplicates=cardduplicaterepo.findAllByIsTakenEquals(false);
        log.info("the cards {}",cardduplicates.size());
        long id=1;
        User user=loginrepo.findById(id).orElseThrow(() -> new UserNotFoundException("user with the given id could not be found "));

        Collections.shuffle(cardduplicates);

        try {
            for(int j=0; j<pack; j++){
                    userCardRepo.save(new UserCard(user,cardduplicates.get(j)));
                    Cardduplicate cardduplicate= new Cardduplicate();
                    cardduplicate.setId(cardduplicates.get(j).getId());
                    cardduplicate.setCard(cardduplicates.get(j).getCard());
                    cardduplicate.setTaken(true);
                    cardduplicaterepo.save(cardduplicate);
            }
            return SelectPackRequestResponse.builder().message("user cards saved for pack "+selectpackrequest.getPack()).build();

       }catch (Exception e){
           return SelectPackRequestResponse.builder().message("an exception has occurred while trying to select pack "+e.getMessage()).build();
       }

    }


    public UserbestCardResponse userbest30cards(Userbestcardrequest userbestcardrequest) {
       String thebest30cards=userbestcardrequest.getUserbestcards();
       String[] split30bestcards=thebest30cards.split(",");
       List<Long> converttolist=new ArrayList<>(0);
        Arrays.stream(split30bestcards).forEach(s1 -> converttolist.add(Long.parseLong(s1)));
       long userid=1;
       User user =loginrepo.findById(userid).orElseThrow(() -> new UserNotFoundException("user with given id could not be found "));
        List<UserCard> userCardList=userCardRepo.findAllByUser(user);
        List<Long> usercardids=new ArrayList<>(0);
        userCardList.forEach(userCard -> usercardids.add(userCard.getId()));
        log.info("the ids {}",usercardids);
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
           return UserbestCardResponse.builder().message("user best cards of size "+counter+" saved ").build();
       }catch (Exception e){
           return UserbestCardResponse.builder().message("exception has occurred while fetching user best thirty cards \"+e.getMessage()").build();

       }

    }
}
