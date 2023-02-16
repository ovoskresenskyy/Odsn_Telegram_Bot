package com.example.poker_tg_bot.handler;

import com.example.poker_tg_bot.model.State;
import com.example.poker_tg_bot.model.User;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

public interface Handler {

    List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message);
    State operatedBotState();
    List<String> operatedCallBackQuery();
}
