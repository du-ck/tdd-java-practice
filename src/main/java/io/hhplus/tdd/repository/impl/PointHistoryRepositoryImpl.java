package io.hhplus.tdd.repository.impl;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.entity.PointHistory;
import io.hhplus.tdd.entity.types.TransactionType;
import io.hhplus.tdd.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final PointHistoryTable pointHistoryDB;

    @Override
    public PointHistory insert(long userId, long amount, TransactionType type, long updateMillis) {
        return pointHistoryDB.insert(userId, amount, type, updateMillis);
    }

    @Override
    public List<PointHistory> selectAllByUserId(long userId) {
        return pointHistoryDB.selectAllByUserId(userId);
    }
}
