package com.example.poker_tg_bot.handler;

import com.example.poker_tg_bot.model.State;
import com.example.poker_tg_bot.model.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.example.poker_tg_bot.handler.RegistrationHandler.NAME_CHANGE;
import static com.example.poker_tg_bot.utility.TelegramUtil.createInlineKeyboardButton;
import static com.example.poker_tg_bot.utility.TelegramUtil.createMessageTemplate;

@Component
public class HelpHandler implements Handler {

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        // Создаем кнопку для смены имени
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Change name", NAME_CHANGE));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        SendMessage messageTemplate = createMessageTemplate(user);
        messageTemplate.setText(String.format("You've asked for help %s? Here it comes!", user.getName()));
        messageTemplate.setReplyMarkup(inlineKeyboardMarkup);

        return List.of(messageTemplate);

    }

    @Override
    public State operatedBotState() {
        return State.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
