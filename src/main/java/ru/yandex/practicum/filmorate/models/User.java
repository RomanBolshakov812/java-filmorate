package ru.yandex.practicum.filmorate.models;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}