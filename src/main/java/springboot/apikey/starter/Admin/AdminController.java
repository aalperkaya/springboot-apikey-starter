package springboot.apikey.starter.Admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.apikey.starter.Model.ApiKey;
import springboot.apikey.starter.Service.ApiKeyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ApiKeyService apiKeyService;

    public AdminController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @GetMapping
    public String admin() {
        return "Admin access granted!";
    }

    @GetMapping("/api-keys")
    public List<ApiKey> getAllApiKeys() {
        return apiKeyService.getAllApiKeys();
    }

    @GetMapping("/api-keys/{id}")
    public ResponseEntity<ApiKey> getApiKeyById(@PathVariable UUID id) {
        return apiKeyService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api-keys")
    public ApiKey createApiKey(@RequestBody ApiKey apiKey) {
        if (apiKey.getKeyValue() == null || apiKey.getKeyValue().isEmpty()) {
            apiKey.setKeyValue(UUID.randomUUID().toString());
        }
        return apiKeyService.createApiKey(apiKey);
    }

    @PostMapping("/api-keys/{id}/revoke")
    public ResponseEntity<ApiKey> revokeApiKey(@PathVariable UUID id) {
        return apiKeyService.findById(id)
                .map(apiKey -> {
                    apiKey.setActive(false);
                    return ResponseEntity.ok(apiKeyService.createApiKey(apiKey));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api-keys/{id}")
    public ResponseEntity<Void> deleteApiKey(@PathVariable UUID id) {
        if (apiKeyService.findById(id).isPresent()) {
            apiKeyService.deleteApiKey(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
