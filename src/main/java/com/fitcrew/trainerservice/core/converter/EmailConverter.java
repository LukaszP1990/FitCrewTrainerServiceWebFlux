package com.fitcrew.trainerservice.core.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.trainerservice.domains.EmailDocument;
import com.fitcrew.trainerservice.dto.EmailDto;

@Mapper
public interface EmailConverter {

	@Mapping(target = "id", ignore = true)
	EmailDocument emailDtoToEmailDocument(EmailDto emailDto);

	@Mapping(target = "id", ignore = true)
	EmailDocument emailModelToEmailDocument(EmailModel emailModel);

	EmailModel emailDocumentToEmailModel(EmailDocument emailDocument);
}
