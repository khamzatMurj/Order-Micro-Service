package com.hamza.microservices.order_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsRestAssuredConfigurationCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.hamcrest.Matchers.equalTo;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Container
	private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
			.withDatabaseName("testdb")
			.withUsername("user")
			.withPassword("user");

	static {
		mysqlContainer.start();
	}

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}
	@Test
	void contextLoads() {
		String requestBody = """
				{
				   
				    "skuCode": "iphone_15",
				    "quantity": 15
				}
				""";
		RestAssured
				.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/order")
				.then()
				.statusCode(201)
				.body(equalTo("Order created successfully"));
	}

}
