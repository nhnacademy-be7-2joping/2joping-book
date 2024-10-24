package com.nhnacademy.bookstore.bookset.contributor.controller;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/bookstore/contributors")
@RequiredArgsConstructor
public class ContributorController {
    private final ContributorService contributorService;

    @PostMapping
    public ResponseEntity<ContributorResponseDto> createContributor(@RequestBody @Valid ContributorRequestDto dto) {
        ContributorResponseDto createdContributor = contributorService.createContributor(dto);
        return ResponseEntity.ok(createdContributor);
    }

    @GetMapping("/{contributorId}")
    public ResponseEntity<ContributorResponseDto> getContributor(@PathVariable Long contributorId) {
        ContributorResponseDto contributor = contributorService.getContributor(contributorId);
        return ResponseEntity.ok(contributor);
    }

    @PutMapping("/{contributorId}")
    public ResponseEntity<ContributorResponseDto> updateContributor(@PathVariable Long contributorId, @RequestBody @Valid ContributorRequestDto dto) {
        ContributorResponseDto updatedContributor = contributorService.updateContributor(contributorId, dto);
        return ResponseEntity.ok(updatedContributor);
    }

    @DeleteMapping("/{contributorId}")
    public ResponseEntity<Void> deleteContributor(@PathVariable Long contributorId) {
        contributorService.deleteContributorById(contributorId);
        return ResponseEntity.noContent().build();
    }
}
