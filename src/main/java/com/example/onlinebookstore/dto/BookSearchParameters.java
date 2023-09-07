package com.example.onlinebookstore.dto;

public record BookSearchParameters(String[] title, String[] author, String[] isbn, String[] price,
                                   String[] description, String[] categories) {
}
