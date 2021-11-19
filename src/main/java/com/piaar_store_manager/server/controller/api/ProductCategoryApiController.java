package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.product_category.ProductCategoryBusinessService;
import com.piaar_store_manager.server.service.product_category.ProductCategoryService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-category")
public class ProductCategoryApiController {

    @Autowired
    private ProductCategoryBusinessService productCategoryBusinessService;

    @Autowired
    private UserService userService;

    /**
     * Search list api for productCategory.
     * <p>
     * <b>GET : API URL => /api/v1/product-category/list</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductCategoryService#searchList
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList(){
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productCategoryBusinessService.searchList());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
}
