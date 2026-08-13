package com.fezaschools.fezasmart.result;

import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.exam_subject.ExamSubject;
import com.fezaschools.fezasmart.grade_boundary.GradeBoundary;
import com.fezaschools.fezasmart.grade_boundary.GradeBoundaryRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student_score.StudentScore;
import com.fezaschools.fezasmart.student_score.StudentScoreRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ResultComputationService {

    private final ResultRepository resultRepository;
    private final StudentScoreRepository studentScoreRepository;
    private final GradeBoundaryRepository gradeBoundaryRepository;

    public ResultComputationService(final ResultRepository resultRepository,
            final StudentScoreRepository studentScoreRepository,
            final GradeBoundaryRepository gradeBoundaryRepository) {
        this.resultRepository = resultRepository;
        this.studentScoreRepository = studentScoreRepository;
        this.gradeBoundaryRepository = gradeBoundaryRepository;
    }

    @Transactional
    public List<ResultDTO> computeResultsForExam(Integer examId) {
        List<StudentScore> allScores = studentScoreRepository.findByExamId(examId);

        Map<Student, List<StudentScore>> scoresByStudent = allScores.stream()
                .collect(Collectors.groupingBy(StudentScore::getStudent));

        List<Result> results = new ArrayList<>();

        for (Map.Entry<Student, List<StudentScore>> entry : scoresByStudent.entrySet()) {
            Student student = entry.getKey();
            List<StudentScore> studentScores = entry.getValue();

            BigDecimal totalScore = studentScores.stream()
                    .map(StudentScore::getScore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int subjectCount = studentScores.size();
            BigDecimal averagePercentage = subjectCount > 0
                    ? totalScore.divide(BigDecimal.valueOf(subjectCount), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal totalPoints = calculatePoints(averagePercentage, examId);

            Result result = resultRepository.findByStudentAndExam(student,
                    studentScores.get(0).getExamSubject().getExam())
                    .orElse(new Result());

            result.setStudent(student);
            result.setExam(studentScores.get(0).getExamSubject().getExam());
            result.setTotalScore(totalScore);
            result.setAveragePercentage(averagePercentage);
            result.setTotalPoints(totalPoints);
            result.setDivision(determineDivision(averagePercentage, examId));
            result.setComputedAt(OffsetDateTime.now());

            results.add(resultRepository.save(result));
        }

        assignRanks(results);

        return results.stream()
                .map(r -> mapToDTO(r, new ResultDTO()))
                .toList();
    }

    @Transactional
    public ResultDTO computeResultForStudentInExam(Integer studentId, Integer examId) {
        List<StudentScore> scores = studentScoreRepository.findByExamIdAndStudentId(examId, studentId);

        if (scores.isEmpty()) {
            throw new NotFoundException("No scores found for this student in this exam");
        }

        Student student = scores.get(0).getStudent();
        Exam exam = scores.get(0).getExamSubject().getExam();

        BigDecimal totalScore = scores.stream()
                .map(StudentScore::getScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int subjectCount = scores.size();
        BigDecimal averagePercentage = subjectCount > 0
                ? totalScore.divide(BigDecimal.valueOf(subjectCount), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal totalPoints = calculatePoints(averagePercentage, examId);

        Result result = resultRepository.findByStudentAndExam(student, exam)
                .orElse(new Result());

        result.setStudent(student);
        result.setExam(exam);
        result.setTotalScore(totalScore);
        result.setAveragePercentage(averagePercentage);
        result.setTotalPoints(totalPoints);
        result.setDivision(determineDivision(averagePercentage, examId));
        result.setComputedAt(OffsetDateTime.now());

        result = resultRepository.save(result);
        return mapToDTO(result, new ResultDTO());
    }

    public List<ResultDTO> getResultsForExam(Integer examId) {
        List<Result> results = resultRepository.findByExamIdOrderByTotalScoreDesc(examId);
        return results.stream()
                .map(r -> mapToDTO(r, new ResultDTO()))
                .toList();
    }

    public ResultDTO getResult(Integer id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return mapToDTO(result, new ResultDTO());
    }

    private BigDecimal calculatePoints(BigDecimal averagePercentage, Integer examId) {
        GradeBoundary boundary = gradeBoundaryRepository.findByTypeAndPercentage("POINT", averagePercentage);
        return boundary != null ? boundary.getPointGrade() : BigDecimal.ZERO;
    }

    private String determineDivision(BigDecimal averagePercentage, Integer examId) {
        GradeBoundary boundary = gradeBoundaryRepository.findByTypeAndPercentage("LETTER", averagePercentage);
        return boundary != null ? boundary.getLetterGrade() : "";
    }

    private void assignRanks(List<Result> results) {
        List<Result> sorted = results.stream()
                .sorted(Comparator.comparing(Result::getTotalScore).reversed())
                .toList();

        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).setRankInClass(i + 1);
            resultRepository.save(sorted.get(i));
        }
    }

    public ResultDTO mapToDTO(final Result result, final ResultDTO resultDTO) {
        resultDTO.setId(result.getId());
        resultDTO.setTotalScore(result.getTotalScore());
        resultDTO.setAveragePercentage(result.getAveragePercentage());
        resultDTO.setTotalPoints(result.getTotalPoints());
        resultDTO.setDivision(result.getDivision());
        resultDTO.setRankInClass(result.getRankInClass());
        resultDTO.setRemark(result.getRemark());
        resultDTO.setComputedAt(result.getComputedAt());
        resultDTO.setStudent(result.getStudent() == null ? null : result.getStudent().getId());
        resultDTO.setExam(result.getExam() == null ? null : result.getExam().getId());
        return resultDTO;
    }
}
