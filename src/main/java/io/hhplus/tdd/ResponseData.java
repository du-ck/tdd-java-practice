package io.hhplus.tdd;

import lombok.Builder;
import lombok.Data;

/**
 * 공통 response data
 */
@Data
@Builder
public class ResponseData<T> {
    private T data;
}
