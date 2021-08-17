package com.alejandrolaban.bookingapi.infrastructure.email;

import com.sendgrid.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Interface to send mail
 */
public interface MailService {

    /**
     * Send email
     * @param email
     * @param parameters
     * @return sendgrid response object
     * @throws IOException
     */
    void send(String email, Map<String, Object> parameters) throws IOException;
}
