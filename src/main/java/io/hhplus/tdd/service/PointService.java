package io.hhplus.tdd.service;

import io.hhplus.tdd.dto.UserPointDTO;
import io.hhplus.tdd.entity.PointHistory;
import io.hhplus.tdd.entity.UserPoint;
import io.hhplus.tdd.entity.types.TransactionType;
import io.hhplus.tdd.exception.ChargeException;
import io.hhplus.tdd.exception.UsePointException;
import io.hhplus.tdd.repository.PointHistoryRepository;
import io.hhplus.tdd.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.StampedLock;

@Service
@RequiredArgsConstructor
public class PointService {
    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final StampedLock stampedLock = new StampedLock();

    public UserPointDTO chargePoint(long id, long amount) throws Exception {
        if (amount < 0) {
            throw new ChargeException("Amount must be positive");
        }

        long stamp = stampedLock.writeLock(); // 쓰기 잠금 획득

        try {
            //기존 포인트를 가져온다.
            Optional<UserPoint> existUser = Optional.ofNullable(userPointRepository.selectById(id));
            if (existUser.isPresent()) {
                //update 시킬 point
                Long updatePoint = existUser.get().point() + amount;

                Optional<UserPoint> getPoint = Optional.ofNullable(userPointRepository.insertOrUpdate(id, updatePoint));
                if (getPoint.isPresent()) {
                    pointHistoryRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

                    return UserPointDTO.builder()
                            .user_id(getPoint.get().id())
                            .user_point(getPoint.get().point())
                            .build();
                }
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }
        return null;
    }

    public UserPointDTO getUserPointInfo(long id) {
        //기존 포인트를 가져온다.
        Optional<UserPoint> existUser = Optional.ofNullable(userPointRepository.selectById(id));

        if (existUser.isPresent()) {
            return UserPointDTO.builder()
                    .user_id(existUser.get().id())
                    .user_point(existUser.get().point())
                    .build();
        }
        return null;
    }

    public UserPointDTO usePoint(long id, long amount) throws Exception {
        if (amount < 0) {
            throw new UsePointException("Amount must be positive");
        }

        long stamp = stampedLock.writeLock(); // 쓰기 잠금 획득
        try {
            //기존 포인트를 가져온다.
            Optional<UserPoint> existUser = Optional.ofNullable(userPointRepository.selectById(id));
            if (existUser.isPresent()) {
                if (existUser.get().point() < amount) {
                    throw new UsePointException("Insufficient Point");
                }
                //update 시킬 point
                Long updatePoint = existUser.get().point() - amount;
                Optional<UserPoint> getPoint = Optional.ofNullable(userPointRepository.insertOrUpdate(id, updatePoint));

                if (getPoint.isPresent()) {
                    pointHistoryRepository.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

                    return UserPointDTO.builder()
                            .user_id(getPoint.get().id())
                            .user_point(getPoint.get().point())
                            .build();
                }
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }

        return null;
    }

    public List<PointHistory> getPointHistories(long id) throws Exception {
        return pointHistoryRepository.selectAllByUserId(id);
    }
}
