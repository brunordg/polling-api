package com.nps.pollingapi.dto;

public record SignupRequest(String email, String password, String firstName, String lastName) {
}
