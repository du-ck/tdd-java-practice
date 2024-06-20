package io.hhplus.tdd.repository.impl;

import io.hhplus.tdd.entity.PointHistory;
import io.hhplus.tdd.entity.types.TransactionType;
import io.hhplus.tdd.repository.PointHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {
    @Override
    public PointHistory insert(long userId, long amount, TransactionType type, long updateMillis) {
        return null;
    }

    @Override
    public List<PointHistory> selectAllByUserId(long userId) {
        return null;
    }
}
