package com.alejandrolaban.bookingapi.infrastructure.email;

import com.alejandrolaban.bookingapi.application.BookingConfigurationProperties;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * Sendgrid implementation to send emails
 */

@Configuration
@AllArgsConstructor
public class SendgridMailService implements MailService {

    public static final String MAIL_SEND = "mail/send";
    public static final String SUBJECT = "subject";
    private final BookingConfigurationProperties bookingConfigurationProperties;

    @Override
    public void send(String email, Map<String, Object> parameters) throws IOException {
        BookingConfigurationProperties.Sendgrid sendgrid = bookingConfigurationProperties.getRegister().getSendgrid();

        Email from = new Email(sendgrid.getSenderMail());
        String subject = sendgrid.getSubject();
        Email to = new Email(email);

        Mail mail = new Mail();
        mail.setFrom(from);
        Personalization personalization = new Personalization();
        personalization.addTo(to);

        parameters.forEach(personalization::addDynamicTemplateData);
        personalization.addDynamicTemplateData(SUBJECT, subject);
        mail.addPersonalization(personalization);
        mail.setTemplateId(sendgrid.getTemplateId());

        SendGrid sg = new SendGrid(sendgrid.getApiKey());
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint(MAIL_SEND);
        request.setBody(mail.build());
        sg.api(request);
    }
}
