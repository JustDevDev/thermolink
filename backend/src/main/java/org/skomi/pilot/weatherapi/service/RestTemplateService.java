package org.skomi.pilot.weatherapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestTemplateService {

    private final RestTemplate restTemplate;

    /**
     * Makes a POST request with a generic request body and returns a generic response
     *
     * @param url          The URL to send the request to
     * @param requestBody  The request body object
     * @param responseType The class type of the expected response
     * @param headers      Optional additional headers
     * @param queryParams  Optional query parameters
     * @param <T>         The type of the request body
     * @param <R>         The type of the response
     * @return The response object
     */
    public <T, R> R post(String url,
                        T requestBody,
                        Class<R> responseType,
                        HttpHeaders headers,
                        Map<String, String> queryParams) {
        
        // Build URL with query parameters if provided
        UriComponentsBuilder builder = getUriComponentsBuilder(url, queryParams);

        // Create headers if not provided
        HttpHeaders requestHeaders = getHttpHeaders(headers);

        // Create HTTP entity with headers and body
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        log.debug("Making POST request to: {}", builder.toUriString());
        log.debug("Request headers: {}", requestHeaders);
        log.debug("Request body: {}", requestBody);

        try {
            ResponseEntity<R> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            );

            log.debug("Response status: {}", response.getStatusCode());
            log.debug("Response body: {}", response.getBody());

            return response.getBody();
        } catch (Exception e) {
            log.error("Error making POST request to {}: {}", url, e.getMessage(), e);
            throw new RestClientException("Failed to make POST request", e);
        }
    }

    private HttpHeaders getHttpHeaders(HttpHeaders headers) {
        HttpHeaders requestHeaders = headers != null ? headers : new HttpHeaders();
        if (!requestHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return requestHeaders;
    }

    /**
     * Makes a POST request with a generic request body and returns a generic response
     * Uses ParameterizedTypeReference for complex generic types
     *
     * @param url          The URL to send the request to
     * @param requestBody  The request body object
     * @param responseType The ParameterizedTypeReference for the response type
     * @param headers      Optional additional headers
     * @param queryParams  Optional query parameters
     * @param <T>         The type of the request body
     * @param <R>         The type of the response
     * @return The response object
     */
    public <T, R> R post(String url,
                        T requestBody,
                        ParameterizedTypeReference<R> responseType,
                        HttpHeaders headers,
                        Map<String, String> queryParams) {

        UriComponentsBuilder builder = getUriComponentsBuilder(url, queryParams);
        HttpHeaders requestHeaders = getHttpHeaders(headers);
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        try {
            ResponseEntity<R> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Error making POST request to {}: {}", url, e.getMessage(), e);
            throw new RestClientException("Failed to make POST request", e);
        }
    }

    private UriComponentsBuilder getUriComponentsBuilder(String url, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }
        return builder;
    }

    /**
     * Makes a GET request and returns a generic response
     *
     * @param url          The URL to send the request to
     * @param responseType The class type of the expected response
     * @param headers      Optional additional headers
     * @param queryParams  Optional query parameters
     * @param <R>         The type of the response
     * @return The response object
     */
    public <R> R get(String url,
                    Class<R> responseType,
                    HttpHeaders headers,
                    Map<String, String> queryParams) {

        UriComponentsBuilder builder = getUriComponentsBuilder(url, queryParams);

        HttpHeaders requestHeaders = headers != null ? headers : new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);

        String uriString = builder.toUriString();

        try {
            ResponseEntity<R> response = restTemplate.exchange(
                    uriString,
                    HttpMethod.GET,
                    requestEntity,
                    responseType
            );

            log.info("GET request: {}", builder.toUriString());
            log.info("GET response: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error("Error making GET request to {}: {}", url, e.getMessage(), e);
            throw new RestClientException("Failed to make GET request", e);
        }
    }

    /**
     * Custom exception for REST client errors
     */
    public static class RestClientException extends RuntimeException {
        public RestClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}