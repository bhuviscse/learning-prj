package com.orderbook.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder(toBuilder = true)
@Getter
@ToString
public class OrderResponseDTO {
    String orderId;
    List<ItemDTO> items;
    BigDecimal totalPrice;
}
