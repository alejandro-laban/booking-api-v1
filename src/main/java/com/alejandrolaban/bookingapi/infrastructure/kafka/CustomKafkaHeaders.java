package com.alejandrolaban.bookingapi.infrastructure.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.kafka.support.KafkaHeaders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomKafkaHeaders {

    public static final String DLT_EXCEPTION_MESSAGE_ROOT = KafkaHeaders.DLT_EXCEPTION_MESSAGE + "-root";
    public static final String DLT_EXCEPTION_STACKTRACE_ROOT = KafkaHeaders.DLT_EXCEPTION_STACKTRACE + "-root";
    public static final String DLT_EXCEPTION_ROOT = KafkaHeaders.PREFIX + "dlt-exception" + "-root";
}
