package ru.dartilla.bookkeeper.rest;

import lombok.Data;

@Data
public class ScriptModel {
    private Long id;
    private String title;
    private String authorName;
    private String genreNames;
}
