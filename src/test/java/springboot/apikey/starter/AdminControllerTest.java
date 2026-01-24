package springboot.apikey.starter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void adminAccessWithAdminKey() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin").header("x-api-key", "adminkey"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Admin access granted!")));
    }

    @Test
    public void adminAccessDeniedWithUserKey() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin").header("x-api-key", "testapikey"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void adminAccessDeniedWithInvalidKey() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin").header("x-api-key", "invalid"))
                .andExpect(status().isForbidden());
    }
}
