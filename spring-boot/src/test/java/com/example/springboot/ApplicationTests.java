package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {
	@Value("${DB_PORT}")
	String dbPort;

	@Value("${DB_HOST}")
	String dbHost;


	@Value("${DB_USER}")
	String dbUser;

	@Value("${DB_PASSWORD}")
	String password;
  @Autowired
  private HealthCheck healthCheck;
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testDatabaseConnection() {
		try {
			Connection connection = (Connection) DriverManager.getConnection("jdbc:mariadb://"+dbHost+":"+dbPort+"/", dbUser, password);
			assertNotNull(connection); // Assert that the connection is not null
		} catch (Exception e) {
			e.printStackTrace();
		}
}}
