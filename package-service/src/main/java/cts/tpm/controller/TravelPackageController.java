package cts.tpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cts.tpm.model.TravelPackageDto;
import cts.tpm.service.TravelPackageServiceImpl;
import cts.tpm.service.FileUploadService;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class TravelPackageController {

	@Autowired
	private TravelPackageServiceImpl travelPackageService;
	
	@Autowired
	private FileUploadService fileUploadService;

	@PostMapping
	public ResponseEntity<TravelPackageDto> createPackage(@Valid @RequestBody TravelPackageDto travelPackageDto) {
		return ResponseEntity.ok(travelPackageService.createPackage(travelPackageDto));
	}
	
	@PostMapping("/{id}/image")
	public ResponseEntity<String> uploadPackageImage(
			@PathVariable Long id,
			@RequestParam("image") MultipartFile image,
			@RequestParam(value = "isMain", defaultValue = "false") boolean isMain) {
		try {
			String imagePath = fileUploadService.uploadImage(image);
			travelPackageService.updatePackageImage(id, imagePath, isMain);
			return ResponseEntity.ok(imagePath);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
		}
	}
	
	@PostMapping("/{id}/images")
	public ResponseEntity<List<String>> uploadPackageImages(
			@PathVariable Long id,
			@RequestParam("images") MultipartFile[] images) {
		try {
			List<String> imagePaths = travelPackageService.addPackageImages(id, images);
			return ResponseEntity.ok(imagePaths);
		} catch (IOException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<TravelPackageDto>> getAllPackages() {
		return ResponseEntity.ok(travelPackageService.getAllActivePackages());
	}

	@GetMapping("/all")
	public ResponseEntity<List<TravelPackageDto>> getAllPackagesIncludingInactive() {
		return ResponseEntity.ok(travelPackageService.getAllPackages());
	}
	
	@GetMapping("/agent/{agentId}")
	public ResponseEntity<List<TravelPackageDto>> getPackagesByAgent(@PathVariable Long agentId) {
		return ResponseEntity.ok(travelPackageService.getPackagesByAgent(agentId));
	}

	@GetMapping("/search")
	public ResponseEntity<List<TravelPackageDto>> searchPackages(
			@RequestParam(required = false) String destination,
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice) {
		return ResponseEntity.ok(travelPackageService.searchPackages(destination, minPrice, maxPrice));
	}

	@GetMapping("/{id}")
	public ResponseEntity<TravelPackageDto> getPackageById(@PathVariable Long id) {
		return ResponseEntity.ok(travelPackageService.getPackageById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TravelPackageDto> updatePackage(@PathVariable Long id,
			@RequestBody TravelPackageDto travelPackageDto) {
		return ResponseEntity.ok(travelPackageService.updatePackage(id, travelPackageDto));
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<TravelPackageDto> updatePackageStatus(@PathVariable Long id,
			@RequestParam boolean active) {
		return ResponseEntity.ok(travelPackageService.updatePackageStatus(id, active));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
		travelPackageService.deletePackage(id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}/image/{imageIndex}")
	public ResponseEntity<Void> deletePackageImage(@PathVariable Long id, @PathVariable int imageIndex) {
		travelPackageService.deletePackageImage(id, imageIndex);
		return ResponseEntity.noContent().build();
	}
}