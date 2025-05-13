package com.SportSync2.controller;

import com.SportSync2.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/sports")
    public String getSportsNews() {
        return newsService.getSportsNews(); // You can return a DTO instead of raw JSON if you want
    }
}
