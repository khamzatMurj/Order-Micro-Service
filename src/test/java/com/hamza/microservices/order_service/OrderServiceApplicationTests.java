package com.hamza.microservices.order_service;

import com.hamza.microservices.order_service.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0) //ask spring to use random port to avoid conflicts
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
		//		la configuration du stab
		InventoryClientStub.stubInventoryCall("iphone_15", 50);




		String requestBody = """
				{
				   
				    "skuCode": "iphone_15",
				    "quantity": 80
				}
				""";
		String submitOrderJson = """
				{
				"skuCode": "iphone_15",
				"price": 1000,
				"quantity": 50
                }
				""";
//		InventoryClientStub.stubInventory("iphone_15", 80);
//		RestAssured
//				.given()
//				.contentType("application/json")
//				.body(requestBody)
//				.when()
//				.post("/api/order")
//				.then()
//				.statusCode(201)
//				.body(equalTo("Order created successfully"));



		RestAssured.given()
					.contentType("application/json")
					.body(submitOrderJson)
					.when()
					.post("/api/order")
					.then()
					.log().all();
	//				.statusCode(500);
		}

}
