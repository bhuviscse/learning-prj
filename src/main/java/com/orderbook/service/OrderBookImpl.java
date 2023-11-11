package com.orderbook.service;

import com.orderbook.dto.ItemDTO;
import com.orderbook.dto.OrderResponseDTO;
import com.orderbook.enums.OrderType;

import java.io.IOException;
import java.util.List;

public class OrderBookImpl {
    OrderBook orderBook;
    public OrderResponseDTO buyOrSellOrder(String type, List<ItemDTO> itemDTO, Boolean isBuy) throws IOException {
        OrderResponseDTO orderResponseDTO = null;
        if (OrderType.MARKET.name().equals(type)) {
            orderBook = new MarketOrderBook();
            orderResponseDTO = orderBook.buyOrSellOrder(itemDTO, isBuy);
        } else if (OrderType.LIMIT.name().equals(type)) {
            orderBook = new LimitOrderBook();
            orderResponseDTO = orderBook.buyOrSellOrder(itemDTO, isBuy);
        }
        return orderResponseDTO;
    }
}
