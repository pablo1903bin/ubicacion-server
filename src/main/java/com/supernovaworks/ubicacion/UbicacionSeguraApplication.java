package com.supernovaworks.ubicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableDiscoveryClient
@EnableWebSocket
public class UbicacionSeguraApplication {

	public static void main(String[] args) {
		SpringApplication.run(UbicacionSeguraApplication.class, args);
	}

}
