package com.fezaschools.fezasmart.club_member;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/club-members")
public class ClubMemberController {

    private final ClubMemberService clubMemberService;

    public ClubMemberController(ClubMemberService clubMemberService) {
        this.clubMemberService = clubMemberService;
    }

    @GetMapping
    public ResponseEntity<List<ClubMemberDTO>> findAll() {
        return ResponseEntity.ok(clubMemberService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubMemberDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(clubMemberService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid ClubMemberDTO clubMemberDTO) {
        return new ResponseEntity<>(clubMemberService.create(clubMemberDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ClubMemberDTO clubMemberDTO) {
        clubMemberService.update(id, clubMemberDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clubMemberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
