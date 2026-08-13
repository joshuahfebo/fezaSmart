package com.fezaschools.fezasmart.transcript;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transcripts")
public class TranscriptController {

    private final TranscriptService transcriptService;

    public TranscriptController(TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateTranscript(@RequestBody Map<String, Integer> request) {
        Integer studentId = request.get("studentId");
        Integer academicYearId = request.get("academicYearId");
        Integer id = transcriptService.generateTranscript(studentId, academicYearId);
        return new ResponseEntity<>(Map.of("id", id), HttpStatus.CREATED);
    }
}
