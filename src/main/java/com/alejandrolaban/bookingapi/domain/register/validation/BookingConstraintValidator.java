package com.alejandrolaban.bookingapi.domain.register.validation;

import com.alejandrolaban.bookingapi.domain.register.repository.BookingDocument;
import org.jetbrains.annotations.NotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Predicate;

import static java.time.temporal.ChronoUnit.DAYS;

public class BookingConstraintValidator implements ConstraintValidator<ValidBooking, BookingDocument> {

    @Override
    public boolean isValid(BookingDocument bookingDocument, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.of(bookingDocument)
                .filter(hasValidCheckingInDate()
                        .and(hasValidCheckOutDate()))
                .map(booking -> Boolean.TRUE)
                .orElse(Boolean.FALSE);
    }

    @NotNull
    private Predicate<BookingDocument> hasValidCheckOutDate() {
        return booking -> booking.getCheckingInDate().until(booking.getCheckingOutDate(), DAYS) > 0;
    }

    @NotNull
    private Predicate<BookingDocument> hasValidCheckingInDate() {
        return booking -> Instant.now().until(booking.getCheckingInDate(), DAYS) >= 0;
    }
}
