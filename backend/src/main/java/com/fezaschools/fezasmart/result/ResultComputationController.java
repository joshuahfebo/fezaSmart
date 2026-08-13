package com.fezaschools.fezasmart.result;

import com.fezaschools.fezasmart.security.RequireRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results/compute")
public class ResultComputationController {

    private final ResultComputationService resultComputationService;

    public ResultComputationController(ResultComputationService resultComputationService) {
        this.resultComputationService = resultComputationService;
    }

    @PostMapping("/exam/{examId}")
    @RequireRole({"SUPER_ADMIN", "HEAD_MASTER", "SECOND_MASTER"})
    public ResponseEntity<List<ResultDTO>> computeResultsForExam(@PathVariable Integer examId) {
        return ResponseEntity.ok(resultComputationService.computeResultsForExam(examId));
    }

    @PostMapping("/exam/{examId}/student/{studentId}")
    @RequireRole({"SUPER_ADMIN", "HEAD_MASTER", "SECOND_MASTER"})
    public ResponseEntity<ResultDTO> computeResultForStudentInExam(
            @PathVariable Integer studentId,
            @PathVariable Integer examId) {
        return ResponseEntity.ok(resultComputationService.computeResultForStudentInExam(studentId, examId));
    }

    @GetMapping("/exam/{examId}")
    @RequireRole({"SUPER_ADMIN", "HEAD_MASTER", "GENERAL_SECOND_MASTER", "SECOND_MASTER"})
    public ResponseEntity<List<ResultDTO>> getResultsForExam(@PathVariable Integer examId) {
        return ResponseEntity.ok(resultComputationService.getResultsForExam(examId));
    }
}
