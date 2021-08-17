package com.alejandrolaban.bookingapi.domain.search;

import com.alejandrolaban.bookingapi.domain.register.repository.BookingCosmoRepository;
import com.alejandrolaban.bookingapi.domain.register.repository.BookingDocument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class CosmosDbSearchBooking implements SearchBooking {

    private final BookingCosmoRepository bookingCosmoRepository;

    @Override
    public Flux<BookingDocument> findAll() {
        return bookingCosmoRepository.findAll();
    }

    @Override
    public Mono<BookingDocument> findById(String id) {
        return bookingCosmoRepository.findById(id);
    }
}
