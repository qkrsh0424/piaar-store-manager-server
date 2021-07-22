package com.piaar_store_manager.server.service.product_option;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.account_book.repository.AccountBookRepository;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product.repository.ProductRepository;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_option.repository.ProductOptionRepository;
import com.piaar_store_manager.server.model.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductOptionService {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private DateHandler dateHandler;

    @Autowired
    private UserRepository userRepository;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption id 값과 상응되는 데이터를 조회한다.
     *
     * @param productOptionId
     * @return ProductOptionGetDto
     * @see ProductOptionRepository
     */
    public ProductOptionGetDto searchOne(Integer productOptionId){
        Optional<ProductOptionEntity> productOptionEntityOpt = productOptionRepository.findById(productOptionId);
        ProductOptionGetDto productOptionDto = new ProductOptionGetDto();

        if(productOptionEntityOpt.isPresent()){
            productOptionDto = getDtoByEntitiy(productOptionEntityOpt.get());
        }

        return productOptionDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductOptionEntity:: => List::ProductOptionGetDto::
     * @param productOptionEntity
     * @return ProductOptionGetDto
     */
    private ProductOptionGetDto getDtoByEntitiy(ProductOptionEntity productOptionEntity){
        ProductOptionGetDto productOptionDto = new ProductOptionGetDto();

        productOptionDto.setDefaultName(productOptionEntity.getDefaultName())
                    .setManagementName(productOptionEntity.getManagementName())
                    .setSalesPrice(productOptionEntity.getSalesPrice())
                    .setStockUnit(productOptionEntity.getStockUnit())
                    .setStatus(productOptionEntity.getStatus())
                    .setMemo(productOptionEntity.getMemo())
                    .setCreatedAt(productOptionEntity.getCreatedAt())
                    .setCreatedBy(productOptionEntity.getCreatedBy())
                    .setUpdatedAt(productOptionEntity.getUpdatedAt())
                    .setUpdatedBy(productOptionEntity.getUpdatedBy())
                    .setProductCid(productOptionEntity.getProductCid());

        return productOptionDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 상품옵션들을 모두 조회한다.
     *
     * @return List::ProductOptionDto::
     * @see ProductOptionRepository
     */
    public List<ProductOptionGetDto> searchList(){
        List<ProductOptionEntity> productOptionEntities = productOptionRepository.findAll();
        List<ProductOptionGetDto> productOptionDto = getDtoByEntities(productOptionEntities);

        return productOptionDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::productOptionEntities:: => List::ProductOptionGetDto::
     * @param productOptionEntities
     * @return List::ProductOptionGetDto::
     */
    private List<ProductOptionGetDto> getDtoByEntities(List<ProductOptionEntity> productOptionEntities){
        List<ProductOptionGetDto> productOptionDtos = new ArrayList<>();

        for(ProductOptionEntity productOptionEntity : productOptionEntities) {
            ProductOptionGetDto productOptionDto = new ProductOptionGetDto();

            productOptionDto.setDefaultName(productOptionEntity.getDefaultName())
                    .setManagementName(productOptionEntity.getManagementName())
                    .setSalesPrice(productOptionEntity.getSalesPrice())
                    .setStockUnit(productOptionEntity.getStockUnit())
                    .setStatus(productOptionEntity.getStatus())
                    .setMemo(productOptionEntity.getMemo())
                    .setCreatedAt(productOptionEntity.getCreatedAt())
                    .setCreatedBy(productOptionEntity.getCreatedBy())
                    .setUpdatedAt(productOptionEntity.getUpdatedAt())
                    .setUpdatedBy(productOptionEntity.getUpdatedBy())
                    .setProductCid(productOptionEntity.getProductCid());

            productOptionDtos.add(productOptionDto);
        }
        return productOptionDtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductOption 내용을 한개 등록한다.
     * @param productOptionGetDto
     * @param userId
     * @see ProductOptionRepository
     */
    // TODO : create option을 할때 product의 cid 데이터가 있어야 하므로, product Cid 값도 같이 넘겨줘서 작업해야합니다.
    public void createOne(ProductOptionGetDto productOptionGetDto, UUID userId){
        ProductOptionEntity entity = convEntitiyByDto(productOptionGetDto, userId);
        productOptionRepository.save(entity);
    }

    // TODO (NEW): createList 작성하는 코드, 한번 훑어보고 ProductService : createOne 에 접목시킬수 있는방법을 생각해보면 좋을것 같습니다.
    // ProductService : createPAO2 코드에 접목 시켜놓았으니 확인바랍니다.
    public void createList(List<ProductOptionGetDto> productOptionGetDtos, UUID userId, Integer productCid){
        List<ProductOptionEntity> entities = new ArrayList<>();

        for(ProductOptionGetDto dto : productOptionGetDtos){
            ProductOptionEntity entity = convEntitiyByDto2(dto, userId, productCid);
            entities.add(entity);
        }

        productOptionRepository.saveAll(entities);
        
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductOptionGetDto => ProductOptionEntity
     * @param productOptionDto
     * @param userId
     * @return ProductOptionEntity
     */
    // TODO : productCid는 외부 인자이므로 인자로 받아서 ProductOptionEntity를 작성해야합니다. convEntityByDto2 를 참고 하세요~
    private ProductOptionEntity convEntitiyByDto(ProductOptionGetDto productOptionDto, UUID userId){
        ProductOptionEntity productOptionEntity = new ProductOptionEntity();

        productOptionEntity.setId(UUID.randomUUID())
                .setDefaultName(productOptionDto.getDefaultName())
                .setManagementName(productOptionDto.getManagementName())
                .setSalesPrice(productOptionDto.getSalesPrice())
                .setStockUnit(productOptionDto.getStockUnit())
                .setStatus(productOptionDto.getStatus())
                .setMemo(productOptionDto.getMemo())
                .setCreatedAt(dateHandler.getCurrentDate())
                .setCreatedBy(userRepository.findById(userId).get().getName())
                .setUpdatedAt(dateHandler.getCurrentDate())
                .setUpdatedBy(userRepository.findById(userId).get().getName())
                .setProductCid(productOptionDto.getProductCid());

        return productOptionEntity;
    }

    // TODO (NEW) : 
    private ProductOptionEntity convEntitiyByDto2(ProductOptionGetDto productOptionDto, UUID userId, Integer productCid){
        ProductOptionEntity productOptionEntity = new ProductOptionEntity();

        productOptionEntity.setId(UUID.randomUUID())
                .setDefaultName(productOptionDto.getDefaultName())
                .setManagementName(productOptionDto.getManagementName())
                .setSalesPrice(productOptionDto.getSalesPrice())
                .setStockUnit(productOptionDto.getStockUnit())
                .setStatus(productOptionDto.getStatus())
                .setMemo(productOptionDto.getMemo())
                .setCreatedAt(dateHandler.getCurrentDate())
                .setCreatedBy(userRepository.findById(userId).get().getName())
                .setUpdatedAt(dateHandler.getCurrentDate())
                .setUpdatedBy(userRepository.findById(userId).get().getName())
                .setProductCid(productCid);

        return productOptionEntity;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption id 값과 상응되는 데이터를 삭제한다.
     * @param productOptionId
     * @see ProductOptionRepository
     */
    public void destroyOne(Integer productOptionId){
        productOptionRepository.findById(productOptionId).ifPresent(productOption-> {
            productOptionRepository.delete(productOption);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 업데이트한다.
     * @param productOptionDto
     * @param userId
     */
    public void changeOne(ProductOptionGetDto productOptionDto, UUID userId){
        productOptionRepository.findById(productOptionDto.getCid()).ifPresentOrElse(productOptionEntity -> {
            productOptionEntity.setDefaultName(productOptionDto.getDefaultName())
                    .setManagementName(productOptionDto.getManagementName())
                    .setSalesPrice(productOptionDto.getSalesPrice())
                    .setStockUnit(productOptionDto.getStockUnit())
                    .setStatus(productOptionDto.getStatus())
                    .setMemo(productOptionDto.getMemo())
                    .setUpdatedAt(dateHandler.getCurrentDate())
                    .setUpdatedBy(userRepository.findById(userId).get().getName())
                    .setProductCid(productOptionDto.getProductCid());

            productOptionRepository.save(productOptionEntity);
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductOption id 값과 상응되는 데이터의 일부분을 업데이트한다.
     * @param productOptionDto
     * @param userId
     */
    public void patchOne(ProductOptionGetDto productOptionDto, UUID userId){
        productOptionRepository.findById(productOptionDto.getCid()).ifPresentOrElse(productOptionEntity -> {
            if(productOptionDto.getDefaultName() != null){
                productOptionEntity.setDefaultName(productOptionDto.getDefaultName());
            }
            else if(productOptionDto.getManagementName() != null) {
                productOptionEntity.setManagementName(productOptionDto.getManagementName());
            }
            else if(productOptionDto.getSalesPrice() != null) {
                productOptionEntity.setSalesPrice(productOptionDto.getSalesPrice());
            }
            else if(productOptionDto.getStockUnit() != null) {
                productOptionEntity.setStockUnit(productOptionDto.getStockUnit());
            }
            else if(productOptionDto.getStatus() != null) {
                productOptionEntity.setStatus(productOptionDto.getStatus());
            }
            else if(productOptionDto.getMemo() != null) {
                productOptionEntity.setMemo(productOptionDto.getMemo());
            }
            else if(productOptionDto.getProductCid() != null) {
                productOptionEntity.setProductCid(productOptionDto.getProductCid());
            }

            productOptionEntity.setUpdatedAt(dateHandler.getCurrentDate())
                               .setUpdatedBy(userRepository.findById(userId).get().getName());

            productOptionRepository.save(productOptionEntity);
        }, null);
    }
}
