package io.hhplus.tdd;

import io.hhplus.tdd.dto.UserPointDTO;
import io.hhplus.tdd.service.PointService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.StampedLock;

@SpringBootTest
public class PointIntegrationTest {

    @Autowired
    private PointService pointService;

    private static final StampedLock lock = new StampedLock();

    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
    }

    /**
     * 포인트 충전 동시성 이슈체크를 위한 테스트코드
     * 500포인트충전을 10번 동시에 충전면 5000포인트가 되어야한다.
     *
     * @throws Exception
     */
    @Test
    void 포인트충전_동시성테스트() throws Exception {
        Long pointToCharge = 500L;

        int numThreads = 10;    //쓰레드 개수
        AtomicLong count = new AtomicLong(); // 쓰레드 개수만큼 메서드가 실행되었는지 확인을 위한 count

        CountDownLatch latch = new CountDownLatch(numThreads);  //쓰레드들을 동시 시작 및 종료를 관리하기 위한 객체
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads); //정해진 쓰레드들(numThreads)에게 동시에 작업할당을 하기위한 객체

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    pointService.chargePoint(userId, pointToCharge);
                    count.getAndIncrement();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();  // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown(); //쓰레드 풀 종료

        // 포인트 확인
        UserPointDTO userPoint = pointService.getUserPointInfo(userId);

        Assertions.assertEquals(numThreads, count.longValue());     // 쓰레드 개수와 메서드 실행 횟수 비교
        Assertions.assertEquals(5000L, userPoint.getUser_point());
    }

    /**
     * 포인트 사용 동시성 이슈체크를 위한 테스트코드
     * 기존이 10000 포인트를 충전 후 300포인트를 10개의 스레드로 동시에 사용시키면
     * 7000포인트가 남아야 한다.
     * @throws Exception
     */
    @Test
    void 포인트사용_동시성테스트() throws Exception {
        Long pointToCharge = 10000L;
        Long pointToUse = 300L;
        pointService.chargePoint(userId, pointToCharge);    //사용 테스트를 위한 포인트 충전

        int numThreads = 10;    //쓰레드 개수
        AtomicLong count = new AtomicLong(); // 쓰레드 개수만큼 메서드가 실행되었는지 확인을 위한 count
        CountDownLatch latch = new CountDownLatch(numThreads);  //쓰레드들을 동시 시작 및 종료를 관리하기 위한 객체
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads); //정해진 쓰레드들(numThreads)에게 동시에 작업할당을 하기위한 객체

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    pointService.usePoint(userId, pointToUse);
                    count.getAndIncrement();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();  // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown(); //쓰레드 풀 종료

        // 포인트 확인
        UserPointDTO userPoint = pointService.getUserPointInfo(userId);

        Assertions.assertEquals(numThreads, count.longValue());     // 쓰레드 개수와 메서드 실행 횟수 비교
        Assertions.assertEquals(7000L, userPoint.getUser_point());
    }
}
