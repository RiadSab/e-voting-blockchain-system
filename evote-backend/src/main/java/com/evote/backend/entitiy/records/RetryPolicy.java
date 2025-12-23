package com.evote.backend.entitiy.records;

import java.time.Duration;

public record RetryPolicy(int maxAttempts, Duration initialBackoff, Duration maxBackoff) { }

