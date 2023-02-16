package com.example.poker_tg_bot.utility;

import com.example.poker_tg_bot.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramUtil {

    public static SendMessage createMessageTemplate(User user) {
        return createMessageTemplate(String.valueOf(user.getChatId()));
    }

    // Создаем шаблон SendMessage с включенным Markdown
    public static SendMessage createMessageTemplate(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        return sendMessage;
    }

    // Создаем кнопку
    public static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(command);

        return inlineKeyboardButton;
    }
}
