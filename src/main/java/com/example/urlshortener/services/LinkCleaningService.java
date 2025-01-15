package com.example.urlshortener.services;

import com.example.urlshortener.repository.UrlRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LinkCleaningService {
    private final UrlRepository urlRepository;

    public LinkCleaningService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Scheduled(cron = "0 0 * * * ?") //ежечасно
    public void cleanLinks() {
        urlRepository.deleteAllByExpiryAtBefore(LocalDateTime.now());
        System.out.println("All ur expired links was deleted :(");
    }
}
