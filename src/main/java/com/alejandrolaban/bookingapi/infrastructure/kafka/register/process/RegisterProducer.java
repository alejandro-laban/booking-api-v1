package com.alejandrolaban.bookingapi.infrastructure.kafka.register.process;


import com.alejandrolaban.booking.register.BookingRegisterMessage;
import com.alejandrolaban.bookingapi.application.BookingConfigurationProperties;
import com.alejandrolaban.bookingapi.domain.register.mapper.BookingMapper;
import com.alejandrolaban.bookingapi.model.Booking;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class RegisterProducer {

    private final KafkaTemplate<String, BookingRegisterMessage> kafkaTemplate;
    private final BookingMapper bookingMapper;
    private final BookingConfigurationProperties bookingConfigurationProperties;

    public Mono<ListenableFuture<SendResult<String, BookingRegisterMessage>>> register(Booking booking) {
        return Mono.just(booking)
                .map(bookingMapper::bookingToBookingRegisterMessage)
                .map(send())
                .subscribeOn(Schedulers.boundedElastic());
    }

    @NotNull
    private Function<BookingRegisterMessage, ListenableFuture<SendResult<String, BookingRegisterMessage>>> send() {
        return bookingRegisterMessage -> kafkaTemplate.send(getTopicName(), bookingRegisterMessage);
    }

    private String getTopicName() {
        return bookingConfigurationProperties.getRegister().getProcess().getTopic();
    }

}
