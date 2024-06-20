package io.hhplus.tdd.controller;

import io.hhplus.tdd.ResponseData;
import io.hhplus.tdd.dto.UserPointDTO;
import io.hhplus.tdd.entity.PointHistory;
import io.hhplus.tdd.entity.UserPoint;
import io.hhplus.tdd.service.PointService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private final PointService pointService;

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    public ResponseEntity<ResponseData> point(
            @PathVariable(name = "id") long id
    ) {
        UserPointDTO user = pointService.getUserPointInfo(id);
        ResponseData responseData = ResponseData.builder()
                .data(user)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    public ResponseEntity<ResponseData> history(
            @PathVariable(name = "id") long id
    ) throws Exception {
        List<PointHistory> resultList = pointService.getPointHistories(id);
        ResponseData responseData = ResponseData.builder()
                .data(CollectionUtils.isEmpty(resultList) ? "No Data" : resultList)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    public ResponseEntity<ResponseData> charge(
            @PathVariable(name = "id") long id,
            @RequestBody long amount
    ) throws Exception {
        UserPointDTO user = pointService.chargePoint(id, amount);
        ResponseData responseData = ResponseData.builder()
                .data(user)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    public ResponseEntity<ResponseData> use(
            @PathVariable(name = "id") long id,
            @RequestBody long amount
    ) throws Exception {
        UserPointDTO user = pointService.usePoint(id, amount);
        ResponseData responseData = ResponseData.builder()
                .data(user)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
