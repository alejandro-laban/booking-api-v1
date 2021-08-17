package com.alejandrolaban.bookingapi.domain.register.repository;

import com.alejandrolaban.bookingapi.domain.register.validation.ValidBooking;
import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Value;
import org.springframework.data.annotation.Id;

import java.time.Instant;

/**
 * Booking document representation on cosmosdb
 */

@ValidBooking
@Value
@Container(containerName = "booking")
public class BookingDocument {

    @Id
    @GeneratedValue
    String id;
    @PartitionKey
    String email;
    Instant checkingInDate;
    Instant checkingOutDate;
    int peopleNumber;
    String guestName;
    int roomNumber;
    int minorNumber;
}
