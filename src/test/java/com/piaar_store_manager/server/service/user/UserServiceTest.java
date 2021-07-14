package com.piaar_store_manager.server.service.user;

import com.piaar_store_manager.server.model.user.dto.SignupReqDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void createOneTest(){
        SignupReqDto signupReqDto = new SignupReqDto();
        signupReqDto.setUsername("*");
        signupReqDto.setPassword("*123!@");
        signupReqDto.setName("*");

        userService.createOne(signupReqDto);
    }
}
