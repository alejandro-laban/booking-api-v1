package com.alejandrolaban.bookingapi.infrastructure.kafka.register.failure;

import com.alejandrolaban.booking.register.BookingRegisterMessage;
import com.alejandrolaban.bookingapi.domain.register.email.EmailHandler;
import com.alejandrolaban.bookingapi.domain.register.mapper.BookingMapper;
import com.alejandrolaban.bookingapi.domain.register.repository.BookingDocument;
import com.alejandrolaban.bookingapi.infrastructure.exception.ExceptionUtil;
import com.alejandrolaban.bookingapi.infrastructure.kafka.CustomKafkaHeaders;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Failure consumer on DLT topic
 */

@Component
@AllArgsConstructor
public class FailureRegisterConsumer {

    private final BookingMapper bookingMapper;
    private final EmailHandler emailHandler;
    private final ExceptionUtil exceptionUtil;

    @KafkaListener(topics = "${booking.register.failure.topic}")
    void registerListener(ConsumerRecord<String, BookingRegisterMessage> bookingRegisterMessage, Acknowledgment acknowledgment) {
        Mono.just(bookingRegisterMessage)
                .map(message -> bookingMapper.bookingRegisterMessageToBookingDocument(message.value()))
                .doOnSuccess(onSuccess(bookingRegisterMessage, acknowledgment))
                .subscribeOn(Schedulers.boundedElastic()).block();
    }

    @NotNull
    private Consumer<BookingDocument> onSuccess(ConsumerRecord<String, BookingRegisterMessage> bookingRegisterMessage, Acknowledgment acknowledgment) {
        return bookingDocument -> {
            emailHandler.sendFailure(bookingRegisterMessage.value(), getThrowable(bookingRegisterMessage));
            acknowledgment.acknowledge();
        };
    }

    @NotNull
    private MessageDeliveryException getThrowable(ConsumerRecord<String, BookingRegisterMessage> bookingRegisterMessage) {

        var exceptionClass = Arrays.stream(bookingRegisterMessage.headers().toArray())
                .filter(header -> header.key().equals(KafkaHeaders.DLT_EXCEPTION_FQCN))
                .findFirst()
                .map(Header::value)
                .map(String::new)
                .orElse(Throwable.class.getCanonicalName());

        return new MessageDeliveryException(Arrays.stream(bookingRegisterMessage.headers().toArray())
                .filter(header -> header.key().equals(CustomKafkaHeaders.DLT_EXCEPTION_ROOT))
                .findFirst()
                .map(Header::value)
                .map(String::new)
                .map(json -> exceptionUtil.getMessageFromThrowableJson(json, exceptionClass))
                .orElse("Unable to process message"));
    }

}
