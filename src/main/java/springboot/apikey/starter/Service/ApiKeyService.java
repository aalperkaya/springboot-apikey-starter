package springboot.apikey.starter.Service;

import org.springframework.stereotype.Service;
import springboot.apikey.starter.Model.ApiKey;
import springboot.apikey.starter.Repository.ApiKeyRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public List<ApiKey> getAllApiKeys() {
        return apiKeyRepository.findAll();
    }

    public Optional<ApiKey> findById(UUID id) {
        return apiKeyRepository.findById(id);
    }

    public Optional<ApiKey> getApiKey(String key) {
        return apiKeyRepository.findByKeyValue(key);
    }
    
    public ApiKey createApiKey(ApiKey apiKey) {
        return apiKeyRepository.save(apiKey);
    }

    public void deleteApiKey(UUID id) {
        apiKeyRepository.deleteById(id);
    }
    
    public long count() {
        return apiKeyRepository.count();
    }
}
