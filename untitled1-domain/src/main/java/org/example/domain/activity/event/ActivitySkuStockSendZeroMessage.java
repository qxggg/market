package org.example.domain.activity.event;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.example.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ActivitySkuStockSendZeroMessage extends BaseEvent<Long>{
    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String value;

    @Override
    public EventMessage<Long> buildEventMessage(Long sku) {
        return EventMessage.<Long>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .data(sku)
                .timestamp(new Date())
                .build();
    }

    @Override
    public String topic() {
        return topic();
    }
}
