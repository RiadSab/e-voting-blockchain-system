package com.evote.backend.blockchain.tx;

import java.time.Duration;

public record RetryPolicy(int maxAttempts, Duration initialBackoff, Duration maxBackoff) { }

