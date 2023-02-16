package com.example.poker_tg_bot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MappedSuperclass
@Access(AccessType.FIELD)
@NoArgsConstructor
public abstract class AbstractBaseEntity {

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    private int Id;

}
