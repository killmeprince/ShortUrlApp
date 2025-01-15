package com.example.urlshortener.utils;
import org.apache.commons.codec.digest.DigestUtils;

public class UrlHasher {
    public static String hash(String url) {
        return DigestUtils.md2Hex(url).substring(0,8);
    }
}
