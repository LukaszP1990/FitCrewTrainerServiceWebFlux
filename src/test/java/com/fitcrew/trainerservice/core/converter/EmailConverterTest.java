package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.FitCrewAppModel.domain.dto.EmailDto;
import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.trainerservice.domains.EmailDocument;
import com.fitcrew.trainerservice.util.EmailUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.fitcrew.trainerservice.util.EmailUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class EmailConverterTest {

    private static final EmailDocument emailDocument = EmailUtil.getEmailDocument();
    private static final EmailModel emailModel = EmailUtil.getEmailModel();
    private static final EmailDto emailDto = EmailUtil.getEmailDto();
    private final EmailConverter emailConverter = Mappers.getMapper(EmailConverter.class);
    
    @Test
    void shouldConvertEmailDtoToEmailDocument() {
        var emailDocument = emailConverter.emailDtoToEmailDocument(emailDto);
        assertNotNull(emailDocument);
        checkEmailDocumentAssertions(emailDocument);
    }
    
    @Test
    void shouldConvertEmailDocumentToEmailModel() {
        var emailModel = emailConverter.emailDocumentToEmailModel(emailDocument);
        assertNotNull(emailModel);
        checkEmailModelAssertions(emailModel);
    }

    @Test
    void shouldConvertEmailModelToEmailDocument() {
        EmailDocument emailDocument = emailConverter.emailModelToEmailDocument(emailModel);
        assertNotNull(emailDocument);
        checkEmailDocumentAssertions(emailDocument);
    }

    private void checkEmailDocumentAssertions(EmailDocument emailDocument) {
        assertAll(() -> {
            assertEquals(SENDER, emailDocument.getSender());
            assertEquals(RECIPIENT, emailDocument.getRecipient());
            assertEquals(SUBJECT, emailDocument.getSubject());
            assertEquals(BODY_OF_MESSAGE, emailDocument.getBodyOfMessage());
            assertNull(emailDocument.getId());
        });
    }

    private void checkEmailModelAssertions(com.fitcrew.FitCrewAppModel.domain.model.EmailModel emailModel) {
        assertAll(() -> {
            assertEquals(SENDER, emailModel.getSender());
            assertEquals(RECIPIENT, emailModel.getRecipient());
            assertEquals(SUBJECT, emailModel.getSubject());
            assertEquals(BODY_OF_MESSAGE, emailModel.getBodyOfMessage());
        });
    }
}