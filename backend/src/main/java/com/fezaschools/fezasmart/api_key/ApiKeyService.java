package com.fezaschools.fezasmart.api_key;

import com.fezaschools.fezasmart.events.BeforeDeleteSchool;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.school.SchoolRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final SchoolRepository schoolRepository;

    public ApiKeyService(final ApiKeyRepository apiKeyRepository,
            final SchoolRepository schoolRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.schoolRepository = schoolRepository;
    }

    public List<ApiKeyDTO> findAll() {
        final List<ApiKey> apiKeys = apiKeyRepository.findAll(Sort.by("id"));
        return apiKeys.stream()
                .map(apiKey -> mapToDTO(apiKey, new ApiKeyDTO()))
                .toList();
    }

    public ApiKeyDTO get(final Integer id) {
        return apiKeyRepository.findById(id)
                .map(apiKey -> mapToDTO(apiKey, new ApiKeyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ApiKeyDTO apiKeyDTO) {
        final ApiKey apiKey = new ApiKey();
        mapToEntity(apiKeyDTO, apiKey);
        return apiKeyRepository.save(apiKey).getId();
    }

    public void update(final Integer id, final ApiKeyDTO apiKeyDTO) {
        final ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(apiKeyDTO, apiKey);
        apiKeyRepository.save(apiKey);
    }

    public void delete(final Integer id) {
        final ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        apiKeyRepository.delete(apiKey);
    }

    private ApiKeyDTO mapToDTO(final ApiKey apiKey, final ApiKeyDTO apiKeyDTO) {
        apiKeyDTO.setId(apiKey.getId());
        apiKeyDTO.setName(apiKey.getName());
        apiKeyDTO.setKeyHash(apiKey.getKeyHash());
        apiKeyDTO.setPermissions(apiKey.getPermissions());
        apiKeyDTO.setExpiresAt(apiKey.getExpiresAt());
        apiKeyDTO.setCreatedAt(apiKey.getCreatedAt());
        apiKeyDTO.setRevoked(apiKey.getRevoked());
        apiKeyDTO.setSchool(apiKey.getSchool() == null ? null : apiKey.getSchool().getId());
        return apiKeyDTO;
    }

    private ApiKey mapToEntity(final ApiKeyDTO apiKeyDTO, final ApiKey apiKey) {
        apiKey.setName(apiKeyDTO.getName());
        apiKey.setKeyHash(apiKeyDTO.getKeyHash());
        apiKey.setPermissions(apiKeyDTO.getPermissions());
        apiKey.setExpiresAt(apiKeyDTO.getExpiresAt());
        apiKey.setCreatedAt(apiKeyDTO.getCreatedAt());
        apiKey.setRevoked(apiKeyDTO.getRevoked());
        final School school = apiKeyDTO.getSchool() == null ? null : schoolRepository.findById(apiKeyDTO.getSchool())
                .orElseThrow(() -> new NotFoundException("school not found"));
        apiKey.setSchool(school);
        return apiKey;
    }

    @EventListener(BeforeDeleteSchool.class)
    public void on(final BeforeDeleteSchool event) {
        final ReferencedException referencedException = new ReferencedException();
        final ApiKey schoolApiKey = apiKeyRepository.findFirstBySchoolId(event.getId());
        if (schoolApiKey != null) {
            referencedException.setKey("school.apiKey.school.referenced");
            referencedException.addParam(schoolApiKey.getId());
            throw referencedException;
        }
    }

}
