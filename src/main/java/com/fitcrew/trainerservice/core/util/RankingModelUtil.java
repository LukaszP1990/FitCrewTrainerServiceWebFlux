package com.fitcrew.trainerservice.core.util;

import com.fitcrew.FitCrewAppModel.domain.model.RankingModel;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RankingModelUtil {

    public static RankingModel getRankingModel(TrainerDocument trainerDocument) {
        return RankingModel.builder()
                .trainerFirstName(trainerDocument.getFirstName())
                .trainerLastName(trainerDocument.getLastName())
                .placeInTheRanking(Integer.valueOf(trainerDocument.getPlaceInTheRanking()))
                .build();
    }
}
