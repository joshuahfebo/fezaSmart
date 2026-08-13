package com.fezaschools.fezasmart.club_member;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/clubMembers", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClubMemberResource {

    private final ClubMemberService clubMemberService;

    public ClubMemberResource(final ClubMemberService clubMemberService) {
        this.clubMemberService = clubMemberService;
    }

    @GetMapping
    public ResponseEntity<List<ClubMemberDTO>> getAllClubMembers() {
        return ResponseEntity.ok(clubMemberService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubMemberDTO> getClubMember(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(clubMemberService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createClubMember(
            @RequestBody @Valid final ClubMemberDTO clubMemberDTO) {
        final Integer createdId = clubMemberService.create(clubMemberDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateClubMember(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ClubMemberDTO clubMemberDTO) {
        clubMemberService.update(id, clubMemberDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClubMember(@PathVariable(name = "id") final Integer id) {
        clubMemberService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
