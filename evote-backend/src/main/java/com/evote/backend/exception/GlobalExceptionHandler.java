package com.evote.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final  Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String TIMESTAMP_KEY = "timestamp";
    @ExceptionHandler(InvalidVoteException.class)
    ProblemDetail handleInvalidVoteException(InvalidVoteException e) {
        //It is usually the voter fault, no need to log stack trace
        log.warn("Invalid vote attempt: {}", e.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid vote attempt");
        problemDetail.setProperty(TIMESTAMP_KEY, Instant.now());
        problemDetail.setTitle("Invalid Vote");
        problemDetail.setType(URI.create("https://api.evote.com/errors/invalid-vote"));
        return problemDetail;
    }

    @ExceptionHandler(BlockchainException.class)
    ProblemDetail handleBlockchainException(BlockchainException e) {
        log.error("Blockchain transaction failed", e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "The vote could not be recorded at this time. Please try again later.");
        problemDetail.setProperty(TIMESTAMP_KEY, Instant.now());
        problemDetail.setTitle("Blockchain Error");
        problemDetail.setType(URI.create("https://api.evote.com/errors/blockchain-error"));
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleGenericException(Exception e) {
        log.error("Unhandled exception occurred: ", e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred.");
        problemDetail.setProperty(TIMESTAMP_KEY, Instant.now());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.evote.com/errors/internal-server-error"));
        return problemDetail;
    }

    @ExceptionHandler(VoteAlreadyExistsException.class)
    ProblemDetail handleVoteAlreadyExistsException(VoteAlreadyExistsException e) {
        log.warn("Duplicate vote attempt: {}", e.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "You have already cast a vote in this election.");
        problemDetail.setProperty(TIMESTAMP_KEY, Instant.now());
        problemDetail.setProperty("zbi", "12345");
        problemDetail.setTitle("Duplicate Vote");
        problemDetail.setType(URI.create("https://api.evote.com/errors/duplicate-vote"));
        return problemDetail;
    }
}
