package com.nhnacademy.bookstore.bookset.contributor.controller;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/bookstore/contributors/role")
@RequiredArgsConstructor
public class ContributorRoleController {
    private final ContributorRoleService contributorRoleService;

    @PostMapping
    public ResponseEntity<ContributorRoleResponseDto> createContributorRole(@RequestBody @Valid ContributorRoleRequestDto dto) {
        ContributorRoleResponseDto createdRole = contributorRoleService.createContributorRole(dto);
        return ResponseEntity.ok(createdRole);
    }

    @GetMapping("/{contributorRoleId}")
    public ResponseEntity<ContributorRoleResponseDto> getContributorRole(@PathVariable Long contributorRoleId) {
        ContributorRoleResponseDto contributorRole = contributorRoleService.getContributorRole(contributorRoleId);
        return ResponseEntity.ok(contributorRole);
    }

    @PutMapping("/{contributorRoleId}")
    public ResponseEntity<ContributorRoleResponseDto> updateContributorRole(@PathVariable Long contributorRoleId, @RequestBody @Valid ContributorRoleRequestDto dto) {
        ContributorRoleResponseDto updatedRole = contributorRoleService.updateContributorRole(contributorRoleId, dto);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{contributorRoleId}")
    public ResponseEntity<Void> deleteContributorRole(@PathVariable Long contributorRoleId) {
        contributorRoleService.deleteContributorRole(contributorRoleId);
        return ResponseEntity.noContent().build();
    }
}
