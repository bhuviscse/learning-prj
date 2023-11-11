package com.orderbook;

import com.orderbook.dto.ItemDTO;
import com.orderbook.dto.OrderResponseDTO;
import com.orderbook.service.OrderBookImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class Test {
    public static void main(String[] arg) throws IOException {
        OrderBookImpl orderBook = new OrderBookImpl();
        OrderResponseDTO orderResponseDTO = orderBook.buyOrSellOrder("LIMIT", Arrays.asList(ItemDTO.builder().id("SKU001")
                        .minLimit(new BigDecimal("50")).maxLimit(new BigDecimal("100")).quantity(3).build()
                , ItemDTO.builder().id("SKU002").minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("3000")).quantity(1).build()), true);
    }
}
