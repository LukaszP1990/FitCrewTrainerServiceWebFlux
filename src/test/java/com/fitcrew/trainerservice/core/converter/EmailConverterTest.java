package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.trainerservice.domains.EmailDocument;
import com.fitcrew.trainerservice.util.EmailUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.fitcrew.trainerservice.util.EmailUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class EmailConverterTest {

    private final EmailConverter emailConverter = Mappers.getMapper(EmailConverter.class);

    @Test
    void shouldConvertEmailDtoToEmailDocument() {
        //given
        var emailDto = EmailUtil.getEmailDto();

        //when
        var emailDocument = emailConverter.emailDtoToEmailDocument(emailDto);

        //then
        checkEmailDocumentAssertions(emailDocument);
    }

    @Test
    void shouldConvertEmailDocumentToEmailModel() {
        //given
        var emailDocument = EmailUtil.getEmailDocument();

        //when
        var emailModel = emailConverter.emailDocumentToEmailModel(emailDocument);

        //then
        checkEmailModelAssertions(emailModel);
    }

    @Test
    void shouldConvertEmailModelToEmailDocument() {
        //given
        var emailModel = EmailUtil.getEmailModel();

        //when
        var emailDocument = emailConverter.emailModelToEmailDocument(emailModel);

        //then
        checkEmailDocumentAssertions(emailDocument);
    }

    private void checkEmailDocumentAssertions(EmailDocument emailDocument) {
        assertAll(() -> {
            assertNotNull(emailDocument);
            assertEquals(SENDER, emailDocument.getSender());
            assertEquals(RECIPIENT, emailDocument.getRecipient());
            assertEquals(SUBJECT, emailDocument.getSubject());
            assertEquals(BODY_OF_MESSAGE, emailDocument.getBodyOfMessage());
            assertNull(emailDocument.getId());
        });
    }

    private void checkEmailModelAssertions(com.fitcrew.FitCrewAppModel.domain.model.EmailModel emailModel) {
        assertAll(() -> {
            assertNotNull(emailModel);
            assertEquals(SENDER, emailModel.getSender());
            assertEquals(RECIPIENT, emailModel.getRecipient());
            assertEquals(SUBJECT, emailModel.getSubject());
            assertEquals(BODY_OF_MESSAGE, emailModel.getBodyOfMessage());
        });
    }
}