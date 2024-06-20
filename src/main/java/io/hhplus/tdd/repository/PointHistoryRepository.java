package io.hhplus.tdd.repository;

import io.hhplus.tdd.entity.PointHistory;
import io.hhplus.tdd.entity.types.TransactionType;

import java.util.List;

public interface PointHistoryRepository {
    PointHistory insert(long userId, long amount, TransactionType type, long updateMillis);
    List<PointHistory> selectAllByUserId(long userId);
}
