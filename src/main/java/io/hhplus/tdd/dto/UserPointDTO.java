package io.hhplus.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserPointDTO {
    private Long user_id;
    private Long user_point;
}
