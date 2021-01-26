package com.fitcrew.trainerservice.util;

import com.fitcrew.FitCrewAppModel.domain.dto.EmailDto;
import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.trainerservice.domains.EmailDocument;

public class EmailUtil {

    public static final String SENDER = "sender";
    public static final String RECIPIENT = "firstName lastName";
    public static final String SUBJECT = "message";
    public static final String BODY_OF_MESSAGE = "body of message";

    public static EmailModel getEmailModel() {
        return EmailModel.builder()
                .sender(SENDER)
                .recipient(RECIPIENT)
                .subject(SUBJECT)
                .bodyOfMessage(BODY_OF_MESSAGE)
                .build();
    }

    public static EmailDto getEmailDto() {
        return EmailDto.builder()
                .sender(SENDER)
                .recipient(RECIPIENT)
                .subject(SUBJECT)
                .bodyOfMessage(BODY_OF_MESSAGE)
                .build();
    }

    public static EmailDocument getEmailDocument() {
        return EmailDocument.builder()
                .sender(SENDER)
                .recipient(RECIPIENT)
                .subject(SUBJECT)
                .bodyOfMessage(BODY_OF_MESSAGE)
                .build();
    }
}
