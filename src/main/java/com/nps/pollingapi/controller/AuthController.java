package com.nps.pollingapi.controller;

import com.nps.pollingapi.dto.AuthenticationResponse;
import com.nps.pollingapi.dto.SignupRequest;
import com.nps.pollingapi.dto.UserPrincipalDTO;
import com.nps.pollingapi.services.auth.AuthService;
import com.nps.pollingapi.services.jwt.UserService;
import com.nps.pollingapi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    private final JWTUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            if (authService.hasUserWithEmail(signupRequest.email())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "User with email already exists!"));
            }

            UserPrincipalDTO user = authService.createUser(signupRequest);

            if (user == null || user.id() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Failed to create user."));
            }

            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(user.email());
            String jwt = jwtUtil.generateToken(userDetails, user.id());

            AuthenticationResponse response = new AuthenticationResponse(
                    jwt,
                    user.firstName() + " " + user.lastName(),
                    user.email()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An error occurred during signup."));
        }

    }
}
