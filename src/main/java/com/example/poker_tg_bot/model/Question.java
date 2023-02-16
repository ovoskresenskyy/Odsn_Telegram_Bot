package com.example.poker_tg_bot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "java_quiz")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Question extends AbstractBaseEntity {

    private String question;

    @Column(name = "answer_correct", nullable = false)
    private String correctAnswer;

    @Column(name = "option1", nullable = false)
    private String optionOne;

    @Column(name = "option2", nullable = false)
    private String optionTwo;

    @Column(name = "option3", nullable = false)
    private String optionThree;

}
