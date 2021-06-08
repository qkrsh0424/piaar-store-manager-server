package com.piaar_store_manager.server.config.auth;

public interface JwtExpireTimeInterface {
    final static Integer ACCESS_TOKEN_COOKIE_EXPIRATION = 5*24*60*60; // seconds | default: 5*24*60*60, 5일
    final static Integer ACCESS_TOKEN_JWT_EXPIRATION = 20*60*1000; // milliseconds | default: 20*60*1000, 20분
    final static Integer REFRESH_TOKEN_JWT_EXPIRATION = 5*24*60*60*1000; // milliseconds | default: 5*24*60*60*1000, 5일
}
