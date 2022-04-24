package com.vihanga.eEducate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.File;
import static com.vihanga.eEducate.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class EEducateApplication {

	public static void main(String[] args) {
		SpringApplication.run(EEducateApplication.class, args);
		new File(USER_FOLDER).mkdirs();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
