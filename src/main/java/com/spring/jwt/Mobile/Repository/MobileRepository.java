package com.spring.jwt.Mobile.Repository;

import com.spring.jwt.Mobile.entity.Mobile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MobileRepository extends JpaRepository<Mobile, Long> {
    Page<Mobile> findByDeletedFalse(Pageable pageable);
    Page<Mobile> findBySeller_SellerIdAndDeletedFalse(Long sellerId, Pageable pageable);
    java.util.Optional<Mobile> findByMobileIdAndDeletedFalse(Long id);
    boolean existsByTitleAndSeller_SellerId(String title, Long sellerId);
//    List<Mobile> findAllWithImages();
@Query("""
    select distinct m
    from Mobile m
    left join fetch m.images
    where m.mobileId in :ids
""")
List<Mobile> findWithImagesByIds(@Param("ids") List<Long> ids);

    @Query("""
    select m
    from Mobile m
    left join fetch m.images
    where m.mobileId = :id and m.deleted = false
""")
    Optional<Mobile> findOneWithImages(@Param("id") Long id);


}
