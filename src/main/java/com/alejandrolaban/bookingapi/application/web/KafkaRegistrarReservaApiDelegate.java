package com.alejandrolaban.bookingapi.application.web;

import com.alejandrolaban.bookingapi.api.RegistrarReservaApiDelegate;
import com.alejandrolaban.bookingapi.infrastructure.kafka.register.process.RegisterProducer;
import com.alejandrolaban.bookingapi.model.Booking;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class KafkaRegistrarReservaApiDelegate implements RegistrarReservaApiDelegate {

    private final RegisterProducer registerProducer;

    @Override
    public Mono<ResponseEntity<Void>> addBooking(Mono<Booking> booking, ServerWebExchange exchange) {
        return booking.flatMap(registerProducer::register)
                .map(ListenableFuture::completable)
                .flatMap(Mono::fromFuture)
                .map(result -> new ResponseEntity<Void>(HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST)));
    }
}
