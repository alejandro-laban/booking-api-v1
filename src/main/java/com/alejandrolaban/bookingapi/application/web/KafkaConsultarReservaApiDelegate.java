package com.alejandrolaban.bookingapi.application.web;

import com.alejandrolaban.bookingapi.api.ConsultarReservaApiDelegate;
import com.alejandrolaban.bookingapi.domain.register.mapper.BookingMapper;
import com.alejandrolaban.bookingapi.domain.search.SearchBooking;
import com.alejandrolaban.bookingapi.model.BookingResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class KafkaConsultarReservaApiDelegate implements ConsultarReservaApiDelegate {

    private final SearchBooking searchBooking;
    private final BookingMapper bookingMapper;

    @Override
    public Mono<ResponseEntity<Flux<BookingResponse>>> getAll(ServerWebExchange exchange) {
        var search = searchBooking.findAll()
                .map(bookingMapper::bookingDocumentToBooking);

        return Mono.just(search)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<ResponseEntity<BookingResponse>> getById(String id, ServerWebExchange exchange) {
        return searchBooking.findById(id).map(bookingMapper::bookingDocumentToBooking)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

