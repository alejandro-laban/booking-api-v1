package com.alejandrolaban.bookingapi.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties("booking")
@Configuration
public class BookingConfigurationProperties {

    private Register register;
    private Search search;

    @Getter
    @Setter
    public static class Register {
        private Sendgrid sendgrid;
        private Topic process;
        private Topic success;
        private Topic failure;
    }

    @Getter
    @Setter
    public static class Search {
        private String topic;
    }

    @Getter
    @Setter
    public static class Sendgrid {
        private String senderMail;
        private String apiKey;
        private String templateId;
        private String subject;
    }

    @Getter
    @Setter
    public static class Topic {
        private String topic;
    }
}
