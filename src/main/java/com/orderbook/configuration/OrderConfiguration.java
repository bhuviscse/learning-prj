package com.orderbook.configuration;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class OrderConfiguration {
    private Map<String,BigDecimal> orderLines;
}
