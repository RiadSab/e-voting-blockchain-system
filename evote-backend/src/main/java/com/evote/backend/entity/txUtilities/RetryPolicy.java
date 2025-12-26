package com.evote.backend.entity.txUtilities;

import java.time.Duration;

public record RetryPolicy(int maxAttempts, Duration initialBackoff, Duration maxBackoff) { }

