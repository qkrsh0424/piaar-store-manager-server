package com.piaar_store_manager.server.domain.sales_performance.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_category.service.ProductCategoryService;
import com.piaar_store_manager.server.domain.sales_performance.dto.BestProductPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesCategoryPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesChannelPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesProductPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.SalesPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.proj.BestProductPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceBusinessService {
    private final SalesPerformanceService salesPerformanceService;
    private final ProductCategoryService productCategoryService;

    public List<SalesPerformanceDto> searchDashboard(DashboardPerformanceSearchFilter filter) {
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

		if (filter.getSearchDate() == null || filter.getSearchDate().size() == 0) {
			throw new CustomInvalidDataException("검색 기간이 올바르지 않습니다.");
		}

		List<String> dateValues = filter.getSearchDate().stream()
				.map(r -> CustomDateUtils.changeUtcDateTime(r, utcHourDifference).toLocalDate().toString())
				.collect(Collectors.toList());
              
        List<SalesPerformanceProjection> initProjs = this.getDashboardInitProjs(dateValues);
        List<SalesPerformanceProjection> performanceProjs = salesPerformanceService.qSearchDashBoardByParams(filter);
        this.setDashboardInitProjs(initProjs, performanceProjs);
        
        List<SalesPerformanceDto> dtos = initProjs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesPerformanceDto> searchSalesPerformance(SalesPerformanceSearchFilter filter) {
        List<SalesPerformanceProjection> initProjs = this.getSalesPerformanceInitProjs(filter);
        List<SalesPerformanceProjection> performanceProjs = salesPerformanceService.qSearchSalesPerformanceByParams(filter);
        this.setSalesPerformanceInitProjs(initProjs, performanceProjs);
        List<SalesPerformanceDto> dtos = initProjs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDto.PerformanceByDate> searchSalesPerformanceByChannel(SalesPerformanceSearchFilter filter) {
        List<SalesChannelPerformanceProjection.PerformanceByDate> initProjs = this.getSalesChannelPerformanceInitProjs(filter);        
        List<SalesChannelPerformanceProjection> performanceProjs = salesPerformanceService.qSearchChannelSalesPerformanceByFilter(filter);
        this.setSalesChannelPerformanceInitProjs(initProjs, performanceProjs);
        List<SalesChannelPerformanceDto.PerformanceByDate> dtos = initProjs.stream().map(SalesChannelPerformanceDto.PerformanceByDate::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDto> searchProductSalesPerformanceByChannel(SalesPerformanceSearchFilter filter) {
        List<SalesChannelPerformanceProjection> projs = salesPerformanceService.qSearchProductChannelSalesPerformanceByFilter(filter);
        List<SalesChannelPerformanceDto> dtos = projs.stream().map(SalesChannelPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesCategoryPerformanceDto.PerformanceByDate> searchSalesPerformanceByCategory(SalesPerformanceSearchFilter filter) {
        List<SalesCategoryPerformanceProjection.PerformanceByDate> initProjs = this.getSalesCategoryPerformanceInitProjs(filter);
        List<SalesCategoryPerformanceProjection> performanceProjs = salesPerformanceService.qSearchCategorySalesPerformanceByParams(filter);
        this.setSalesCategoryPerformanceInitProjs(initProjs, performanceProjs);
        List<SalesCategoryPerformanceDto.PerformanceByDate> dtos = initProjs.stream().map(SalesCategoryPerformanceDto.PerformanceByDate::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesCategoryPerformanceDto.PerformanceByProductCategoryName> searchSalesProductPerformanceByCategory(SalesPerformanceSearchFilter filter) {
        List<ProductCategoryEntity> categoryEntities = productCategoryService.searchAll();
        List<String> categoryName = categoryEntities.stream().map(r -> r.getName()).collect(Collectors.toList());
        List<SalesCategoryPerformanceProjection.PerformanceByProductCategoryName> initProjs = this.getSalesCategoryAndProductPerformanceInitProjs(categoryName);
        List<SalesCategoryPerformanceProjection> performanceProjs = salesPerformanceService.qSearchCategoryAndProductSalesPerformanceByParams(filter, categoryName);
        this.setSalesCategoryAndProductPerformanceInitProjs(initProjs, performanceProjs);
        List<SalesCategoryPerformanceDto.PerformanceByProductCategoryName> dtos = initProjs.stream().map(SalesCategoryPerformanceDto.PerformanceByProductCategoryName::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesProductPerformanceDto.PerformanceByDate> searchSalesPerformanceByProductOption(SalesPerformanceSearchFilter filter) {
        List<SalesProductPerformanceProjection.PerformanceByDate> initProjs = this.getSalesProductOptionPerformanceInitProjs(filter);
        List<SalesProductPerformanceProjection> performanceProjs = salesPerformanceService.qSearchProductOptionSalesPerformanceByFilter(filter);
		this.setSalesProductOptionPerformanceInitProjs(initProjs, performanceProjs);
        List<SalesProductPerformanceDto.PerformanceByDate> dtos = initProjs.stream().map(SalesProductPerformanceDto.PerformanceByDate::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesPerformanceDto> searchSalesPerformanceByProduct(SalesPerformanceSearchFilter filter) {
        List<SalesPerformanceProjection> initProjs = this.getProductSalesPerformanceInitProjs(filter);        
        List<SalesProductPerformanceProjection> performanceProjs = salesPerformanceService.qSearchProductSalesPerformanceByFilter(filter);
		this.setProductSalesPerformanceInitProjs(initProjs, performanceProjs);
        List<SalesPerformanceDto> dtos = initProjs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public Page<BestProductPerformanceDto> searchBestProductPerformance(SalesPerformanceSearchFilter filter, Pageable pageable) {
        Page<BestProductPerformanceProjection> projs = salesPerformanceService.qSearchBestProductPerformanceByFilter(filter, pageable);
        List<BestProductPerformanceDto> dtos = projs.stream().map(BestProductPerformanceDto::toDto).collect(Collectors.toList());
        return new PageImpl(dtos, pageable, projs.getTotalElements());
    }

    public List<BestProductPerformanceDto.RelatedProductOptionPerformance> searchProductOptionPerformanceByProduct(SalesPerformanceSearchFilter filter) {
        List<BestProductPerformanceProjection.RelatedProductOptionPerformance> projs = salesPerformanceService.qSearchProductOptionPerformanceByFilter(filter);
        List<BestProductPerformanceDto.RelatedProductOptionPerformance> dtos = projs.stream().map(BestProductPerformanceDto.RelatedProductOptionPerformance::toDto).collect(Collectors.toList());
        return dtos;
    }

    /*
	 * dashboard projs 세팅
	 */
	private List<SalesPerformanceProjection> getDashboardInitProjs(List<String> dateValues) {
		List<SalesPerformanceProjection> projs = new ArrayList<>();

		dateValues.forEach(dateValue -> {
			projs.add(
				SalesPerformanceProjection.builder()
					.datetime(dateValue)
					.orderRegistration(0)
					.salesRegistration(0)
					.orderUnit(0)
					.salesUnit(0)
					.orderPayAmount(0)
					.salesPayAmount(0)
					.build()
			);
		});
		return projs;
	}

    private void setDashboardInitProjs(List<SalesPerformanceProjection> initProjs, List<SalesPerformanceProjection> performanceProjs) {
		initProjs.forEach(initProj -> {
			performanceProjs.forEach(performanceProj -> {
				if (initProj.getDatetime().equals(performanceProj.getDatetime())) {
					initProj.setOrderRegistration(performanceProj.getOrderRegistration())
							.setOrderUnit(performanceProj.getOrderUnit())
							.setOrderPayAmount(performanceProj.getOrderPayAmount())
							.setSalesRegistration(performanceProj.getSalesRegistration())
							.setSalesUnit(performanceProj.getSalesUnit())
							.setSalesPayAmount(performanceProj.getSalesPayAmount());
				}
			});
		});
	}

    /*
	 * sales performance projs 세팅
	 */
	private List<SalesPerformanceProjection> getSalesPerformanceInitProjs(SalesPerformanceSearchFilter filter) {
		LocalDateTime startDate = filter.getStartDate();
		LocalDateTime endDate = filter.getEndDate();

		if (startDate == null || endDate == null) {
			throw new CustomInvalidDataException("검색 기간이 올바르지 않습니다.");
		}

		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
		startDate = CustomDateUtils.changeUtcDateTime(startDate, utcHourDifference);
		endDate = CustomDateUtils.changeUtcDateTime(endDate, utcHourDifference);
		int dateDiff = (int) Duration.between(startDate, endDate).toDays();

		List<SalesPerformanceProjection> projs = new ArrayList<>();
		String datetime = null;

		for (int i = 0; i <= dateDiff; i++) {
			datetime = startDate.plusDays(i).toLocalDate().toString();

			projs.add(
				SalesPerformanceProjection.builder()
					.datetime(datetime)
					.orderRegistration(0)
					.orderUnit(0)
					.orderPayAmount(0)
					.salesRegistration(0)
					.salesUnit(0)
					.salesPayAmount(0)
					.build()
			);
		}
		return projs;
	}

	private void setSalesPerformanceInitProjs(List<SalesPerformanceProjection> initProjs, List<SalesPerformanceProjection> performanceProjs) {
		initProjs.forEach(r -> {
			performanceProjs.forEach(r2 -> {
				if (r.getDatetime().equals(r2.getDatetime())) {
					r.setOrderRegistration(r2.getOrderRegistration())
							.setOrderUnit(r2.getOrderUnit())
							.setOrderPayAmount(r2.getOrderPayAmount())
							.setSalesRegistration(r2.getSalesRegistration())
							.setSalesUnit(r2.getSalesUnit())
							.setSalesPayAmount(r2.getSalesPayAmount());
				}
			});
		});
	}

    private List<SalesChannelPerformanceProjection.PerformanceByDate> getSalesChannelPerformanceInitProjs(SalesPerformanceSearchFilter filter) {
		LocalDateTime startDate = filter.getStartDate();
		LocalDateTime endDate = filter.getEndDate();

		if (startDate == null || endDate == null) {
			throw new CustomInvalidDataException("검색 기간이 올바르지 않습니다.");
		}

		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
		startDate = CustomDateUtils.changeUtcDateTime(filter.getStartDate(), utcHourDifference);
		endDate = CustomDateUtils.changeUtcDateTime(filter.getEndDate(), utcHourDifference);
		int dateDiff = (int) Duration.between(filter.getStartDate(), filter.getEndDate()).toDays();

		List<SalesChannelPerformanceProjection.PerformanceByDate> projs = new ArrayList<>();
		String datetime = null;
		for (int i = 0; i <= dateDiff; i++) {
			datetime = startDate.plusDays(i).toLocalDate().toString();

			projs.add(
				SalesChannelPerformanceProjection.PerformanceByDate.builder()
					.datetime(datetime)
					.build()
			);
		}
		return projs;
	}

	private void setSalesChannelPerformanceInitProjs(List<SalesChannelPerformanceProjection.PerformanceByDate> initProjs, List<SalesChannelPerformanceProjection> performanceProjs) {
		int initProjsSize = initProjs.size();
		int performanceProjsSize = performanceProjs.size();

		List<SalesChannelPerformanceProjection> performances = null;
		SalesChannelPerformanceProjection.PerformanceByDate initProj = null;
		SalesChannelPerformanceProjection performanceProj = null;

		for(int i = 0; i < initProjsSize; i++) {
			performances = new ArrayList<>();
			initProj = initProjs.get(i);

			for(int j = 0; j < performanceProjsSize; j++) {
				performanceProj = performanceProjs.get(j);
				if (initProj.getDatetime().equals(performanceProj.getDatetime())) {
					String channelName = performanceProj.getSalesChannel().isBlank() ? "미지정" : performanceProj.getSalesChannel();

					performances.add(
						SalesChannelPerformanceProjection.builder()
							.datetime(performanceProj.getDatetime())
							.salesChannel(channelName)
							.orderRegistration(performanceProj.getOrderRegistration())
							.orderUnit(performanceProj.getOrderUnit())
							.orderPayAmount(performanceProj.getOrderPayAmount())
							.salesRegistration(performanceProj.getSalesRegistration())
							.salesUnit(performanceProj.getSalesUnit())
							.salesPayAmount(performanceProj.getSalesPayAmount())
							.build()
					);
				}
				initProj.setPerformance(performances);
			}
		}
	}

    /*
	 * sales category performance projs 세팅
	 */
	private List<SalesCategoryPerformanceProjection.PerformanceByDate> getSalesCategoryPerformanceInitProjs(SalesPerformanceSearchFilter filter) {
		LocalDateTime startDate = filter.getStartDate();
		LocalDateTime endDate = filter.getEndDate();

		if (startDate == null || endDate == null) {
			throw new CustomInvalidDataException("검색 기간이 올바르지 않습니다.");
		}

		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
		startDate = CustomDateUtils.changeUtcDateTime(startDate, utcHourDifference);
		endDate = CustomDateUtils.changeUtcDateTime(endDate, utcHourDifference);
		int dateDiff = (int) Duration.between(startDate, endDate).toDays();

		// List<SalesCategoryPerformanceProjection> performances = categoryName.stream().map(r -> {
		// 	return SalesCategoryPerformanceProjection.builder()
		// 			.productCategoryName(r)
		// 			.orderRegistration(0)
		// 			.orderUnit(0)
		// 			.orderPayAmount(0)
		// 			.salesRegistration(0)
		// 			.salesUnit(0)
		// 			.salesPayAmount(0)
		// 			.build();
		// }).collect(Collectors.toList());

		List<SalesCategoryPerformanceProjection.PerformanceByDate> projs = new ArrayList<>();
		String datetime = null;
		for (int i = 0; i <= dateDiff; i++) {
			datetime = startDate.plusDays(i).toLocalDate().toString();

			projs.add(
				SalesCategoryPerformanceProjection.PerformanceByDate
					.builder()
					.datetime(datetime)
					// .performance(performances)
					.build()
			);
		}

		return projs;
	}

	// private void setSalesCategoryPerformanceInitProjs(List<SalesCategoryPerformanceProjection.PerformanceByDate> initProjs, List<SalesCategoryPerformanceProjection> performanceProjs) {
	// 	int initProjsSize = initProjs.size();
	// 	int performanceProjsSize = performanceProjs.size();

	// 	List<SalesCategoryPerformanceProjection> performances = null;
	// 	SalesCategoryPerformanceProjection.PerformanceByDate initProj = null;
	// 	SalesCategoryPerformanceProjection performanceProj = null;
	// 	SalesCategoryPerformanceProjection detailProj = null;

	// 	for(int i = 0; i < initProjsSize; i++) {
	// 		initProj = initProjs.get(i);
			
	// 		for(int j = 0; j < performanceProjsSize; j++) {
	// 			performances = new ArrayList<>();
	// 			performanceProj = performanceProjs.get(j);
				
	// 			if(initProj.getDatetime().equals(performanceProj.getDatetime())) {
	// 				for(int k = 0; k < initProj.getPerformance().size(); k++) {
	// 					detailProj = initProj.getPerformance().get(k);
	// 					String categoryName = (detailProj.getProductCategoryName() == null || detailProj.getProductCategoryName().isBlank()) ? "미지정" : detailProj.getProductCategoryName();
						
	// 					if(categoryName.equals(performanceProj.getProductCategoryName())) {
	// 						SalesCategoryPerformanceProjection proj = SalesCategoryPerformanceProjection.builder()
	// 								.productCategoryName(categoryName)
	// 								.orderRegistration(performanceProj.getOrderRegistration())
	// 								.orderUnit(performanceProj.getOrderUnit())
	// 								.orderPayAmount(performanceProj.getOrderPayAmount())
	// 								.salesRegistration(performanceProj.getSalesRegistration())
	// 								.salesUnit(performanceProj.getSalesUnit())
	// 								.salesPayAmount(performanceProj.getSalesPayAmount())
	// 								.build();

	// 						performances.add(proj);
	// 					} else {
	// 						performances.add(detailProj);
	// 					}
	// 				}
	// 				initProj.setPerformance(performances);
	// 			}
	// 		}
	// 	}
	// }

	private void setSalesCategoryPerformanceInitProjs(List<SalesCategoryPerformanceProjection.PerformanceByDate> initProjs, List<SalesCategoryPerformanceProjection> performanceProjs) {
		int initProjsSize = initProjs.size();
		int performanceProjsSize = performanceProjs.size();

		for(int i = 0; i < initProjsSize; i++) {
			List<SalesCategoryPerformanceProjection> projs = new ArrayList<>();

			for(int j = 0; j < performanceProjsSize; j++) {
				if(initProjs.get(i).getDatetime().equals(performanceProjs.get(j).getDatetime())) {
					String productCategoryName = performanceProjs.get(j).getProductCategoryName() == null || performanceProjs.get(j).getProductCategoryName().isBlank() ? "미지정" : performanceProjs.get(j).getProductCategoryName();
					
					SalesCategoryPerformanceProjection proj = SalesCategoryPerformanceProjection.builder()
								.productCategoryName(productCategoryName)
								.orderRegistration(performanceProjs.get(j).getOrderRegistration())
								.orderUnit(performanceProjs.get(j).getOrderUnit())
								.orderPayAmount(performanceProjs.get(j).getOrderPayAmount())
								.salesRegistration(performanceProjs.get(j).getSalesRegistration())
								.salesUnit(performanceProjs.get(j).getSalesUnit())
								.salesPayAmount(performanceProjs.get(j).getSalesPayAmount())
								.build();
					
					projs.add(proj);
				}
			}

			initProjs.get(i).setPerformance(projs);
		}
	}

    private List<SalesCategoryPerformanceProjection.PerformanceByProductCategoryName> getSalesCategoryAndProductPerformanceInitProjs(List<String> categoryName) {
        List<SalesCategoryPerformanceProjection.PerformanceByProductCategoryName> projs = categoryName.stream().map(category -> {
			return SalesCategoryPerformanceProjection.PerformanceByProductCategoryName
					.builder()
					.productCategoryName(category)
					.build();
		}).collect(Collectors.toList());

		return projs;
	}

	private void setSalesCategoryAndProductPerformanceInitProjs(List<SalesCategoryPerformanceProjection.PerformanceByProductCategoryName> initProjs, List<SalesCategoryPerformanceProjection> performanceProjs) {
		int initProjsSize = initProjs.size();
		int performanceProjsSize = performanceProjs.size();

		List<SalesCategoryPerformanceProjection> performances = null;
		SalesCategoryPerformanceProjection.PerformanceByProductCategoryName initProj = null;
		SalesCategoryPerformanceProjection performanceProj = null;

		for (int i = 0; i < initProjsSize; i++) {
			performances = new ArrayList<>();
			initProj = initProjs.get(i);

			for (int j = 0; j < performanceProjsSize; j++) {
				performanceProj = performanceProjs.get(j);
				if (initProj.getProductCategoryName().equals(performanceProj.getProductCategoryName())) {
					performances.add(
							SalesCategoryPerformanceProjection.builder()
									.productCategoryName(performanceProj.getProductCategoryName())
									.productDefaultName(performanceProj.getProductDefaultName())
									.orderRegistration(performanceProj.getOrderRegistration())
									.orderUnit(performanceProj.getOrderUnit())
									.orderPayAmount(performanceProj.getOrderPayAmount())
									.salesRegistration(performanceProj.getSalesRegistration())
									.salesUnit(performanceProj.getSalesUnit())
									.salesPayAmount(performanceProj.getSalesPayAmount())
									.build());
				}
			}
			initProj.setPerformance(performances);
		}
    }

    private List<SalesProductPerformanceProjection.PerformanceByDate> getSalesProductOptionPerformanceInitProjs(SalesPerformanceSearchFilter filter) {
		LocalDateTime startDate = filter.getStartDate();
		LocalDateTime endDate = filter.getEndDate();

		if (startDate == null || endDate == null) {
			return null;
		}

		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
		startDate = CustomDateUtils.changeUtcDateTime(startDate, utcHourDifference);
		endDate = CustomDateUtils.changeUtcDateTime(endDate, utcHourDifference);
		int dateDiff = (int) Duration.between(startDate, endDate).toDays();

		List<SalesProductPerformanceProjection.PerformanceByDate> projs = new ArrayList<>();
		String datetime = null;
		for (int i = 0; i <= dateDiff; i++) {
			datetime = startDate.plusDays(i).toLocalDate().toString();

			projs.add(
				SalesProductPerformanceProjection.PerformanceByDate.builder()
					.datetime(datetime)
					.build()
			);
		}
		return projs;
	}

	private void setSalesProductOptionPerformanceInitProjs(
			List<SalesProductPerformanceProjection.PerformanceByDate> initProjs,
			List<SalesProductPerformanceProjection> performanceProjs) {
		int initProjsSize = initProjs.size();
		int performanceProjsSize = performanceProjs.size();

		List<SalesProductPerformanceProjection> performances = null;
		SalesProductPerformanceProjection.PerformanceByDate initProj = null;
		SalesProductPerformanceProjection performanceProj = null;

		for(int i = 0; i < initProjsSize; i++) {
			performances = new ArrayList<>();
			initProj = initProjs.get(i);

			for(int j = 0; j < performanceProjsSize; j++) {
				performanceProj = performanceProjs.get(j);

				if(initProj.getDatetime().equals(performanceProj.getDatetime())) {
					performances.add(
						SalesProductPerformanceProjection
							.builder()
							.datetime(performanceProj.getDatetime())
							.productCode(performanceProj.getProductCode())
							.productDefaultName(performanceProj.getProductDefaultName())
							.optionCode(performanceProj.getOptionCode())
							.optionDefaultName(performanceProj.getOptionDefaultName())
							.orderRegistration(performanceProj.getOrderRegistration())
							.orderUnit(performanceProj.getOrderUnit())
							.orderPayAmount(performanceProj.getOrderPayAmount())
							.salesRegistration(performanceProj.getSalesRegistration())
							.salesUnit(performanceProj.getSalesUnit())
							.salesPayAmount(performanceProj.getSalesPayAmount())
							.build());
				}
			}
			initProj.setPerformance(performances);
		}
	}

    private List<SalesPerformanceProjection> getProductSalesPerformanceInitProjs(SalesPerformanceSearchFilter filter) {
		LocalDateTime startDate = filter.getStartDate();
		LocalDateTime endDate = filter.getEndDate();

		if (startDate == null || endDate == null) {
			return null;
		}

		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
		startDate = CustomDateUtils.changeUtcDateTime(startDate, utcHourDifference);
		endDate = CustomDateUtils.changeUtcDateTime(endDate, utcHourDifference);
		int dateDiff = (int) Duration.between(startDate, endDate).toDays();

		List<SalesPerformanceProjection> projs = new ArrayList<>();
		String datetime = null;
		for (int i = 0; i <= dateDiff; i++) {
			datetime = startDate.plusDays(i).toLocalDate().toString();

			projs.add(
				SalesPerformanceProjection.builder()
					.datetime(datetime)
					.orderRegistration(0)
					.orderUnit(0)
					.orderPayAmount(0)
					.salesRegistration(0)
					.salesUnit(0)
					.salesPayAmount(0)
					.build()
			);
		}

		return projs;
	}

	private void setProductSalesPerformanceInitProjs(List<SalesPerformanceProjection> initProjs, List<SalesProductPerformanceProjection> performanceProjs) {
		initProjs.forEach(r -> {
			performanceProjs.forEach(r2 -> {
				if (r.getDatetime().equals(r2.getDatetime())) {
					r.setOrderRegistration(r2.getOrderRegistration())
							.setOrderUnit(r2.getOrderUnit())
							.setOrderPayAmount(r2.getOrderPayAmount())
							.setSalesRegistration(r2.getSalesRegistration())
							.setSalesUnit(r2.getSalesUnit())
							.setSalesPayAmount(r2.getSalesPayAmount());
				}
			});
		});
	}
}
