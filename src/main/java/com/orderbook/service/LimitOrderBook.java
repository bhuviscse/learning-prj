package com.orderbook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.orderbook.configuration.OrderConfiguration;
import com.orderbook.dto.ItemDTO;
import com.orderbook.dto.OrderResponseDTO;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class LimitOrderBook implements OrderBook{

    public OrderResponseDTO buyOrSellOrder(List<ItemDTO> itemDTO, Boolean isBuy) throws IOException {
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<OrderResponseDTO> orderResponseDTO = new AtomicReference<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        OrderConfiguration orderConfiguration = mapper.readValue(new File("src/main/resources/application.yml"), OrderConfiguration.class);
        {
            AtomicReference<List<ItemDTO>> toBeOrdered = new AtomicReference<>();
            toBeOrdered.set(new ArrayList<>());
            itemDTO.forEach(item -> {
                BigDecimal priceForSingleItem = orderConfiguration.getOrderLines().getOrDefault(item.getId(), new BigDecimal(0));
                if (priceForSingleItem.compareTo(item.getMinLimit()) >= 0 && priceForSingleItem.compareTo(item.getMaxLimit()) <= 0) {
                    BigDecimal totalPriceForSingleItem = priceForSingleItem.multiply(BigDecimal.valueOf(item.getQuantity()));
                    totalPrice.getAndAccumulate(totalPriceForSingleItem, BigDecimal::add);
                } else {
                    toBeOrdered.get().add(item);
                }

            });
            if (totalPrice.get().compareTo(BigDecimal.valueOf(0)) > 0) {
                // have to remove toBeRemoved list from itemDTO
                String orderId = UUID.randomUUID().toString();
                makePayment(orderId, totalPrice);
                orderResponseDTO.set(OrderResponseDTO.builder().orderId(orderId).totalPrice(totalPrice.get())
                        .items(itemDTO.stream().filter(o -> !toBeOrdered.get().contains(o)).collect(Collectors.toList())).build());
                System.out.println("limit order placed for " +orderResponseDTO.get().toString());
            }
            if (!toBeOrdered.get().isEmpty()) {
                ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                Runnable runnable = () -> {
                    try {
                        if (toBeOrdered.get().isEmpty()) {
                            scheduledExecutorService.shutdown();
                        } else {
                            toBeOrdered.get().forEach(item -> {
                                BigDecimal priceForSingleItem = orderConfiguration.getOrderLines().getOrDefault(item.getId(), new BigDecimal(0));
                                if (priceForSingleItem.compareTo(item.getMinLimit()) >= 0 && priceForSingleItem.compareTo(item.getMaxLimit()) <= 0) {
                                    BigDecimal totalPriceForSingleItem = priceForSingleItem.multiply(BigDecimal.valueOf(item.getQuantity()));
                                    totalPrice.getAndAccumulate(totalPriceForSingleItem, BigDecimal::add);
                                    String orderId = UUID.randomUUID().toString();
                                    if (isBuy) {
                                        makePayment(orderId, totalPrice);
                                    } else {
                                        acceptPayment(orderId, totalPrice);
                                    }
                                    orderResponseDTO.set(OrderResponseDTO.builder()
                                            .orderId(orderId).totalPrice(totalPrice.get()).items(Arrays.asList(item)).build());
                                    System.out.println("limit order placed for " + orderResponseDTO);
                                    toBeOrdered.get().remove(item);
                                }

                            });
                        }
                    } catch (Exception e) {
                        System.out.println("Interrupted Exception");
                    }
                };
                // periodically execute the same task every 450 ms
                scheduledExecutorService.scheduleAtFixedRate(runnable, 100, 450, TimeUnit.MILLISECONDS);
            }
        }
        return orderResponseDTO.get();
    }

    private void makePayment(final String orderId, final AtomicReference<BigDecimal> totalPrice) {
        // save into db
    }

    private void acceptPayment(final String orderId, final AtomicReference<BigDecimal> totalPrice) {
        // save into db
    }
}
