package com.example.petprojectbudgettracker.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotInitializer {
    public BotInitializer(TelegramBotsApi telegramBotsApi, BudgetTrackerBot budgetTrackerBot) {
        try {
            telegramBotsApi.registerBot(budgetTrackerBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
