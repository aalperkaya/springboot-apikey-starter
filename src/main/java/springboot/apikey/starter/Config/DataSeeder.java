package springboot.apikey.starter.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import springboot.apikey.starter.Model.ApiKey;
import springboot.apikey.starter.Service.ApiKeyService;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    
    private final ApiKeyService apiKeyService;
    private final ApiKeyProperties apiKeyProperties;

    public DataSeeder(ApiKeyService apiKeyService, ApiKeyProperties apiKeyProperties) {
        this.apiKeyService = apiKeyService;
        this.apiKeyProperties = apiKeyProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        if (apiKeyService.count() == 0) {
            log.info("No API keys found in database. Seeding from properties...");
            for (ApiKeyProperties.KeyConfig keyConfig : apiKeyProperties.getKeys()) {
                ApiKey apiKey = new ApiKey(
                        keyConfig.getValue(),
                        keyConfig.getClient(),
                        keyConfig.getRoles(),
                        true
                );
                apiKeyService.createApiKey(apiKey);
                log.info("Seeded API key for client: {}", keyConfig.getClient());
            }
        } else {
            log.info("API keys found in database. Skipping seeding.");
        }
    }
}
