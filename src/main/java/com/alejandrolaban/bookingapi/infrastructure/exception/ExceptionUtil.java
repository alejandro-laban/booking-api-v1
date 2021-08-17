package com.alejandrolaban.bookingapi.infrastructure.exception;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * Exception util for serialization and deserialization
 */

@Component
@AllArgsConstructor
public class ExceptionUtil {

    private final Gson gson;

    @SneakyThrows
    public Throwable getThrowableFromJson(String json, String className) {
        Class<?> clazz = ClassUtils.forName(className, Thread.currentThread().getContextClassLoader());
        return (Throwable) gson.fromJson(json, clazz);
    }

    public String getMessageFromThrowableJson(String json, String className) {
        var throwable = getThrowableFromJson(json, className);
        return getMessageFromThrowable(throwable);
    }

    public String getMessageFromThrowable(Throwable throwable) {
        String messageDetail = throwable.getMessage();
        if (throwable instanceof ConstraintViolationException) {
            messageDetail = ((ConstraintViolationException) throwable).getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(StringUtils.LF));
        }

        return messageDetail;
    }
}
