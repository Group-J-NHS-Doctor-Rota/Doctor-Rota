package edu.uob;

//Some tests adapted from link:
//https://www.javainuse.com/spring/springboot_testcases

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySpringApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
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

}
