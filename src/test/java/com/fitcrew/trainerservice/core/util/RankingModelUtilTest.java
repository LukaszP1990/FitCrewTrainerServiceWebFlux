package com.fitcrew.trainerservice.core.util;

import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_FIRST_NAME;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_LAST_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RankingModelUtilTest {

    private static final TrainerDocument trainerDocument = TrainerUtil.getTrainerDocument();

    @Test
    void shouldGetRankingModel() {
        var rankingModel = RankingModelUtil.getRankingModel(trainerDocument);
        assertNotNull(rankingModel);
        assertAll(() -> {
            assertEquals(TRAINER_FIRST_NAME, rankingModel.getTrainerFirstName());
            assertEquals(TRAINER_LAST_NAME, rankingModel.getTrainerLastName());
            assertEquals(1, rankingModel.getPlaceInTheRanking());
        });
    }
}