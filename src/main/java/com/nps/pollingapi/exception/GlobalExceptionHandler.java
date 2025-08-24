package com.nps.pollingapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        logException(e);  // Log a detailed exception for debugging purposes
        return buildProblemDetailResponse(e, HttpStatus.BAD_REQUEST, "Data Integrity Violation", "common.data_integrity_violation", request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        logException(ex);  // Log a detailed exception for debugging purposes
        return buildProblemDetailResponse(ex, HttpStatus.NOT_FOUND, "Resource not found", ex.getCode(), req);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        logException(ex);  // Log a detailed exception for debugging purposes
        return buildProblemDetailResponse(ex, HttpStatus.UNAUTHORIZED, "Invalid credentials", "auth.invalid_credentials", req);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ProblemDetail> handleDisabled(DisabledException ex, HttpServletRequest req) {
        logException(ex);  // Log a detailed exception for debugging purposes
        return buildProblemDetailResponse(ex, HttpStatus.FORBIDDEN, "User disabled", "auth.user_disabled", req);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest req) {
        logException(ex);  // Log a detailed exception for debugging purposes
        return buildProblemDetailResponse(ex, HttpStatus.NOT_FOUND, "User not found", "auth.user_not_found", req);
    }


    private void logException(Exception e) {
        log.error("Exception occurred: {} - {}", e.getClass().getName(), e.getMessage(), e);
    }

    private ResponseEntity<ProblemDetail> buildProblemDetailResponse(Exception ex, HttpStatus status, String title, String code, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        pd.setTitle(title);
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", code);
        pd.setProperty("timestamp", System.currentTimeMillis());  // Optional: Add timestamp to help track errors
        pd.setType(URI.create(request.getRequestURI()));

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(pd);
    }
}
