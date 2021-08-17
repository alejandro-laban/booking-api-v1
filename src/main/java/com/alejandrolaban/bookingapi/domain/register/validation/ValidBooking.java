package com.alejandrolaban.bookingapi.domain.register.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = BookingConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidBooking {

    String message() default "{booking.register.error.date-validation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
