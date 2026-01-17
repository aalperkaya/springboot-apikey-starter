package springboot.apikey.starter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class SysdateControllerTest {
	@Autowired
	private MockMvc mvc;

	@Test
	void testGetSysdate() throws Exception {
		MvcResult res1 = mvc.perform(MockMvcRequestBuilders.get("/test/sysdate1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andReturn();
		
		MvcResult res2 = mvc.perform(MockMvcRequestBuilders.get("/test/sysdate2").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotEquals(res1.getResponse().getContentAsString(),res2.getResponse().getContentAsString());
	}

}
