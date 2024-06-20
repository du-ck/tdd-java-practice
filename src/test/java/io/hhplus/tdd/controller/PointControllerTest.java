package io.hhplus.tdd.controller;

import io.hhplus.tdd.dto.UserPointDTO;
import io.hhplus.tdd.entity.PointHistory;
import io.hhplus.tdd.entity.types.TransactionType;
import io.hhplus.tdd.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    MockMvc mockMvc;

    /**
     * 가짜객체를 만들어 컨테이너가 주입할 수 있도록 해줌.
     */
    @MockBean
    PointService pointService;

    private Long userId;
    private Long existPoint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = 1L;
        existPoint = 200L;
    }

    /**
     * Patch 요청을 통해 포인트충전 기능의 정상여부 판단을 위해 작성
     * @throws Exception
     */
    @Test
    void chargePoint() throws Exception {
        //given
        Long pointToCharge = 100L;

        given(pointService.chargePoint(userId, pointToCharge))
                .willReturn(UserPointDTO.builder()
                        .user_id(userId)
                        .user_point(existPoint + pointToCharge)
                        .build());
        //when - then
        mockMvc.perform(patch("/point/{id}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(pointToCharge)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user_id").exists())
                .andExpect(jsonPath("$.data.user_point").exists());
    }

    /**
     * GET 요청을 통해 사용자의 포인트 정보를 조회하는 기능의 정상여부 판단을 위해 작성
     * @throws Exception
     */
    @Test
    void getPointInfo() throws Exception {
        given(pointService.getUserPointInfo(userId))
                .willReturn(UserPointDTO.builder()
                        .user_id(userId)
                        .user_point(0L)
                        .build());

        mockMvc.perform(get("/point/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user_id").exists())
                .andExpect(jsonPath("$.data.user_point").exists());
    }

    /**
     * patch 요청을 통해 사용자의 포인트 사용 기능의 정상여부 판단을 위해 작성
     * @throws Exception
     */
    @Test
    void usePoint() throws Exception {
        //@PatchMapping("{id}/use")
        Long usePoint = 100L;
        given(pointService.usePoint(userId, usePoint))
                .willReturn(UserPointDTO.builder()
                        .user_id(userId)
                        .user_point(existPoint)
                        .build());

        mockMvc.perform(patch("/point/{id}/use", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(usePoint)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user_id").exists())
                .andExpect(jsonPath("$.data.user_point").exists());
    }

    /**
     * get 요청을 통해 사용자 포인트 내역기능의 정상여부 판단을 위해 작성
     * @throws Exception
     */
    @Test
    void getHistories() throws Exception {
        List<PointHistory> histories = new ArrayList<>(){
            {
                add(new PointHistory(1L, userId, 300, TransactionType.CHARGE, System.currentTimeMillis()));
                add(new PointHistory(2L, userId, -100, TransactionType.USE, System.currentTimeMillis()));
                add(new PointHistory(3L, userId, 200, TransactionType.CHARGE, System.currentTimeMillis()));
                add(new PointHistory(4L, userId, -50, TransactionType.USE, System.currentTimeMillis()));
            }
        };
        given(pointService.getPointHistories(userId))
                .willReturn(histories);

        mockMvc.perform(get("/point/{id}/histories", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].id").exists())
                .andExpect(jsonPath("$.data[*].userId").exists())
                .andExpect(jsonPath("$.data[*].amount").exists())
                .andExpect(jsonPath("$.data[*].type").exists())
                .andExpect(jsonPath("$.data[*].updateMillis").exists());
    }
}