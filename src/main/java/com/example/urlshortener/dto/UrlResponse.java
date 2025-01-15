package com.example.urlshortener.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UrlResponse {
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime expiryDate;
}
