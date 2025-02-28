package com.example.petprojectbudgettracker.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BudgetTrackerBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    private final Map<Long, String> userTokens = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate;

    @Autowired
    public BudgetTrackerBot(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();
            String response = handleCommand(messageText, Long.parseLong(chatId));
            sendMessage(chatId, response);
        }
    }

    private String handleCommand(String command, Long chatId) {
        if (command.equals("/transactions")) {
            String token = userTokens.get(chatId);
            if (token == null) {
                return "❌ You need to login first! Use /login <email> <password>";
            }
            return fetchTransactions(token);
        } else if (command.startsWith("/transaction ")) {
            String[] parts = command.split(" ");
            if (parts.length > 1) {
                String token = userTokens.get(chatId);
                if (token == null) {
                    return "❌ You need to login first! Use /login <email> <password>";
                }
                return fetchTransactionById(parts[1], token);
            }
        } else if (command.startsWith("/login")) {
            String[] parts = command.split(" ");
            if (parts.length == 3) {
                String token = authenticateUser(parts[1], parts[2]);
                if (token != null) {
                    userTokens.put(chatId, token);
                    return "✅ You logged in!";
                } else {
                    return "❌ Login failed! Check your credentials.";
                }
            }
        }
        return "❌ Invalid command!";
    }

    private String fetchTransactions(String token) {
        String url = "http://localhost:8080/api/transaction/get-all-transactions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> transactions = response.getBody();

                if (transactions.isEmpty()) {
                    return "ℹ️ You have no transactions!.";
                }

                StringBuilder result = new StringBuilder("📋 *Your transactions:*\n\n");
                for (Map<String, Object> transaction : transactions) {
                    result.append(formatTransaction(transaction)).append("\n\n");
                }
                return result.toString();
            } else {
                return "❌ Error. Status code: " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
    private String formatTransaction(Map<String, Object> transaction) {
        Long id = ((Number) transaction.get("id")).longValue();
        Double amount = ((Number) transaction.get("amount")).doubleValue();
        String type = (String) transaction.get("type");
        String category = (String) transaction.get("category");
        String description = (String) transaction.get("description");
        String date = (String) transaction.get("transactionDate");

        Map<String, Object> subCategory = (Map<String, Object>) transaction.get("subCategory");
        String subCategoryName = (subCategory != null) ? (String) subCategory.get("name") : "No";

        return String.format(
                "🆔 **ID:** %d\n💰 **Amount:** %.2f\n📂 **Type:** %s\n📌 **Category:** %s\n🔖 **Sub-category:** %s\n📝 **Desctiption:** %s\n📅 **Date:** %s",
                id, amount, type, category, subCategoryName, description, date
        );
    }

    private String fetchTransactionById(String id, String token) {
        String url = "http://localhost:8080/api/transaction/get-transaction-by-id?id=" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return response.getBody();
    }

    private String authenticateUser(String email, String password) {
        String url = "http://localhost:8080/api/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) Objects.requireNonNull(response.getBody()).get("token");
        }
        return null;
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
