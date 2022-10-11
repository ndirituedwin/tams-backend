package com.cardgame.Controller;

import com.cardgame.Dto.requests.Getuserbestcardrequest;
import com.cardgame.Dto.responses.GetUserBest30cardsresponse;
import com.cardgame.Dto.responses.Marketplacecardsresponse;
import com.cardgame.Dto.responses.Page.PagedResponse;
import com.cardgame.Service.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cardgame.config.ApiUtils.DEFAULT_PAGE_NUMBER;
import static com.cardgame.config.ApiUtils.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/market-place")
public class Marketplacecontroller {

    @Autowired
    private MarketplaceService marketplaceService;


    @PostMapping("/getmarketplacepacks")
    public PagedResponse<Marketplacecardsresponse> getallcardsforauser(@RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                                       @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size){

        return marketplaceService.getmarketplacepacks(page,size);

    }
}
