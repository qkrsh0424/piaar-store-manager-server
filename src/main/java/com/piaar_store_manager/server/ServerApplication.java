package com.piaar_store_manager.server;

import com.piaar_store_manager.server.property.FileUploadProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableConfigurationProperties({FileUploadProperties.class})
@SpringBootApplication
public class ServerApplication {
//	@PostConstruct
//	void started() {
//		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
//	}
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
