package com.example.urlshortener.controllers;

import com.example.urlshortener.dto.UrlRequest;
import com.example.urlshortener.dto.UrlResponse;

import com.example.urlshortener.services.UrlService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ShortUrlController {
    private final UrlService urlService;

    public ShortUrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Создать короткую ссылку.
     * Принимает оригинальный URL и возвращает уникальную короткую ссылку.
     * Если пользователь не передал UUID, создается новый.
     */
    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody UrlRequest request) {
        return ResponseEntity.ok(urlService.shortenUrl(request));
    }
    /**
     * Получить информацию о ссылке
     * Этот эндпоинт возвращает оригинальный URL, дату истечения и другую информацию о ссылке.
     */
    @GetMapping("/info/{shortUrl}")
    public ResponseEntity<UrlResponse> getOriginalUrl(@PathVariable String shortUrl) {
        UrlResponse response = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.ok(response);
    }
    /**
     * Получит все ссылки, созданные пользователем
     * На основе UUID пользователя возвращает список всех его коротких ссылок
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UrlResponse>> getUserUrls(@PathVariable UUID userId) {

        List<UrlResponse> responses = urlService.getAllUrlsByUserId(userId);
        return ResponseEntity.ok(responses);
    }
    /**
     * перенаправление по короткой ссылке.
     * Этот эндпоинт позволяет перейти на оригинальный URL, используя короткую ссылку.
     * Если ссылка недействительна (истек срок или превышен лимит кликов), выбрасывается ошибка
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectionToOriginalUrl(@PathVariable String shortUrl) {
        UrlResponse response = urlService.getOriginalUrl(shortUrl);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(response.getOriginalUrl()))
                .build();
    }
    /**
     * Обновить лимит кликов для ссылки.
     * Пользователь может задать новый лимит кликов для конкретной короткой ссылки
     */
    @PutMapping("/updateLimit/{shortUrl}")
    public ResponseEntity<String> updateMaxClicks(@PathVariable String shortUrl,@RequestParam int clientClicksLimit) {
        urlService.updateMaxClicks(shortUrl,clientClicksLimit);
        return ResponseEntity.ok("Updated max clicks limit to " + clientClicksLimit);
    }
    /**
     * Удалит короткую ссылку
     * Пользователь может удалить ссылку, если он является её создателем
     * Проверяется UUID пользователя для авторизации
     */
    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<String> deleteLink(@PathVariable String shortUrl, @RequestParam UUID userId) {
        urlService.deleteLink(shortUrl,userId);
        return ResponseEntity.ok("Deleted link " + shortUrl);
    }
}
