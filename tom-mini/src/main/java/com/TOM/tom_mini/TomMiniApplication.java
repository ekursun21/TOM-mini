package com.TOM.tom_mini;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class TomMiniApplication {
	private static final Logger logger = LoggerFactory.getLogger(TomMiniApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TomMiniApplication.class, args);
		logger.info("Hey Tom Mini Application Started");
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		logger.info("Application is fully ready and running!");
	}
}
