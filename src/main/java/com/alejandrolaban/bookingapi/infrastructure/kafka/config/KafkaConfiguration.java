package com.alejandrolaban.bookingapi.infrastructure.kafka.config;

import com.alejandrolaban.booking.register.BookingRegisterMessage;
import com.alejandrolaban.bookingapi.application.BookingConfigurationProperties;
import com.alejandrolaban.bookingapi.infrastructure.kafka.CustomKafkaHeaders;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

/**
 * Add kafka bean configurations
 */

@Configuration
@AllArgsConstructor
public class KafkaConfiguration {

    private final Gson gson;

    private final BookingConfigurationProperties bookingConfigurationProperties;

    @Bean
    public ConcurrentMessageListenerContainer<String, BookingRegisterMessage> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, BookingRegisterMessage> containerFactory, KafkaTemplate<String, BookingRegisterMessage> template) {

        ConcurrentMessageListenerContainer<String, BookingRegisterMessage> repliesContainer =
                containerFactory.createContainer(getTopicName());
        containerFactory.setReplyTemplate(template);
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        recoverer.setHeadersFunction(additionalHeaders());
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        containerFactory.setErrorHandler(new SeekToCurrentErrorHandler(
                recoverer, new FixedBackOff(FixedBackOff.DEFAULT_INTERVAL, 3L)));
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }

    private String getTopicName() {
        return bookingConfigurationProperties.getRegister().getProcess().getTopic();
    }

    @NotNull
    private BiFunction<ConsumerRecord<?, ?>, Exception, Headers> additionalHeaders() {
        return (consumerRecord, exception) -> {
            var headers = new RecordHeaders();
            headers.add(CustomKafkaHeaders.DLT_EXCEPTION_MESSAGE_ROOT, ExceptionUtils.getRootCause(exception).getMessage().getBytes(StandardCharsets.UTF_8));
            headers.add(CustomKafkaHeaders.DLT_EXCEPTION_STACKTRACE_ROOT, ExceptionUtils.getStackTrace(exception).getBytes(StandardCharsets.UTF_8));
            headers.add(CustomKafkaHeaders.DLT_EXCEPTION_ROOT, gson.toJson(exception).getBytes(StandardCharsets.UTF_8));
            return headers;
        };
    }

    @Configuration
    public static class TemplateConfiguration {

        @Bean
        public KafkaTemplate<String, BookingRegisterMessage> template(
                ProducerFactory<String, BookingRegisterMessage> pf) {
            return new KafkaTemplate<>(pf);
        }

    }
}
