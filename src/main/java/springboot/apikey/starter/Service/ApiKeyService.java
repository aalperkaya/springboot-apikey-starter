package springboot.apikey.starter.Service;

import org.springframework.stereotype.Service;
import springboot.apikey.starter.Model.ApiKey;
import springboot.apikey.starter.Repository.ApiKeyRepository;

import java.util.Optional;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public Optional<ApiKey> getApiKey(String key) {
        return apiKeyRepository.findByKeyValue(key);
    }
    
    public ApiKey createApiKey(ApiKey apiKey) {
        return apiKeyRepository.save(apiKey);
    }
    
    public long count() {
        return apiKeyRepository.count();
    }
}
