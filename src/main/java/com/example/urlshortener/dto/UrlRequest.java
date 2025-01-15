package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UrlRequest {
    @NotBlank(message = "Please put ur long URL")
    private String OriginUrl;


    //идентификация для сущ. пользователя
    private UUID userId;

    //время жизни, которое передал пользователь
    private String expirationTime;
}
