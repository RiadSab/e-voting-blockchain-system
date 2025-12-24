package com.evote.backend.mapper;

import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor

public final class VoteParamMapper {

    public static BigInteger toBigInt(String s, String fieldName) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is blank");
        }
        String v = s.trim();
        try {
            if (v.startsWith("0x") || v.startsWith("0X")) {
                return new BigInteger(v.substring(2), 16);
            }
            return new BigInteger(v, 10);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " is not a valid integer: " + s, ex);
        }
    }

    public static List<BigInteger> toBigIntList(List<String> values, String fieldName) {
        Objects.requireNonNull(values, fieldName + " is null");
        return values.stream()
                .map(x -> toBigInt(x, fieldName + "[]"))
                .toList();
    }

    public static void requireSize(List<?> list, int size, String fieldName) {
        if (list == null) throw new IllegalArgumentException(fieldName + " is null");
        if (list.size() != size) {
            throw new IllegalArgumentException(fieldName + " must have exactly " + size + " elements, got " + list.size());
        }
    }
}
