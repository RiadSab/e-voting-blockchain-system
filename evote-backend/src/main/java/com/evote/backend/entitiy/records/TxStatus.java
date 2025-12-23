package com.evote.backend.entitiy.records;

public enum TxStatus {
    CREATED,
    BROADCASTED,
    MINED,
    CONFIRMED,
    REVERTED,
    FAILED_TO_BROADCAST,
    TIMEOUT_PENDING
}