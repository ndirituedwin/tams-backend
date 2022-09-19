package com.cardgame.Repo;

import com.cardgame.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Userrepo extends JpaRepository<User,Long> {
    Page<User> findAllByUIDIn(List<Long> userids, Pageable pageable);
    List<User> findAllByUIDIn(List<Long> userids);

   /** @Query(value = "select i from ItemEntity i where i.secondaryId in :ids
            order by FIND_IN_SET(i.secondaryId, :idStr)")
    List<ItemEntity> itemsIn(@Param("ids") List<UUID> ids, @Param("idStr") String idStr);*/
   @Query("select i from User i where i.UID in :uids order by FIND_IN_SET(i.UID, :uidstr)")
    List<User> findAllByUIDInFindInSet(List<Long> uids,String uidstr);


}
