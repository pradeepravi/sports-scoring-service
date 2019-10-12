package com.example.sports.service.exception;

public class MatchAlreadyConcludedException extends  Exception {
    public MatchAlreadyConcludedException() {
    }

    public MatchAlreadyConcludedException(String message) {
        super(message);
    }
}
