package com.orderbook;

import com.orderbook.dto.ItemDTO;
import com.orderbook.dto.OrderResponseDTO;
import com.orderbook.enums.OrderType;
import com.orderbook.service.OrderBookImpl;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderBookImplTest {
    @Test
    public void buyOrSellOrderTest() throws IOException {
        OrderBookImpl orderBook = new OrderBookImpl();
        OrderResponseDTO orderResponseDTO = orderBook.buyOrSellOrder(OrderType.MARKET.name(), Arrays.asList(ItemDTO.builder().id("SKU001")
                        .minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("300")).quantity(3).build()
                , ItemDTO.builder().id("SKU002").minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("3000")).quantity(1).build()), true);
        assertNotNull(orderResponseDTO.getOrderId());
        assertEquals(2, orderResponseDTO.getItems().size());
        assertEquals(new BigDecimal("2716.23"), orderResponseDTO.getTotalPrice());
    }
    ////

    @Test
    public void buyLimitOrderWhenAllItemsAreInPriceLimitTest() throws IOException {
        OrderBookImpl orderBook = new OrderBookImpl();
        OrderResponseDTO orderResponseDTO = orderBook.buyOrSellOrder(OrderType.LIMIT.name(), Arrays.asList(ItemDTO.builder().id("SKU001")
                        .minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("300")).quantity(3).build()
                , ItemDTO.builder().id("SKU002").minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("3000")).quantity(1).build()), true);
        assertNotNull(orderResponseDTO.getOrderId());
        assertEquals(2, orderResponseDTO.getItems().size());
        assertEquals(new BigDecimal("2716.23"), orderResponseDTO.getTotalPrice());
    }

    @Test
    public void sellLimitOrderWhenAllItemsAreInPriceLimitTest() throws IOException {
        OrderBookImpl orderBook = new OrderBookImpl();
        OrderResponseDTO orderResponseDTO = orderBook.buyOrSellOrder(OrderType.LIMIT.name(), Arrays.asList(ItemDTO.builder().id("SKU001")
                        .minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("300")).quantity(3).build()
                , ItemDTO.builder().id("SKU002").minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("3000")).quantity(1).build()), false);
        assertNotNull(orderResponseDTO.getOrderId());
        assertEquals(2, orderResponseDTO.getItems().size());
        assertEquals(new BigDecimal("2716.23"), orderResponseDTO.getTotalPrice());
    }

    @Test
    public void buyLimitOrderWhenItemsAreNotInPriceLimitTest() throws IOException {
        OrderBookImpl orderBook = new OrderBookImpl();
        OrderResponseDTO orderResponseDTO = orderBook.buyOrSellOrder(OrderType.LIMIT.name(), Arrays.asList(ItemDTO.builder().id("SKU001")
                        .minLimit(new BigDecimal("50")).maxLimit(new BigDecimal("100")).quantity(3).build()
                , ItemDTO.builder().id("SKU002").minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("3000")).quantity(1).build()), true);
        assertNotNull(orderResponseDTO.getOrderId());
        assertEquals(1, orderResponseDTO.getItems().size());
        assertEquals(new BigDecimal("2344.89"), orderResponseDTO.getTotalPrice());
    }

    @Test
    public void sellLimitOrderWhenItemsAreNotInPriceLimitTest() throws IOException {
        OrderBookImpl orderBook = new OrderBookImpl();
        OrderResponseDTO orderResponseDTO = orderBook.buyOrSellOrder(OrderType.LIMIT.name(), Arrays.asList(ItemDTO.builder().id("SKU001")
                        .minLimit(new BigDecimal("50")).maxLimit(new BigDecimal("100")).quantity(3).build()
                , ItemDTO.builder().id("SKU002").minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("3000")).quantity(1).build()), false);
        assertNotNull(orderResponseDTO.getOrderId());
        assertEquals(1, orderResponseDTO.getItems().size());
        assertEquals(new BigDecimal("2344.89"), orderResponseDTO.getTotalPrice());
    }

    @Test
    public void buyMarketOrderTest() throws IOException {
        OrderBookImpl orderBook = new OrderBookImpl();
        OrderResponseDTO orderResponseDTO = orderBook.buyOrSellOrder(OrderType.MARKET.name(), Arrays.asList(ItemDTO.builder().id("SKU001").quantity(3).build()
                , ItemDTO.builder().id("SKU002").minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("3000")).quantity(1).build()), true);
        assertNotNull(orderResponseDTO.getOrderId());
        assertEquals(2, orderResponseDTO.getItems().size());
        assertEquals(new BigDecimal("2716.23"), orderResponseDTO.getTotalPrice());
    }

    @Test
    public void sellMarketOrderTest() throws IOException {
        OrderBookImpl orderBook = new OrderBookImpl();
        OrderResponseDTO orderResponseDTO = orderBook.buyOrSellOrder(OrderType.MARKET.name(), Arrays.asList(ItemDTO.builder().id("SKU001").quantity(3).build()
                , ItemDTO.builder().id("SKU002").minLimit(new BigDecimal("100")).maxLimit(new BigDecimal("3000")).quantity(1).build()), false);
        assertNotNull(orderResponseDTO.getOrderId());
        assertEquals(2, orderResponseDTO.getItems().size());
        assertEquals(new BigDecimal("2716.23"), orderResponseDTO.getTotalPrice());
    }
}
