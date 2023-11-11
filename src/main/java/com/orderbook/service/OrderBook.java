package com.orderbook.service;

import com.orderbook.dto.ItemDTO;
import com.orderbook.dto.OrderResponseDTO;

import java.io.IOException;
import java.util.List;

public interface OrderBook {
    public OrderResponseDTO buyOrSellOrder(List<ItemDTO> itemDTO, Boolean isBuy) throws IOException;
}
