package com.spring.jwt;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.security.Security;
import java.util.*;

@SpringBootApplication
@EnableScheduling
@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
@OpenAPIDefinition(
		info = @Info(
				title = " REST API Documentation",
				description = " REST API Documentation" ,
				version = "v1",
				contact = @Contact(
						name = "A",
						email = "example@gmail.com"
				),
				license = @License(
						name = "Apache 2.0"
				)
		)
)
public class JwtWithSpringSecurityApplication {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

		SpringApplication.run(JwtWithSpringSecurityApplication.class, args);
		System.err.println("  *****    *******  *******       *****   *******    *****    ******   *******" );
		System.err.println(" *     *   *      *    *         *           *      *     *   *     *     *   " );
		System.err.println("*       *  *      *    *         *           *     *       *  *     *     *   " );
		System.err.println("*       *  *******     *          *****      *     *       *  ******      *   " );
		System.err.println("*********  *           *               *     *     *********  *   *       *   " );
		System.err.println("*       *  *           *               *     *     *       *  *    *      *   " );
		System.err.println("*       *  *        *******       *****      *     *       *  *     *     *   " );


		System.err.println("PORT : localhost8080");
		System.err.println("documentation : "+"http://localhost:8087/swagger-ui/index.html#/   ok");

		System.out.println("Git Is Working");

		System.out.println("New changes from Abhishek");


	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		System.out.println("Timezone set to IST");
	}
}
