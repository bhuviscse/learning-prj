package com.orderbook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.orderbook.configuration.OrderConfiguration;
import com.orderbook.dto.ItemDTO;
import com.orderbook.dto.OrderResponseDTO;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class MarketOrderBook implements OrderBook{

    public OrderResponseDTO buyOrSellOrder(List<ItemDTO> itemDTO, Boolean isBuy) throws IOException {
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(new BigDecimal(0));
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        OrderConfiguration orderConfiguration = mapper.readValue(new File("src/main/resources/application.yml"), OrderConfiguration.class);
        itemDTO.forEach(item -> {
            BigDecimal priceForSingleItem = orderConfiguration.getOrderLines().getOrDefault(item.getId(), new BigDecimal(0));
            BigDecimal totalPriceForSingleItem = priceForSingleItem.multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice.getAndAccumulate(totalPriceForSingleItem, BigDecimal::add);
        });
        String orderId = UUID.randomUUID().toString();
        if (isBuy) {
            makePayment(orderId, totalPrice);
        } else {
            acceptPayment(orderId, totalPrice);
        }

        return OrderResponseDTO.builder().orderId(orderId).totalPrice(totalPrice.get()).items(itemDTO).build();
    }

    private void makePayment(final String orderId, final AtomicReference<BigDecimal> totalPrice) {
        // save into db
    }

    private void acceptPayment(final String orderId, final AtomicReference<BigDecimal> totalPrice) {
        // save into db
    }
}
