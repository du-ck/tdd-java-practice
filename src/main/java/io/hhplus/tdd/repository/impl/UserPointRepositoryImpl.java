package io.hhplus.tdd.repository.impl;

import io.hhplus.tdd.entity.UserPoint;
import io.hhplus.tdd.repository.UserPointRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserPointRepositoryImpl implements UserPointRepository {
    @Override
    public UserPoint selectById(Long id) {
        return null;
    }

    @Override
    public UserPoint insertOrUpdate(long id, long amount) {
        return null;
    }
}
