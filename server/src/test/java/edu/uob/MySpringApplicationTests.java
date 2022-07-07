package edu.uob;

//Some tests adapted from link:
//https://www.javainuse.com/spring/springboot_testcases

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	public void testPutFixedShift() throws Exception {
//		int id = 1001;
		//todo more vars needed
		mockMvc.perform(put("/account/{1001}/fixedshift")).andExpect(status().isOk());
	}

}
