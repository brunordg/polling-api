package com.nps.pollingapi.controller;

import com.nps.pollingapi.dto.AuthenticationRequest;
import com.nps.pollingapi.dto.AuthenticationResponse;
import com.nps.pollingapi.dto.SignupRequest;
import com.nps.pollingapi.dto.UserPrincipalDTO;
import com.nps.pollingapi.entity.UserPrincipal;
import com.nps.pollingapi.exception.NotFoundException;
import com.nps.pollingapi.repository.UserPrincipalRepository;
import com.nps.pollingapi.services.auth.AuthService;
import com.nps.pollingapi.services.jwt.UserService;
import com.nps.pollingapi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final UserPrincipalRepository userPrincipalRepository;

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
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An error occurred during signup."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        UserPrincipal user = userPrincipalRepository.findFirstByEmail(authenticationRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.email(), authenticationRequest.password()
                )
        );

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.email());

        String jwt = jwtUtil.generateToken(userDetails, user.getId());

        AuthenticationResponse response = new AuthenticationResponse(
                jwt,
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }

}
