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

	// Cannot provide email
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

	private void checkLevel(int accountId, int expectedLevel) throws Exception {
		ResultActions response = mockMvc.perform(get("/account/" + accountId)
				.header("token", validToken)
		);
		response.andExpect(status().isOk());
		JsonNode account = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		assertEquals(expectedLevel, account.get("level").asInt(), "Should be level " + expectedLevel);
	}

	private int getAccountId(String username) throws Exception {
		ResultActions response = mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		);
		response.andExpect(status().isOk());
		JsonNode login = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		return login.get("accountId").asInt();
	}

	private int getAnnualLeave(int accountId) throws Exception {
		// Check annual leave remaining
		ResultActions response = mockMvc.perform(get("/leaves")
				.header("token", validToken).queryParam("accountId", String.valueOf(accountId))
		);
		response.andExpect(status().isOk());
		JsonNode leaves = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		return leaves.get("annualLeave").asInt();
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
		int accountId = getAccountId(username);
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

	@Test
	public void testChangePassword() throws Exception {
		// Create account
		String username = createAccount();
		// Login like normal
		mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		).andExpect(status().isOk());
		// Change password
		int accountId = getAccountId(username);
		String newPassword = TestTools.getRandomUsername();
		mockMvc.perform(patch("/password")
				.header("token", validToken).header("oldPassword", defaultPassword)
				.header("newPassword", newPassword).queryParam("accountId", String.valueOf(accountId))
		).andExpect(status().isOk());
		// Login with new password
		mockMvc.perform(get("/login")
				.header("password", newPassword).queryParam("username", username)
		).andExpect(status().isOk());
		// Delete account
		deleteAccount(accountId, username);
	}

	@Test
	public void testResetPassword() throws Exception {
		// Create username + email
		String username = TestTools.getRandomUsername();
		String email = TestTools.getRandomEmail();
		// Create account (not using method as need to specify the email)
		mockMvc.perform(post("/account")
				.header("token", validToken).queryParam("username", username)
				.queryParam("email", email)
		).andExpect(status().isOk());
		// Change password
		int accountId = getAccountId(username);
		String newPassword = TestTools.getRandomUsername();
		mockMvc.perform(patch("/password")
				.header("token", validToken).header("oldPassword", defaultPassword)
				.header("newPassword", newPassword).queryParam("accountId", String.valueOf(accountId))
		).andExpect(status().isOk());
		// Try logging in with old password
		mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		).andExpect(status().isConflict());
		// Reset password
		mockMvc.perform(patch("/passwordreset")
				.queryParam("username", username).queryParam("email", email)
		).andExpect(status().isOk());
		// Login in with old password
		mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		).andExpect(status().isOk());
		// Delete account
		deleteAccount(accountId, username);
	}

	@Test
	public void testLeavesUpdate() throws Exception {
		// Create account
		String username = createAccount();
		// Get accountId
		int accountId = getAccountId(username);
		// Check annual leave remaining
		int annualLeave1 = getAnnualLeave(accountId);
		// Add leave request
		mockMvc.perform(post("/request/leave")
				.header("token", validToken).queryParam("accountId", String.valueOf(accountId))
				.queryParam("date", "2022-11-01").queryParam("type", "0")
				.queryParam("length", "0").queryParam("note", "")
		).andExpect(status().isOk());
		// Confirm new notification
		ResultActions response = mockMvc.perform(get("/notification")
				.header("token", validToken).queryParam("accountId", String.valueOf(accountId))
		);
		response.andExpect(status().isOk());
		JsonNode notifications = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString()).get("leaveRequests");
		assertEquals(1, notifications.size(), "Should only be one notification");
		int notificationId = notifications.get(0).get("id").asInt();
		// Approve leave request
		mockMvc.perform(patch("/notification/" + notificationId)
				.header("token", validToken).queryParam("accountId", String.valueOf(accountId))
				.queryParam("status", "1")
		).andExpect(status().isOk());
		// Check annual leave remaining
		int annualLeave2 = getAnnualLeave(accountId);
		assertEquals(annualLeave1 - 1, annualLeave2, "Annual leave should have reduced by 1");
		// Delete account
		deleteAccount(accountId, username);
	}

	@Test
	public void testPartTimeLeaves() throws Exception {
		// Create account
		String username = createAccount();
		// Get accountId
		int accountId = getAccountId(username);
		// Check annual leave remaining
		int annualLeave1 = getAnnualLeave(accountId);
		// Update to part-time
		mockMvc.perform(patch("/account/" + accountId)
				.header("token", validToken).queryParam("timeWorked", "0.8")
		).andExpect(status().isOk());
		// Check annual leave remaining
		int annualLeave2 = getAnnualLeave(accountId);
		assertEquals(annualLeave1*0.8, annualLeave2, "Annual leave should now be pro-rated");
		// Delete account
		deleteAccount(accountId, username);
	}

	@Test
	public void testTokenRefreshing() throws Exception {
		// Create account
		String username = createAccount();
		// Get account id and current token
		ResultActions response = mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		);
		response.andExpect(status().isOk());
		JsonNode login1 = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		int accountId = login1.get("accountId").asInt();
		String token1 = login1.get("token").asText();
		// Logout (token will get changed)
		mockMvc.perform(patch("/logout")
				.header("token", token1)
		).andExpect(status().isOk());
		// Get new token
		response = mockMvc.perform(get("/login")
				.header("password", defaultPassword).queryParam("username", username)
		);
		response.andExpect(status().isOk());
		JsonNode login2 = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		String token2 = login2.get("token").asText();
		// Compare tokens
		assertNotEquals(token1, token2, "Tokens should be different");
		// Check new token is valid
		mockMvc.perform(get("/notification")
				.header("token", token1).queryParam("accountId", String.valueOf(accountId))
		).andExpect(status().isUnauthorized());
		// Check old token is not valid
		mockMvc.perform(get("/notification")
				.header("token", token2).queryParam("accountId", String.valueOf(accountId))
		).andExpect(status().isOk());
		// Delete account
		deleteAccount(accountId, username);
	}

	@Test
	public void testAddVariousAccountDetails() throws Exception {
		// Create account
		String username = createAccount();
		// Get accountId
		int accountId = getAccountId(username);
		// Change email
		String email = TestTools.getRandomEmail();
		mockMvc.perform(patch("/account/" + accountId)
				.header("token", validToken).queryParam("email", email)
				.queryParam("fixedWorking", "true")
		).andExpect(status().isOk());
		// Add working days
		mockMvc.perform(put("/account/" + accountId + "/workingdays")
				.header("token", validToken).queryParam("monday", "true")
				.queryParam("tuesday", "true").queryParam("wednesday", "false")
				.queryParam("thursday", "true").queryParam("friday", "false")
				.queryParam("saturday", "true").queryParam("sunday", "true")
		).andExpect(status().isOk());
		// Add fixed shift
		mockMvc.perform(put("/account/" + accountId + "/fixedshift")
				.header("token", validToken).queryParam("date", "1999-09-09")
				.queryParam("shiftType", "2")
		).andExpect(status().isOk());
		// Add rota type dates
		mockMvc.perform(put("/account/" + accountId + "/rotatype")
				.header("token", validToken).queryParam("startDate", "1999-09-09")
				.queryParam("endDate", "2001-01-01").queryParam("rotaTypeId", "3")
		).andExpect(status().isOk());
		mockMvc.perform(put("/account/" + accountId + "/rotatype")
				.header("token", validToken).queryParam("startDate", "2000-01-01")
				.queryParam("endDate", "2002-02-02").queryParam("rotaTypeId", "4")
		).andExpect(status().isOk());
		// Check all details
		ResultActions response = mockMvc.perform(get("/account/" + accountId)
				.header("token", validToken)
		);
		response.andExpect(status().isOk());
		JsonNode account = new ObjectMapper().readTree(response.andReturn().getResponse().getContentAsString());
		assertEquals(email, account.get("email").asText(), "Emails should match");
		assertTrue(account.get("partTimeDetails").get("monday").asBoolean(), "Does work on Monday");
		assertTrue(account.get("partTimeDetails").get("tuesday").asBoolean(), "Does work on Tuesday");
		assertFalse(account.get("partTimeDetails").get("wednesday").asBoolean(), "Doesn't work on Wednesday");
		assertTrue(account.get("partTimeDetails").get("thursday").asBoolean(), "Does work on Thursday");
		assertFalse(account.get("partTimeDetails").get("friday").asBoolean(), "Doesn't work on Friday");
		assertTrue(account.get("partTimeDetails").get("saturday").asBoolean(), "Does work on Saturday");
		assertTrue(account.get("partTimeDetails").get("sunday").asBoolean(), "Does work on Sunday");
		assertEquals(1, account.get("fixedRotaShifts").size(), "");
		assertEquals(2, account.get("accountRotaTypes").size(), "");
		// Delete account
		deleteAccount(accountId, username);
	}
}
