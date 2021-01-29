package com.fitcrew.trainerservice.core.util;

import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_FIRST_NAME;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_LAST_NAME;
import static org.junit.jupiter.api.Assertions.*;

class RankingModelUtilTest {

    @Test
    void shouldGetRankingModel() {
        //given
        var trainerDocument = TrainerUtil.getTrainerDocument();

        //when
        var rankingModel = RankingModelUtil.getRankingModel(trainerDocument);

        //then
        assertAll(() -> {
            assertNotNull(rankingModel);
            assertEquals(TRAINER_FIRST_NAME, rankingModel.getTrainerFirstName());
            assertEquals(TRAINER_LAST_NAME, rankingModel.getTrainerLastName());
            assertEquals(1, rankingModel.getPlaceInTheRanking());
        });
    }
}