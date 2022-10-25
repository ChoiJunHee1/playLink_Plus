package com.playLink_Plus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.text.SimpleDateFormat;
import java.util.Date;
@EnableJpaAuditing
@SpringBootApplication
public class PlayLinkPlusApplication {

	public static void main(String[] args) {
		System.setProperty("logging.file.name",
				"/Users/choejunhui/api/storage/logs"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".log");
		SpringApplication.run(PlayLinkPlusApplication.class, args);
	}

}
