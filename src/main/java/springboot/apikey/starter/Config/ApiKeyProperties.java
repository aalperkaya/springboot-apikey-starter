package springboot.apikey.starter.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "springboot.apikey")
public class ApiKeyProperties {

    private String headerName;
    private List<KeyConfig> keys = new ArrayList<>();

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public List<KeyConfig> getKeys() {
        return keys;
    }

    public void setKeys(List<KeyConfig> keys) {
        this.keys = keys;
    }

    public static class KeyConfig {
        private String value;
        private String client;
        private List<String> roles = new ArrayList<>();

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
}
