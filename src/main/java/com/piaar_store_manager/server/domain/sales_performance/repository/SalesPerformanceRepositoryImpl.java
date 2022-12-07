package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_order_item.entity.QErpOrderItemEntity;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection.Dashboard;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class SalesPerformanceRepositoryImpl implements SalesPerformanceRepositoryCustom {
    private final JPAQueryFactory query;

    private final QErpOrderItemEntity qErpOrderItemEntity = QErpOrderItemEntity.erpOrderItemEntity;

    @Autowired
    public SalesPerformanceRepositoryImpl(
        JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<Dashboard> qSearchDashBoardByParams(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // client에서 전달되는 날짜값으로 배열만들어서 사용해보기
        if(params.get("date") == null) {
            return null;
        }
        
        List<String> dateList = Arrays.asList(params.get("date").toString().split(","));
        List<String> dateValues =  dateList.stream().map(r -> LocalDate.parse(r, formatter).toString()).collect(Collectors.toList());

        // TODO :: 현재 11-01 조회하면 전날로 조회됨. 근데 이건 서버에서는 정상적으로 돌아갈 수 있으니 확인해봐야함
        List<Dashboard> projs = (List<Dashboard>) query.from(qErpOrderItemEntity)
            .where(dateformatted().in(dateValues))
            .groupBy(dateformatted())
            .orderBy(dateformatted().asc())
            .transform(
                GroupBy.groupBy(dateformatted())
                .list(
                    Projections.fields(
                        Dashboard.class,
                        dateformatted().as("datetime"),
                        (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                            .then(1)
                            .otherwise(0)
                        ).sum().as("orderRegistration"),
                        (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum()).as("orderPayAmount"),
                        (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(1)
                            .otherwise(0)
                        ).sum().as("salesRegistration"),
                        (new CaseBuilder()
                            .when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                            .otherwise(0)
                        ).sum().as("salesPayAmount")
                    )
                )
            );

        return projs;
    }

    private DateTemplate<String> dateformatted() {
        DateTemplate<String> formattedDate = Expressions.dateTemplate(
            String.class,
            "DATE_FORMAT({0}, {1})",
            qErpOrderItemEntity.channelOrderDate,
            ConstantImpl.create("%Y-%m-%d")
        );

        return formattedDate;   
    }
}
