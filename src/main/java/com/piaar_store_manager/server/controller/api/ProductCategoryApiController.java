package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.product_category.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-category")
public class ProductCategoryApiController {

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     *
     * Search list api for productCategory.
     * <p>
     * <b>GET : API URL => /api/v1/product-category/list</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductCategoryService#searchList
     */
    // TODO : Category 기본 메소드 명세 작성
    @GetMapping("/list")
    public ResponseEntity<?> searchList(){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(productCategoryService.searchList());
        return new ResponseEntity<>(message, message.getStatus());
    }

    // test
    @GetMapping("/test/{productId}")
    public ResponseEntity<?> updateJoinEntity(@PathVariable(value="productId") Integer productId) {
        Message message = new Message();

        try{
            message.setData(productCategoryService.searchJoinOne(productId));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("No Data");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }
}
