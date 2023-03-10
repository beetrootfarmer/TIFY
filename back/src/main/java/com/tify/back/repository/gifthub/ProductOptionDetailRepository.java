package com.tify.back.repository.gifthub;



import com.tify.back.model.gifthub.Product;
import com.tify.back.model.gifthub.ProductOptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOptionDetailRepository extends JpaRepository<ProductOptionDetail, Long> {
    @Query("SELECT pd FROM ProductOptionDetail pd JOIN pd.productOption po JOIN po.product p WHERE p = :product AND po.title = :optionTitle")
    List<ProductOptionDetail> findByProductAndOptionTitle(@Param("product") Product product, @Param("optionTitle") String optionTitle);
}
