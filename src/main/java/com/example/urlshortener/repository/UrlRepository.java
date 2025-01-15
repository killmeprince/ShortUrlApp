package com.example.urlshortener.repository;

import com.example.urlshortener.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url,Long> {
    Url findByShortUrl(String url);
    List<Url> findByUserId(UUID userId);
    void deleteAllByExpiryAtBefore(LocalDateTime expiryTime);

}
