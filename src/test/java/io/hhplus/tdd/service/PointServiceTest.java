package io.hhplus.tdd.service;

import io.hhplus.tdd.dto.UserPointDTO;
import io.hhplus.tdd.entity.UserPoint;
import io.hhplus.tdd.exception.ChargeException;
import io.hhplus.tdd.exception.UsePointException;
import io.hhplus.tdd.repository.PointHistoryRepository;
import io.hhplus.tdd.repository.UserPointRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    private Long userId;
    private Long existUserPoint;

    @BeforeEach
    void setUp() {
        userId = 1L;
        existUserPoint = 200L;
    }

    /**
     * 기본적으로 충전의 기능이 정상작동 여부를 보기 위한 테스트
     */
    @Test
    void 충전기능_테스트() throws Exception {
        Long pointToCharge = 100L;

        UserPoint user = new UserPoint(userId, existUserPoint, 0);

        given(userPointRepository.selectById(anyLong()))
                .willReturn(user);

        given(userPointRepository.insertOrUpdate(anyLong(), anyLong()))
                .willReturn(new UserPoint(userId, user.point() + pointToCharge, 0));

        UserPointDTO resultUser = pointService.chargePoint(userId, pointToCharge);

        Assertions.assertNotNull(resultUser);
        Assertions.assertEquals(userId, resultUser.getUser_id());
        Assertions.assertEquals(300L, resultUser.getUser_point());
    }

    /**
     * 사용 기능이 아닌 충전 기능에서 충전하고자 하는 포인트가 0 이하인건 의미없다고 판단하여 테스트 코드 작성
     *
     * @throws Exception
     */
    @Test
    void 음수나_0_포인트_충전_테스트() {
        Exception exception = Assertions.assertThrows(ChargeException.class,
                () -> pointService.chargePoint(userId, -10L));

        //예외 메세지 검증
        Assertions.assertEquals("Amount must be positive", exception.getMessage());
    }

    /**
     * id 가 없는 케이스의 테스트는
     * 현재 프로젝트에서 의미가 없어 보이므로 패스..
     * ex) selectById -> table.getOrDefault()
     *     insertOrUpdate -> table.put()
     */

    /**
     * 포인트 조회의 기본적인 기능 성공 여부를 판단하기 위한 테스트
     */
    @Test
    void 유저의_포인트를_조회하는_기능_테스트() {

        UserPoint user = new UserPoint(userId, existUserPoint, 0);

        given(userPointRepository.selectById(anyLong()))
                .willReturn(user);


        UserPointDTO userPoint = pointService.getUserPointInfo(userId);

        Assertions.assertNotNull(userPoint);
        Assertions.assertEquals(userId, userPoint.getUser_id());
        Assertions.assertEquals(userPoint.getUser_point(), existUserPoint);
    }

    /**
     * 포인트 사용의 기본적인 기능 성공 여부를 판단하기 위한 테스트
     * @throws Exception
     */
    @Test
    void 유저의_포인트_사용기능_테스트() throws Exception {
        Long usePoint = 100L;

        UserPoint user = new UserPoint(userId, existUserPoint, 0);

        given(userPointRepository.selectById(anyLong()))
                .willReturn(user);
        given(userPointRepository.insertOrUpdate(anyLong(), anyLong()))
                .willReturn(new UserPoint(userId, existUserPoint - usePoint, 0));

        UserPointDTO userPoint = pointService.usePoint(userId, usePoint);

        Assertions.assertNotNull(userPoint);
        Assertions.assertEquals(userId, userPoint.getUser_id());
        Assertions.assertEquals(userPoint.getUser_point(), existUserPoint - usePoint);
    }

    /**
     * 사용할 포인트가 0 이하는 의미없다고 판단하여 테스트 작성
     */
    @Test
    void 유저가_사용할_포인트가_0이하인_경우() {
        Exception exception = Assertions.assertThrows(UsePointException.class,
                () -> pointService.usePoint(userId, -50L));

        //예외 메세지 검증
        Assertions.assertEquals("Amount must be positive", exception.getMessage());
    }

    /**
     * 포인트가 부족하면 사용을 못해야 하므로 테스트 작성
     */
    @Test
    void 유저가_사용할_포인트만큼_잔액이_없을_경우() {
        Long usePoint = 500L;

        UserPoint user = new UserPoint(userId, existUserPoint, 0);

        given(userPointRepository.selectById(anyLong()))
                .willReturn(user);

        Exception exception = Assertions.assertThrows(UsePointException.class,
                () -> pointService.usePoint(userId, usePoint));

        //예외 메세지 검증
        Assertions.assertEquals("Insufficient Point", exception.getMessage());
    }
}