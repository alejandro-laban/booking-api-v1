package com.alejandrolaban.bookingapi.domain.register.email;

import com.alejandrolaban.booking.register.BookingRegisterMessage;
import com.alejandrolaban.bookingapi.domain.register.email.mapper.EmailMapper;
import com.alejandrolaban.bookingapi.domain.register.repository.BookingDocument;
import com.alejandrolaban.bookingapi.infrastructure.email.MailService;
import com.alejandrolaban.bookingapi.infrastructure.exception.ExceptionUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@AllArgsConstructor
public class EmailHandler {

    private final MailService mailService;
    private final Gson gson;
    private final EmailMapper emailMapper;
    private final ExceptionUtil exceptionUtil;

    public void sendSuccessful(BookingDocument bookingDocument) {
        try {
            var emailDynamicData = emailMapper
                    .bookingDocumentToEmailDynamicData(bookingDocument)
                    .withIsSuccessful(Boolean.TRUE);
            mailService.send(bookingDocument.getEmail(), convertToMap(emailDynamicData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> convertToMap(Object o) {
        var json = gson.toJson(o);
        return gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public void sendFailure(BookingRegisterMessage bookingDocument, Throwable throwable) {
        var emailDynamicData = emailMapper
                .bookingRegisterMessageToEmailDynamicData(bookingDocument)
                .withIsSuccessful(Boolean.FALSE)
                .withErrorDetail(exceptionUtil.getMessageFromThrowable(throwable));
        var parameters = convertToMap(emailDynamicData);
        try {
            mailService.send(bookingDocument.getEmail(), parameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
