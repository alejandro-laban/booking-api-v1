package com.alejandrolaban.bookingapi.domain.register.repository;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.stereotype.Repository;

/**
 * Cosmosdb repository for bookings
 */

@Repository
public interface BookingCosmoRepository extends ReactiveCosmosRepository<BookingDocument, String> {

}
