package edu.uob;

//Some tests adapted from link:
//https://www.javainuse.com/spring/springboot_testcases

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySpringApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	private static String validToken;
	private static String defaultPassword;

	@BeforeClass
	public static void setup1() {
		validToken = ConnectionTools.getValidToken();
		defaultPassword = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");
	}

	@Before
	public void setup2() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testRoot() throws Exception {
		// Check the root url returns OK status code (server running)
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

	@Test
	public void testDbConnection() throws Exception {
		// Check the /test url returns ok, so the server can connect to the database
		mockMvc.perform(get("/test")).andExpect(status().isOk());
	}

	@Test
	public void testInvalidToken() throws Exception {
		// Get random token, that will be invalid
		String randomToken = Encryption.getRandomToken();
		// Other random values to use
		String accountId = TestTools.getTestAccountId()+"";
		String notificationId = TestTools.getTestAccountId()+"";
		String username = TestTools.getRandomUsername();
		String password1 = RandomStringUtils.randomAlphanumeric(12);
		String password2 = RandomStringUtils.randomAlphanumeric(12);
		// Test put working days
		mockMvc.perform(put("/account/" + accountId + "/workingdays")
				.header("token", randomToken).queryParam("monday","true")
				.queryParam("tuesday","true").queryParam("wednesday","true")
				.queryParam("thursday","true").queryParam("friday","true")
				.queryParam("saturday","true").queryParam("sunday","true")
		).andExpect(status().isUnauthorized());
		// Test get notification
		mockMvc.perform(get("/notification")
				.header("token", randomToken).queryParam("accountId",accountId)
		).andExpect(status().isUnauthorized());
		// Test get leaves
		mockMvc.perform(get("/leaves")
				.header("token", randomToken).queryParam("accountId",accountId)
		).andExpect(status().isUnauthorized());
		// Test delete account
		mockMvc.perform(delete("/account/"+accountId)
				.header("token", randomToken)
		).andExpect(status().isUnauthorized());
		// Test parch account
		mockMvc.perform(patch("/account/"+accountId)
				.header("token", randomToken).queryParam("annualLeave","25")
		).andExpect(status().isUnauthorized());
		// Test get account
		mockMvc.perform(get("/account/"+accountId)
				.header("token", randomToken)
		).andExpect(status().isUnauthorized());
		// Test get all accounts
		mockMvc.perform(get("/account")
				.header("token", randomToken)
		).andExpect(status().isUnauthorized());
		// Test patch notification
		mockMvc.perform(patch("/notification/"+notificationId)
				.header("token", randomToken).queryParam("accountId",accountId)
				.queryParam("status","2")
		).andExpect(status().isUnauthorized());
		// Test post request leave
		mockMvc.perform(post("/request/leave")
				.header("token", randomToken).queryParam("accountId",accountId)
				.queryParam("date","2022-01-02").queryParam("type","1")
				.queryParam("length","0").queryParam("note","Note about leave request")
		).andExpect(status().isUnauthorized());
		// Test put fixed shift
		mockMvc.perform(put("/account/"+accountId+"/fixedshift")
				.header("token", randomToken).queryParam("date","2022-01-03")
				.queryParam("shiftType","2")
		).andExpect(status().isUnauthorized());
		// Test get shifts for year
		mockMvc.perform(get("/shift/2022")
				.header("token", randomToken).queryParam("accountId",accountId)
		).andExpect(status().isUnauthorized());
		// Test put rota build
		mockMvc.perform(put("/rotabuild")
				.header("token", randomToken)
		).andExpect(status().isUnauthorized());
		// Test post new account
		mockMvc.perform(post("/account")
				.header("token", randomToken).queryParam("username",username)
		).andExpect(status().isUnauthorized());
		// Test patch password
		mockMvc.perform(patch("/password")
				.header("token", randomToken).header("oldPassword",password1)
				.header("newPassword",password2).queryParam("accountId",accountId)
		).andExpect(status().isUnauthorized());
		// Test patch logout
		mockMvc.perform(patch("/logout")
				.header("token", randomToken)
		).andExpect(status().isUnauthorized());
	}

	private String createAccount() throws Exception {
		// Generate username
		String username = TestTools.getRandomUsername();
		// Create account
		mockMvc.perform(post("/account")
				.header("token", validToken).queryParam("username",username)
		).andExpect(status().isOk());
		return username;
	}

	private void deleteAccount(int accountId, String username) throws Exception {
		// Delete account
		mockMvc.perform(delete("/account/" + accountId)
				.header("token", validToken)
		).andExpect(status().isOk());
		// Check delete account
		mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		).andExpect(status().isConflict());
	}

	private void deleteAccount(String username) throws Exception {
		// Get account id
		ResultActions response = mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		);
		response.andExpect(status().isOk());
		JsonNode login = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		int accountId = login.get("accountId").asInt();
		// Use main delete account method
		deleteAccount(accountId, username);
	}

	private void checkLevel(int accountId, int expectedLevel) throws Exception {
		ResultActions response = mockMvc.perform(get("/account/" + accountId)
				.header("token", validToken)
		);
		response.andExpect(status().isOk());
		JsonNode account = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		assertEquals(expectedLevel, account.get("level").asInt(), "Should be level " + expectedLevel);
	}

	@Test
	public void testAccountCreation() throws Exception {
		// Create account
		String username = createAccount();
		// Check account exists
		ResultActions response = mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		);
		response.andExpect(status().isOk());
		JsonNode login = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		int accountId = login.get("accountId").asInt();
		// Delete account
		deleteAccount(accountId, username);
	}

	@Test
	public void testAdminCreation() throws Exception {
		// Create account
		String username = createAccount();
		// Get accountId
		ResultActions response = mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		);
		response.andExpect(status().isOk());
		JsonNode login = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		int accountId = login.get("accountId").asInt();
		// New account have standard privileges (level 0)
		checkLevel(accountId, 0);
		// Change level to 1
		mockMvc.perform(patch("/account/" + accountId)
				.header("token", validToken).queryParam("level", "1")
		).andExpect(status().isOk());
		// Check level change
		checkLevel(accountId, 1);
		// Delete account
		deleteAccount(accountId, username);
	}

}
