package com.x64tech.meserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.x64tech.meserver.network.BCNetwork;

@SpringBootApplication
public class MeserverApplication {

	public static void main(String[] args) throws Exception {
		//BCNetwork.EnrollAdmin();
		SpringApplication.run(MeserverApplication.class, args);
	}

}
