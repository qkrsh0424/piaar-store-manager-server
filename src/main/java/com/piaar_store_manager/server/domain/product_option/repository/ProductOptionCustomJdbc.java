package com.piaar_store_manager.server.domain.product_option.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionStockCycleDto;

@Repository
public interface ProductOptionCustomJdbc {
    List<ProductOptionStockCycleDto> searchStockStatusByWeek(LocalDateTime searchEndDate, Integer productId);
}
