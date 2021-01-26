package com.fitcrew.trainerservice.service.admin;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AdminServiceFacadeImpl implements AdminServiceFacade {

	private final AdminService adminService;

	public AdminServiceFacadeImpl(AdminService adminService) {
		this.adminService = adminService;
	}


	@Override
	public Mono<List<TrainerModel>> getTrainers() {
		return adminService.getTrainers();
	}

	@Override
	public Mono<TrainerModel> updateTrainer(TrainerDto trainerDto, String trainerEmail) {
		return adminService.updateTrainer(trainerDto, trainerEmail);
	}

	@Override
	public Mono<TrainerModel> getTrainer(String trainerEmail) {
		return adminService.getTrainer(trainerEmail);
	}

	@Override
	public Mono<TrainerModel> deleteTrainer(String trainerEmail) {
		return adminService.deleteTrainer(trainerEmail);
	}
}
