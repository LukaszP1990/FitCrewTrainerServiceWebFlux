package com.fitcrew.trainerservice.resource.admin;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.service.admin.AdminServiceFacade;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/trainer")
public class AdminResource {

	private final AdminServiceFacade adminServiceFacade;

	public AdminResource(AdminServiceFacade adminServiceFacade) {
		this.adminServiceFacade = adminServiceFacade;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(path = "/admin/trainers", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<List<TrainerModel>> getTrainers() {
		return adminServiceFacade.getTrainers();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(path = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<TrainerModel> deleteTrainer(@RequestParam("trainer-email") String trainerEmail) {
		return adminServiceFacade.deleteTrainer(trainerEmail);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(path = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<TrainerModel> updateTrainer(@RequestBody TrainerDto trainerDto,
											@RequestParam("trainer-email") String trainerEmail) {
		return adminServiceFacade.updateTrainer(trainerDto, trainerEmail);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(path = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<TrainerModel> getTrainer(@RequestParam("trainer-email") String trainerEmail) {
		return adminServiceFacade.getTrainer(trainerEmail);
	}
}
