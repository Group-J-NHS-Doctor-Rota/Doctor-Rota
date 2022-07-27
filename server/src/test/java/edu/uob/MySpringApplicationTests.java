package edu.uob;

//Some tests adapted from link:
//https://www.javainuse.com/spring/springboot_testcases

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

	//TODO remove this test once skeleton requests have been completed
	@Test
	public void testSkeletonRequests() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		// Check rotabuild works without any variables
		mockMvc.perform(put("/rotabuild")).andExpect(status().isOk());
		// Check login needs a request parameter and a header variable
		mockMvc.perform(get("/login?username=asdfasdf").header("password", "myPassword")).andExpect(status().isOk());
		// Check login returns three parameters in JSON format
		MvcResult result = mockMvc.perform(get("/login?username=5dgsdf4t").header("password", "myPassword")).andReturn();
		JsonNode node = mapper.readTree(result.getResponse().getContentAsString());
		assertTrue(node.has("token"));
		assertTrue(node.has("accountId"));
		assertTrue(node.has("level"));
		// Check password needs a request parameter and two header variables
		mockMvc.perform(patch("/password?accountId=1").header("oldPassword", "myPassword")
				.header("newPassword", "myPassword2")).andExpect(status().isOk());
		// Check password needs two different values for the header variables
		mockMvc.perform(patch("/password?accountId=2").header("oldPassword", "myPassword")
				.header("newPassword", "myPassword")).andExpect(status().isConflict());
		// Check passwordReset needs two request parameters
		mockMvc.perform(patch("/passwordReset?username=myUsername&email=test@test.com")).andExpect(status().isOk());
		// Check logout works without any variables
		mockMvc.perform(patch("/logout")).andExpect(status().isOk());
	}

}
