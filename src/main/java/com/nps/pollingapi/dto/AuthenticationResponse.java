package com.nps.pollingapi.dto;

public record AuthenticationResponse(String jwtToken, String name, String email) {
}
