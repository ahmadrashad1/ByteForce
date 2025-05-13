package com.SportSync2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsService {

    @Value("${newsapi.key}")
    private String apiKey;

    private final String BASE_URL = "https://newsapi.org/v2/top-headlines?category=sports&language=en&apiKey=";

    public String getSportsNews() {
        RestTemplate restTemplate = new RestTemplate();
        String fullUrl = BASE_URL + apiKey;

        return restTemplate.getForObject(fullUrl, String.class);
    }
}
