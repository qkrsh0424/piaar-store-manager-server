package com.piaar_store_manager.server.domain.sub_option_code.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sub_option_code.proj.SubOptionCodeProjection;

@Repository
public interface SubOptionCodeRepositoryCustom {
    List<SubOptionCodeProjection.RelatedProductOption> qSearchAll();
}
