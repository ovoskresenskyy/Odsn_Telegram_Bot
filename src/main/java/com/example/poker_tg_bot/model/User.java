package com.example.poker_tg_bot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = "chat_id",
                name = "users_unique_chatid_idx")})
public class User extends AbstractBaseEntity {

    @Column(name = "chat_id")
    private int chatId;
    private String name;
    private Integer score;
    @Column(name = "high_score")
    private Integer highScore;
    @Column(name = "bot_state")
    private State botState;

}
