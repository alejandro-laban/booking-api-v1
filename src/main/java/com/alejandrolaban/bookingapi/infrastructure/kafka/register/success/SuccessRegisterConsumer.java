package com.alejandrolaban.bookingapi.infrastructure.kafka.register.success;

import com.alejandrolaban.booking.register.BookingRegisterMessage;
import com.alejandrolaban.bookingapi.domain.register.email.EmailHandler;
import com.alejandrolaban.bookingapi.domain.register.mapper.BookingMapper;
import com.alejandrolaban.bookingapi.domain.register.repository.BookingDocument;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Consumer;

@Component
@AllArgsConstructor
public class SuccessRegisterConsumer {

    private final BookingMapper bookingMapper;
    private final EmailHandler emailHandler;

    @KafkaListener(topics = "${booking.register.success.topic}")
    void registerListener(BookingRegisterMessage bookingRegisterMessage, Acknowledgment acknowledgment) {
        Mono.just(bookingRegisterMessage)
                .map(bookingMapper::bookingRegisterMessageToBookingDocument)
                .doOnSuccess(onSuccess(acknowledgment))
                .subscribeOn(Schedulers.boundedElastic()).block();
    }

    @NotNull
    private Consumer<BookingDocument> onSuccess(Acknowledgment acknowledgment) {
        return bookingDocument -> {
            emailHandler.sendSuccessful(bookingDocument);
            acknowledgment.acknowledge();
        };
    }
}
