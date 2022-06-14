package com.piaar_store_manager.server.domain.product_option.repository;

import java.util.List;

import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.stock_analysis.proj.StockAnalysisProj;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepositoryCustom {
    List<ProductOptionProj> qfindAllM2OJ();
    List<StockAnalysisProj> qfindStockAnalysis();
}
