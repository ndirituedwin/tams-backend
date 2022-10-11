package com.cardgame.Service;


import com.cardgame.Dto.responses.Marketplacecardsresponse;
import com.cardgame.Dto.responses.Marketplacecardsresponse;
import com.cardgame.Dto.responses.Page.PagedResponse;
import com.cardgame.Entity.Pack;
import com.cardgame.Entity.Userbestcard;
import com.cardgame.Repo.Packrepo;
import com.cardgame.Service.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketplaceService {

    @Autowired
    private CardService cardService;
    @Autowired
    private Packrepo packrepo;
    public PagedResponse<Marketplacecardsresponse> getmarketplacepacks(int page, int size) {
        cardService.validatePagenumberandSize(page,size);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            List<Pack> packList=  packrepo.findAll().stream().filter(pack -> pack.getGold() == null && pack.getSilver() == null && pack.getBronze() == null && pack.getTrainer() == null).toList();


            //            Page<Pack> packpageable = new PageImpl<>(packList);

            final int start = (int)pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), packList.size());
            final Page<Pack> packpageable = new PageImpl<>(packList.subList(start,end), pageable,packList.size());

            if (packpageable.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), packpageable.getNumber(), packpageable.getSize(), packpageable.getTotalElements(), packpageable.getTotalPages(), packpageable.isLast());
            }

             List<Marketplacecardsresponse>  marketplacecardslist = packpageable.map(ModelMapper::mapmarketplacepackstomarketplaceresponse).getContent();
            return new PagedResponse<>( marketplacecardslist, packpageable.getNumber(), packpageable.getSize(), packpageable.getTotalElements(), packpageable.getTotalPages(), packpageable.isLast());

        }catch (Exception e){
            throw new RuntimeException("An exception has occured while fetching market place cards "+e);
        }
    }
}
