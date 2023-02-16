package com.example.poker_tg_bot.repository;

import com.example.poker_tg_bot.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface JpaQuestionRepository extends JpaRepository<Question, Integer> {

    @Query(nativeQuery = true, value = "SELECT *  FROM java_quiz ORDER BY random() LIMIT 1")
    Question getRandomQuestion();
}
