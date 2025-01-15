package com.example.urlshortener.services;

import com.example.urlshortener.conf.AppProperties;
import com.example.urlshortener.dto.UrlRequest;
import com.example.urlshortener.dto.UrlResponse;
import com.example.urlshortener.entities.Url;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.utils.UrlHasher;
import lombok.Getter;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Service
public class UrlService {
    private final AppProperties appProperties;
    private final UrlRepository urlRepository;
    private final ModelMapper modelMapper;


    public UrlService(AppProperties appProperties, UrlRepository urlRepository, ModelMapper modelMapper) {
        this.appProperties = appProperties;
        this.urlRepository = urlRepository;
        this.modelMapper = modelMapper;
    }

    public String shortenUrl(UrlRequest urlRequest) {
        UUID userId = urlRequest.getUserId() != null ? urlRequest.getUserId() : UUID.randomUUID();


        String shortUrl = UrlHasher.hash(urlRequest.getOriginUrl() + userId);

        //конфигурационное врмея жизни
        long configExpTime = Long.parseLong(appProperties.getDefaultExpirationTime().replace("h","").trim());

        //время жизни, заданное пользователем
        long expTimeByUser = urlRequest.getExpirationTime() != null ? Long.parseLong(urlRequest.getExpirationTime().replace("h","").trim()) : configExpTime;

        //выбор мин значения
        long choiceExpTime = Math.min(configExpTime, expTimeByUser);

        LocalDateTime expiryDate = LocalDateTime.now().plusDays(choiceExpTime);


        Url url = new Url();
        url.setOriginalUrl(urlRequest.getOriginUrl());
        url.setShortUrl(shortUrl);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiryAt(expiryDate);
        url.setMaxClicks(appProperties.getDefaultMaxClicks());
        url.setClicks(0);
        url.setUserId(userId);


        urlRepository.save(url);

        return shortUrl + "USER ID " + userId;

    }
/*
    public List<UrlResponse> getUrlsByUser(UUID userId) {
        List<Url> urls = urlRepository.findByUserId(userId);
        return urls.stream().map(url -> {
            UrlResponse response = new UrlResponse();
            response.setShortUrl(url.getShortUrl());
            response.setOriginalUrl(url.getOriginalUrl());
            response.setExpiryDate(url.getExpiryAt());
            return response;
        }).collect(Collectors.toList());
    }
*/
//Оказалось, что это довольно удобно, не надо сильно большой вариант писать.
// Но пользуюсь этим впервые.
// Выше оставлю закоменченный метод который был до этого, идея переписать его пришла из-за громозкости
    public List<UrlResponse> getAllUrlsByUserId(UUID userId) {
        return urlRepository.findByUserId(userId)
                .stream()
                .map(url -> modelMapper.map(url, UrlResponse.class))
                .collect(Collectors.toList());
    }

    public UrlResponse getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url == null) {
            throw new RuntimeException("Link was expired or no longer available");
        }

        if ( url.getClicks() >= url.getMaxClicks() || url.getExpiryAt().isBefore(LocalDateTime.now())) {
            urlRepository.delete(url);
            throw new RuntimeException("Link was expired or no longer available");
        }
        url.setClicks(url.getClicks() + 1);
        urlRepository.save(url);

        UrlResponse response = new UrlResponse();
        response.setShortUrl(shortUrl);
        response.setOriginalUrl(url.getOriginalUrl());
        response.setExpiryDate(url.getExpiryAt());

        return response;
    }
    public void updateMaxClicks(String shortUrl, int maxClicksByUser) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url == null) {
            throw new RuntimeException("URL NOT FOUND");

        }
        url.setMaxClicks(maxClicksByUser);
        urlRepository.save(url);
    }
    public void deleteLink(String shortUrl, UUID userId) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url == null || !url.getUserId().equals(userId)) {
            throw new RuntimeException("Link was not found");

        }
        urlRepository.delete(url);
    }

}
