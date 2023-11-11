package com.orderbook.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@Builder(toBuilder = true)
@Getter
@ToString
public class ItemDTO {
    private String id;
    private Integer quantity;
    private BigDecimal minLimit;
    private BigDecimal maxLimit;
}
