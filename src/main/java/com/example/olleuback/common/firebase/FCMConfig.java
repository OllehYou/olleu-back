package com.example.olleuback.common.firebase;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FCMConfig {

	@Value("${spring.firebase.configuration-file}")
	private String googleApplicationCredentials;

	@PostConstruct
	public void initialize() {
		try{
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(
					new ClassPathResource(googleApplicationCredentials).getInputStream()))
				.setProjectId("olleu-d5a93")
				.build();
			if(FirebaseApp.getApps().isEmpty()){
				FirebaseApp.initializeApp(options);
				log.info("Firebase application has been initialized");
			}
		} catch (IOException e){
			log.info("firebase initialize error : {}", e.getMessage());
		}
	}
}
