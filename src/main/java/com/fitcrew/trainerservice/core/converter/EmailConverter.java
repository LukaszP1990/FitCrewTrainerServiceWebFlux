package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.FitCrewAppModel.domain.dto.EmailDto;
import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.trainerservice.domains.EmailDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmailConverter {

	@Mapping(target = "id", ignore = true)
	EmailDocument emailDtoToEmailDocument(EmailDto emailDto);

	@Mapping(target = "id", ignore = true)
	EmailDocument emailModelToEmailDocument(EmailModel emailModel);

	EmailModel emailDocumentToEmailModel(EmailDocument emailDocument);
}
