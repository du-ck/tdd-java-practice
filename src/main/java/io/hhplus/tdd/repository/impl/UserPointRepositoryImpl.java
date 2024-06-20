package io.hhplus.tdd.repository.impl;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.entity.UserPoint;
import io.hhplus.tdd.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {
    private final UserPointTable userPointDB;

    @Override
    public UserPoint selectById(Long id) {
        return userPointDB.selectById(id);
    }

    @Override
    public UserPoint insertOrUpdate(long id, long amount) {
        return userPointDB.insertOrUpdate(id, amount);
    }
}
