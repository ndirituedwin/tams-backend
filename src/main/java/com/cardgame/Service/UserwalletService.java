package com.cardgame.Service;

import com.cardgame.Dto.requests.Addmoneytowalletrequest;
import com.cardgame.Dto.requests.Getwalletbaancerequest;
import com.cardgame.Dto.requests.Withdrawmoneyrequest;
import com.cardgame.Dto.responses.Addmoneytowaletresponse;
import com.cardgame.Dto.responses.Userwalletbalance;
import com.cardgame.Dto.responses.Withdrawresponse;
import com.cardgame.Entity.*;
import com.cardgame.Exceptions.UserNotFoundException;
import com.cardgame.Exceptions.UserWalletNotFoundException;
import com.cardgame.Repo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;

@Service
@Slf4j
public class UserwalletService {

    private final Loginrepo loginrepo;
    private final Userwalletrepo userwalletrepo;
    private final Userdepositrepo userdepositrepo;
    private final Withdrawrepo withdrawrepo;
    private final Changingwalletbalancerepo changingwalletbalancerepo;

    public UserwalletService(Loginrepo loginrepo, Userwalletrepo userwalletrepo, Userdepositrepo userdepositrepo, Withdrawrepo withdrawrepo, Changingwalletbalancerepo changingwalletbalancerepo) {
        this.loginrepo = loginrepo;
        this.userwalletrepo = userwalletrepo;
        this.userdepositrepo = userdepositrepo;
        this.withdrawrepo = withdrawrepo;
        this.changingwalletbalancerepo = changingwalletbalancerepo;
    }

