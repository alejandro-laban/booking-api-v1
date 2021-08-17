package com.alejandrolaban.bookingapi.infrastructure.email;

import com.sendgrid.Response;

import java.io.IOException;
import java.util.Map;

public interface MailService {

    Response send(String email, Map<String, Object> parameters) throws IOException;
}
