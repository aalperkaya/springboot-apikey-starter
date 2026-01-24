package springboot.apikey.starter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTest {
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void getGreeting() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/greeting").header("x-api-key", "testapikey").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello,")));
	}

	@Test
	public void getGreetingWithAdminKey() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/greeting").header("x-api-key", "adminkey").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello,")));
	}
	
	@Test
	public void failGreeting() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/greeting").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	
	@Test
	public void badCredentials() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/greeting").header("x-api-key", "X").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}	
	

}
