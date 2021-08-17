package com.alejandrolaban.bookingapi.domain.search;

import com.alejandrolaban.bookingapi.domain.register.repository.BookingDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SearchBooking {

    Flux<BookingDocument> findAll();

    Mono<BookingDocument> findById(String id);

}
