package dev.mehdizebhi.web3.controllers.request;

public record RegisterRequest(String email, String username, String password, String confirmPassword) {
}