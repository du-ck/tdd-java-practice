package io.hhplus.tdd.service;

import io.hhplus.tdd.dto.UserPointDTO;
import io.hhplus.tdd.repository.PointHistoryRepository;
import io.hhplus.tdd.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UserPointDTO chargePoint(long id, long amount) throws Exception {
        return null;
    }
}
