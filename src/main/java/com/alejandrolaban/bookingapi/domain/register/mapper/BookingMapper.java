package com.alejandrolaban.bookingapi.domain.register.mapper;

import com.alejandrolaban.booking.register.BookingRegisterMessage;
import com.alejandrolaban.bookingapi.domain.register.repository.BookingDocument;
import com.alejandrolaban.bookingapi.model.Booking;
import com.alejandrolaban.bookingapi.model.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Validated
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookingMapper {

    @Valid
    BookingDocument bookingRegisterMessageToBookingDocumentValid(BookingRegisterMessage bookingRegisterMessage);

    BookingDocument bookingRegisterMessageToBookingDocument(BookingRegisterMessage bookingRegisterMessage);

    BookingRegisterMessage bookingDocumentToBookingRegisterMessage(BookingDocument bookingDocument);

    Booking bookingRegisterMessageToBooking(BookingRegisterMessage bookingRegisterMessage);

    BookingRegisterMessage bookingToBookingRegisterMessage(Booking booking);

    default Instant offsetDateTimeToInstant(OffsetDateTime value) {
        return value.toInstant();
    }

    default OffsetDateTime instantToOffsetDateTime(Instant value) {
        return ZonedDateTime.from(value.atZone(ZoneId.systemDefault())).toOffsetDateTime();
    }

    BookingResponse bookingDocumentToBooking(BookingDocument bookingDocument);
}
