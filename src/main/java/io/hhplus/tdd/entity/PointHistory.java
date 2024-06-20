package io.hhplus.tdd.entity;

import io.hhplus.tdd.entity.types.TransactionType;

public record PointHistory(
        long id,
        long userId,
        long amount,
        TransactionType type,
        long updateMillis
) {
}