    public Userwalletbalance getwalletservice(Getwalletbaancerequest getwalletbaancerequest) {
        if (getwalletbaancerequest.getUid()==null || getwalletbaancerequest.getUid()==0){
            return new Userwalletbalance("uid may not be null");
        }


        User user=loginrepo.findById(getwalletbaancerequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));
        try {
            boolean existsbyuserid=userwalletrepo.existsByUserid(user.getId());
            if (existsbyuserid){

                Userwallet userwallet=userwalletrepo.findByUserid(user.getId()).orElseThrow(() -> new UserWalletNotFoundException("user wallet not not found"));
                return new Userwalletbalance(userwallet.getTotalwalletbalance());
            }else {
                return new Userwalletbalance("You have no details yet.Deposit funds first to view your balance");
            }
        }catch (Exception e){
            return new Userwalletbalance("An exception has occured while fetching wallet balance for the user "+e.getMessage());
        }

    }

    @Transactional
    public Addmoneytowaletresponse addmoneytowallet(Addmoneytowalletrequest addmoneytowallet) {

        if (addmoneytowallet.getAmount()==null){
            return new Addmoneytowaletresponse("amount may not be null");
        }
        if (addmoneytowallet.getAmount().compareTo(BigDecimal.ZERO)<1){
            return new Addmoneytowaletresponse("you cannot add negative or zero  amount ");
        }
        if (addmoneytowallet.getUid()==null||addmoneytowallet.getUid()==0){
            return new Addmoneytowaletresponse("uid may no be null");

        }

//        boolean existsbyorderorpaymentids=userdepositrepo.existsByOrderidOrPaymentid(addmoneytowallet.getOrderid(),addmoneytowallet.getPaymentid());
//       if (existsbyorderorpaymentids){
//           return new Addmoneytowaletresponse("order id or payment id provide already exist");
//       }
        User user=loginrepo.findById(addmoneytowallet.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));
        try {

            boolean userwalletexists=userwalletrepo.existsByUserid(user.getId());
            if (userwalletexists){
//                log.info("firt one");
                Userwallet userwallet=userwalletrepo.findByUserid(user.getId()).orElseThrow(() -> new UserWalletNotFoundException("your user wallet could not be found "));
               BigDecimal availablebalance=userwallet.getTotalwalletbalance();
                userwallet.setTotalwalletbalance(availablebalance.add(addmoneytowallet.getAmount()));
                 BigDecimal availablewalletbalance=userwalletrepo.save(userwallet).getTotalwalletbalance();

                 Userdeposit userdeposit=new Userdeposit();
                 userdeposit.setUserid(userwallet.getUserid());
                 userdeposit.setAmountadded(addmoneytowallet.getAmount());
                 userdeposit.setOrderid(addmoneytowallet.getOrderid());
                 userdeposit.setUserwallet(userwallet);
                 userdeposit.setPaymentid(addmoneytowallet.getPaymentid());
                 userdepositrepo.save(userdeposit);

                Changingwalletbalance changingwalletbalance=new Changingwalletbalance();
                changingwalletbalance.setUserid(user.getId());
                changingwalletbalance.setAction("DEPOSIT");
                changingwalletbalance.setPreviousbalance(availablebalance);
                changingwalletbalance.setNewbalance(availablewalletbalance);
                changingwalletbalance.setUserwallet(userwallet);
                changingwalletbalancerepo.save(changingwalletbalance);
                 return new Addmoneytowaletresponse("Your wallet has been successfully updated.your new balance is:"+availablewalletbalance.toString());
             }else {

               Userwallet newuserwallet=new Userwallet();
               newuserwallet.setUserid(user.getId());
               newuserwallet.setTotalwalletbalance(addmoneytowallet.getAmount());
               BigDecimal userwalletttb=userwalletrepo.save(newuserwallet).getTotalwalletbalance();
//               log.info("userwalletttb {}",userwalletttb);

               Userdeposit userdeposit2=new Userdeposit();
               userdeposit2.setUserid(newuserwallet.getUserid());
               userdeposit2.setAmountadded(addmoneytowallet.getAmount());
               userdeposit2.setOrderid(addmoneytowallet.getOrderid());
               userdeposit2.setUserwallet(newuserwallet);

               userdeposit2.setPaymentid(addmoneytowallet.getPaymentid());
               userdepositrepo.save(userdeposit2);

                Changingwalletbalance changingwalletbalance=new Changingwalletbalance();
                changingwalletbalance.setUserid(user.getId());
                changingwalletbalance.setAction("DEPOSIT");
                changingwalletbalance.setPreviousbalance(userwalletttb.subtract(addmoneytowallet.getAmount()));
                changingwalletbalance.setNewbalance(userwalletttb);
                changingwalletbalance.setUserwallet(newuserwallet);

                changingwalletbalancerepo.save(changingwalletbalance);

                return new Addmoneytowaletresponse("Your wallet has been successfully updated.your new balance is:"+userwalletttb.toString());
           }





         }catch (Exception e){
             return new Addmoneytowaletresponse("An exception has occurred while adding money to your wallet "+e.getMessage());
         }

    }

    @Transactional
    public Withdrawresponse withdraw(Withdrawmoneyrequest withdrawmoneyrequest) {

        if (withdrawmoneyrequest.getWithdrawamount()==null){
            return new Withdrawresponse("amount to withdraw may not  be null",withdrawmoneyrequest.getWithdrawamount());
        }
        if (withdrawmoneyrequest.getWithdrawamount().compareTo(BigDecimal.ZERO)<0){
            return new Withdrawresponse("you have entered invalid amount to withdraw ",withdrawmoneyrequest.getWithdrawamount());
        }
        if (withdrawmoneyrequest.getUid()==null| withdrawmoneyrequest.getUid()==0){
            return new Withdrawresponse("uid may no be null",withdrawmoneyrequest.getWithdrawamount());
        }

        User user=loginrepo.findById(withdrawmoneyrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));
        try {
            Userwallet userwallet=userwalletrepo.findByUserid(user.getId()).orElseThrow(() -> new UserWalletNotFoundException("user wallet for that user could not be found"));
            BigDecimal amountavailable=userwallet.getTotalwalletbalance();
            if (amountavailable.compareTo(BigDecimal.ZERO)<=0){
                return new Withdrawresponse("you have insufficient funds to complete the withdrawal",amountavailable);
            }
            if (amountavailable.compareTo(withdrawmoneyrequest.getWithdrawamount())==-1){
                return new Withdrawresponse("Amount to withdraw cannot be greater that the available balance",amountavailable);

            }
            if (amountavailable.compareTo(withdrawmoneyrequest.getWithdrawamount())==0 || amountavailable.compareTo(withdrawmoneyrequest.getWithdrawamount())==1){
//               log.info("withdraw {} {}",amountavailable,withdrawmoneyrequest.getWithdrawamount());
               BigDecimal availablebalance=userwallet.getTotalwalletbalance();
               userwallet.setTotalwalletbalance(amountavailable.subtract(withdrawmoneyrequest.getWithdrawamount()));
               BigDecimal leftbalance=userwalletrepo.save(userwallet).getTotalwalletbalance();
                Withdrawal withdrawal=new Withdrawal();
                withdrawal.setAmount(withdrawmoneyrequest.getWithdrawamount());
                withdrawal.setCreateddate(Instant.now());
                withdrawal.setUserid(user.getId());
                withdrawal.setOrderid(withdrawmoneyrequest.getOrderid());
                withdrawal.setPaymentid(withdrawmoneyrequest.getPaymentid());
                BigDecimal withdrawal2=withdrawrepo.save(withdrawal).getAmount();
                Changingwalletbalance changingwalletbalance=new Changingwalletbalance();
                changingwalletbalance.setUserid(user.getId());
                changingwalletbalance.setAction("WITHDRAW");
                changingwalletbalance.setPreviousbalance(availablebalance);
                changingwalletbalance.setNewbalance(leftbalance);
                changingwalletbalance.setUserwallet(userwallet);

                changingwalletbalancerepo.save(changingwalletbalance);


                return new Withdrawresponse("withrawal of "+withdrawal2+" was successfull.Available balance is: "+leftbalance,leftbalance);
            }

        }catch (Exception e){
            return new Withdrawresponse("An exception has occurred while trying to withdraw the money "+e.getMessage(),withdrawmoneyrequest.getWithdrawamount());
        }

        return null;
    }
}
