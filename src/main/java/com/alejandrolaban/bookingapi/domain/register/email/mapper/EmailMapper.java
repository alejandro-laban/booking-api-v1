package com.alejandrolaban.bookingapi.domain.register.email.mapper;

import com.alejandrolaban.booking.register.BookingRegisterMessage;
import com.alejandrolaban.bookingapi.domain.register.email.EmailDynamicData;
import com.alejandrolaban.bookingapi.domain.register.repository.BookingDocument;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EmailMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME
            .withZone(ZoneId.systemDefault());

    EmailDynamicData bookingDocumentToEmailDynamicData(BookingDocument bookingDocument);

    EmailDynamicData bookingRegisterMessageToEmailDynamicData(BookingRegisterMessage bookingRegisterMessage);

    default String instantToString(Instant instant) {
        return FORMATTER.format(instant);
    }
}
