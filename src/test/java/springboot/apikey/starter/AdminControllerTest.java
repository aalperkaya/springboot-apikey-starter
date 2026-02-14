package springboot.apikey.starter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import springboot.apikey.starter.Model.ApiKey;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void listApiKeys() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/api-keys").header("x-api-key", "adminkey"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void createAndRevokeApiKey() throws Exception {
        ApiKey newKey = new ApiKey(null, "New Client", List.of("ROLE_USER"), true);
        
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/admin/api-keys")
                .header("x-api-key", "adminkey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newKey)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("New Client"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.keyValue").exists())
                .andReturn();

        ApiKey createdKey = objectMapper.readValue(result.getResponse().getContentAsString(), ApiKey.class);
        UUID id = createdKey.getId();

        // Revoke
        mvc.perform(MockMvcRequestBuilders.post("/admin/api-keys/" + id + "/revoke")
                .header("x-api-key", "adminkey"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        // Delete
        mvc.perform(MockMvcRequestBuilders.delete("/admin/api-keys/" + id)
                .header("x-api-key", "adminkey"))
                .andExpect(status().isNoContent());

        // Verify deleted
        mvc.perform(MockMvcRequestBuilders.get("/admin/api-keys/" + id)
                .header("x-api-key", "adminkey"))
                .andExpect(status().isNotFound());
    }
}
