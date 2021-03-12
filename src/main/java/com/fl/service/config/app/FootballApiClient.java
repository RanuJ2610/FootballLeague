package com.fl.service.config.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fl.service.exception.FootballException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * Created By techjini on Mar, 2021
 */
public class FootballApiClient {

    private final RestTemplate restTemplate;

    public FootballApiClient() {
        restTemplate = new RestTemplate();
        configureRestTemplate();
    }

    public <T> List<T> getResourceAsList(String path, Class<T> resourceClazz) throws FootballException {
        URI uri = URI.create(appendToBaseUrl(path));
        HttpHeaders headers = this.generateHttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<T> result = null;

        if (responseEntity.hasBody()) {
            try {
                result = mapper.readValue(responseEntity.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, resourceClazz));
            } catch (IOException exp) {
                throw new FootballException(responseEntity.getBody());
            }
        }

        return result;
    }

    protected void configureRestTemplate() {
        ObjectMapper mapper = getMapper();
        configureMapper(mapper);
    }

    protected void configureMapper(ObjectMapper mapper) {
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }

    protected ObjectMapper getMapper() {
        ObjectMapper objectMapper = null;
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> element : messageConverters) {
            if (element.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName())) {
                MappingJackson2HttpMessageConverter messageConverter = (MappingJackson2HttpMessageConverter) element;
                objectMapper = messageConverter.getObjectMapper();
            }
        }
        return objectMapper;
    }

    private HttpHeaders generateHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer ");
        return headers;
    }

    private String appendToBaseUrl(String path) {
        String apiKeys = "41aba4ffe7ad9d0776a1d2c531bc7b20d7034010ff97c3860b6b3217864c0e49";
        String baseUrl = "https://apiv2.apifootball.com/?APIkey=" + apiKeys + "&action=";
        if (StringUtils.isBlank(path)) {
            return baseUrl;
        }
        return baseUrl +StringUtils.trim(path);
    }
}
