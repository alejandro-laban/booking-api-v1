package com.alejandrolaban.bookingapi.domain.register.email;

import lombok.Value;
import lombok.With;

@Value
public class EmailDynamicData {

    String checkingInDate;
    String checkingOutDate;
    String peopleNumber;
    String guestName;
    int roomNumber;
    int minorNumber;

    @With
    Boolean isSuccessful;
    @With
    String errorDetail;
}
