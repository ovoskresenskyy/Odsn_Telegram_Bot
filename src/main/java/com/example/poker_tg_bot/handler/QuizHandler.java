package com.example.poker_tg_bot.handler;

import com.example.poker_tg_bot.model.Question;
import com.example.poker_tg_bot.model.State;
import com.example.poker_tg_bot.model.User;
import com.example.poker_tg_bot.repository.JpaQuestionRepository;
import com.example.poker_tg_bot.repository.JpaUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.poker_tg_bot.utility.TelegramUtil.createInlineKeyboardButton;
import static com.example.poker_tg_bot.utility.TelegramUtil.createMessageTemplate;

@Component
@Slf4j
public class QuizHandler implements Handler {

    //Храним поддерживаемые CallBackQuery в виде констант
    public static final String QUIZ_CORRECT = "/quiz_correct";
    public static final String QUIZ_INCORRECT = "/quiz_incorrect";
    public static final String QUIZ_START = "/quiz_start";
    //Храним варианты ответа
    private static final List<String> OPTIONS = List.of("A", "B", "C", "D");

    private final JpaUserRepository userRepository;
    private final JpaQuestionRepository questionRepository;

    @Autowired
    public QuizHandler(JpaUserRepository userRepository, JpaQuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if (message.startsWith(QUIZ_CORRECT)) {
            // действие на коллбек с правильным ответом
            return correctAnswer(user, message);
        } else if (message.startsWith(QUIZ_INCORRECT)) {
            // действие на коллбек с неправильным ответом
            return incorrectAnswer(user);
        } else {
            return startNewQuiz(user);
        }
    }

    private List<PartialBotApiMethod<? extends Serializable>> correctAnswer(User user, String message) {
        log.info("correct");
        final int currentScore = user.getScore() + 1;
        userRepository.save(user);

        return nextQuestion(user);
    }

    private List<PartialBotApiMethod<? extends Serializable>> incorrectAnswer(User user) {
        final int currentScore = user.getScore();
        // Обновляем лучший итог
        if (user.getHighScore() < currentScore) {
            user.setHighScore(currentScore);
        }
        // Меняем статус пользователя
        user.setScore(0);
        user.setBotState(State.NONE);
        userRepository.save(user);

        // Создаем кнопку для повторного начала игры
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Try again?", QUIZ_START));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        SendMessage messageTemplate = createMessageTemplate(user);
        messageTemplate.setText(String.format("Incorrect!%nYou scored *%d* points!", currentScore));
        messageTemplate.setReplyMarkup(inlineKeyboardMarkup);

        return List.of(messageTemplate);
    }

    private List<PartialBotApiMethod<? extends Serializable>> startNewQuiz(User user) {
        user.setBotState(State.PLAYING_QUIZ);
        userRepository.save(user);

        return nextQuestion(user);
    }

    private List<PartialBotApiMethod<? extends Serializable>> nextQuestion(User user) {
        Question question = questionRepository.getRandomQuestion();

        // Собираем список возможных вариантов ответа
        List<String> options = new ArrayList<>(List.of(question.getCorrectAnswer(),
                question.getOptionOne(),
                question.getOptionTwo(),
                question.getOptionThree()));
        // Перемешиваем
        Collections.shuffle(options);

        // Начинаем формировать сообщение с вопроса
        StringBuilder sb = new StringBuilder();
        sb.append('*')
                .append(question.getQuestion())
                .append("*\n\n");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // Создаем два ряда кнопок
        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowTwo = new ArrayList<>();

        // Формируем сообщение и записываем CallBackData на кнопки
        for (int i = 0; i < options.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();

            final String callbackData = options.get(i).equalsIgnoreCase(question.getCorrectAnswer()) ? QUIZ_CORRECT : QUIZ_INCORRECT;

            button.setText(OPTIONS.get(i));
            button.setCallbackData(String.format("%s %d", callbackData, question.getId()));

            if (i < 2) {
                inlineKeyboardButtonsRowOne.add(button);
            } else {
                inlineKeyboardButtonsRowTwo.add(button);
            }
            sb.append(OPTIONS.get(i))
                    .append(". ")
                    .append(options.get(i))
                    .append("\n");
        }

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne, inlineKeyboardButtonsRowTwo));

        SendMessage messageTemplate = createMessageTemplate(user);
        messageTemplate.setText(sb.toString());
        messageTemplate.setReplyMarkup(inlineKeyboardMarkup);

        return List.of(messageTemplate);
    }

    @Override
    public State operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(QUIZ_START, QUIZ_CORRECT, QUIZ_INCORRECT);
    }
}
