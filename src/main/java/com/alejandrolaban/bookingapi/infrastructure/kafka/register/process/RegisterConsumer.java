package com.alejandrolaban.bookingapi.infrastructure.kafka.register.process;


import com.alejandrolaban.booking.register.BookingRegisterMessage;
import com.alejandrolaban.bookingapi.domain.register.mapper.BookingMapper;
import com.alejandrolaban.bookingapi.domain.register.repository.BookingCosmoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@AllArgsConstructor
public class RegisterConsumer {

    private final BookingCosmoRepository bookingCosmoRepository;
    private final BookingMapper bookingMapper;

    @SendTo("#{bookingConfigurationProperties.register.success.topic}")
    @KafkaListener(topics = "${booking.register.process.topic}")
    BookingRegisterMessage registerListener(ConsumerRecord<String, BookingRegisterMessage> bookingRegisterMessage, Acknowledgment acknowledgment) {
        return Mono.just(bookingRegisterMessage)
                .map(ConsumerRecord::value)
                .map(bookingMapper::bookingRegisterMessageToBookingDocumentValid)
                .flatMap(bookingCosmoRepository::save)
                .map(bookingDocument -> bookingRegisterMessage.value())
                .doOnSuccess(bookingDocument -> acknowledgment.acknowledge())
                .subscribeOn(Schedulers.boundedElastic()).block();
    }

}
