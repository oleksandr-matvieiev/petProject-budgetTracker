package com.example.petprojectbudgettracker.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BudgetTrackerBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

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
            String response = handleCommand(messageText);
            sendMessage(chatId, response);
        }
    }

    private String handleCommand(String command) {
        if (command.equals("/transactions")) {
            return fetchTransactions();
        } else if (command.startsWith("/transaction ")) {
            String[] parts = command.split(" ");
            if (parts.length > 1) {
                return fetchTransactionById(parts[1]);
            }
        }
        return "Invalid command";
    }

    private String fetchTransactions() {
        String url = "http://localhost:8080/api/transaction/get-all-transactions";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    private String fetchTransactionById(String id) {
        String url = "http://localhost:8080/api/transaction/get-transaction-by-id?id=" + id;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
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
