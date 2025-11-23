package com.zascapay.client.service;

/**
 * Central place for API configuration. TOKEN_API and BASE_URL are read from
 * environment variables or system properties when available to make replacement easy.
 *
 * Environment variables checked (in order):
 * - TOKEN_API
 * - token_api (system property)
 *
 * - API_BASE_URL
 * - api_base_url (system property)
 */
public class ApiConfig {
    public static final String TOKEN_API;
    public static final String BASE_URL;

    static {
        String token = System.getenv("TOKEN_API");
        if (token == null || token.isEmpty()) {
            token = System.getProperty(
                    "token_api",
//                    "357dad5ab76bca006ecfac80e1536b5ef6fdd1da" // shop a
//                    "25bb999703e24af1ded4872f1227abe8aaad088f" // shop b
                    "f2c89a1b5ce46cd965049d4e44066d60bb355309" // test user
            );
        }
        TOKEN_API = token;

        String base = System.getenv("API_BASE_URL");
        if (base == null || base.isEmpty()) {
            base = System.getProperty("api_base_url", "http://localhost:8888");
        }
        BASE_URL = base.endsWith("/") ? base : base + "/";
    }

    private ApiConfig() {
        // utility class
    }
}
