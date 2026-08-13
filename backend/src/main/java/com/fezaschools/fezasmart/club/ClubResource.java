package com.fezaschools.fezasmart.club;

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
@RequestMapping(value = "/api/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClubResource {

    private final ClubService clubService;

    public ClubResource(final ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping
    public ResponseEntity<List<ClubDTO>> getAllClubs() {
        return ResponseEntity.ok(clubService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> getClub(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(clubService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createClub(@RequestBody @Valid final ClubDTO clubDTO) {
        final Integer createdId = clubService.create(clubDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateClub(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ClubDTO clubDTO) {
        clubService.update(id, clubDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable(name = "id") final Integer id) {
        clubService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
