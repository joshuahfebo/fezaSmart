package com.fezaschools.fezasmart.transcript;

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
@RequestMapping(value = "/api/transcripts", produces = MediaType.APPLICATION_JSON_VALUE)
public class TranscriptResource {

    private final TranscriptService transcriptService;

    public TranscriptResource(final TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }

    @GetMapping
    public ResponseEntity<List<TranscriptDTO>> getAllTranscripts() {
        return ResponseEntity.ok(transcriptService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranscriptDTO> getTranscript(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(transcriptService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createTranscript(
            @RequestBody @Valid final TranscriptDTO transcriptDTO) {
        final Integer createdId = transcriptService.create(transcriptDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateTranscript(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final TranscriptDTO transcriptDTO) {
        transcriptService.update(id, transcriptDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTranscript(@PathVariable(name = "id") final Integer id) {
        transcriptService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
