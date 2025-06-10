package com.example.CurrencyService;

import io.github.cdimascio.dotenv.Dotenv;

public class ApiKeyProvider {
    public static String getApiKey() {
        Dotenv dotenv = Dotenv.configure().filename("currency.api.env").load();
        return dotenv.get("API");
    }
}
