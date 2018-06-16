package com.gazorpazorp.CQRSShoppingCart;

import javax.annotation.PostConstruct;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages="com.gazorpazorp")
@EntityScan(basePackages={"com.gazorpazorp.query.model", "org.axonframework.eventsourcing.eventstore.jpa", "org.axonframework.eventhandling.saga.repository.jpa", "org.axonframework.eventhandling.tokenstore.jpa"})
@EnableJpaRepositories("com.gazorpazorp.query.repository")
public class CqrsShoppingCartApplication {
	
	@Autowired
	private CommandGateway commandGateway;
	
	@PostConstruct
	public void getDbManager(){
	   DatabaseManagerSwing.main(
		new String[] { "--url", "jdbc:hsqldb:mem:test://localhost/axontest?characterEncoding=UTF-8", "--user", "SA", "--password", ""});
	}

	public static void main(String[] args) {
		SpringApplication.run(CqrsShoppingCartApplication.class, args);
	}
}
