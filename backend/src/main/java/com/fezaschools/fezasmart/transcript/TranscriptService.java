package com.fezaschools.fezasmart.transcript;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.academic_year.AcademicYearRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteAcademicYear;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.result.Result;
import com.fezaschools.fezasmart.result.ResultRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;


@Service
public class TranscriptService {

    private final TranscriptRepository transcriptRepository;
    private final StudentRepository studentRepository;
    private final AcademicYearRepository academicYearRepository;
    private final StaffRepository staffRepository;
    private final ResultRepository resultRepository;
    private final ApplicationEventPublisher publisher;

    public TranscriptService(final TranscriptRepository transcriptRepository,
            final StudentRepository studentRepository,
            final AcademicYearRepository academicYearRepository,
            final StaffRepository staffRepository,
            final ResultRepository resultRepository,
            final ApplicationEventPublisher publisher) {
        this.transcriptRepository = transcriptRepository;
        this.studentRepository = studentRepository;
        this.academicYearRepository = academicYearRepository;
        this.staffRepository = staffRepository;
        this.resultRepository = resultRepository;
        this.publisher = publisher;
    }

    public List<TranscriptDTO> findAll() {
        final List<Transcript> transcripts = transcriptRepository.findAll(Sort.by("id"));
        return transcripts.stream()
                .map(transcript -> mapToDTO(transcript, new TranscriptDTO()))
                .toList();
    }

    public TranscriptDTO get(final Integer id) {
        return transcriptRepository.findById(id)
                .map(transcript -> mapToDTO(transcript, new TranscriptDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TranscriptDTO transcriptDTO) {
        final Transcript transcript = new Transcript();
        mapToEntity(transcriptDTO, transcript);
        return transcriptRepository.save(transcript).getId();
    }

    public void update(final Integer id, final TranscriptDTO transcriptDTO) {
        final Transcript transcript = transcriptRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(transcriptDTO, transcript);
        transcriptRepository.save(transcript);
    }

    public void delete(final Integer id) {
        final Transcript transcript = transcriptRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        transcriptRepository.delete(transcript);
    }

    @Transactional
    public Integer generateTranscript(Integer studentId, Integer academicYearId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        AcademicYear academicYear = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new NotFoundException("Academic year not found"));

        Transcript existing = transcriptRepository.findByStudentAndAcademicYear(student, academicYear)
                .orElse(null);

        List<Result> results = resultRepository.findByStudentId(studentId);

        String transcriptData = buildTranscriptData(student, results, academicYear);

        Transcript transcript = existing != null ? existing : new Transcript();
        transcript.setStudent(student);
        transcript.setAcademicYear(academicYear);
        transcript.setTranscriptData(transcriptData);
        transcript.setCreatedAt(OffsetDateTime.now());

        Transcript saved = transcriptRepository.save(transcript);
        return saved.getId();
    }

    private String buildTranscriptData(Student student, List<Result> results, AcademicYear academicYear) {
        StringBuilder sb = new StringBuilder();
        sb.append("TRANSCRIPT\n");
        sb.append("Student: ").append(student.getFirstName()).append(" ").append(student.getLastName()).append("\n");
        sb.append("Control Number: ").append(student.getControlNumber()).append("\n");
        sb.append("Academic Year: ").append(academicYear.getName()).append("\n");
        sb.append("\nResults:\n");

        for (Result result : results) {
            sb.append("Exam: ").append(result.getExam().getName()).append("\n");
            sb.append("  Total Score: ").append(result.getTotalScore()).append("\n");
            sb.append("  Average: ").append(result.getAveragePercentage()).append("%\n");
            sb.append("  Division: ").append(result.getDivision()).append("\n");
            sb.append("  Rank: ").append(result.getRankInClass()).append("\n\n");
        }

        return sb.toString();
    }

    private void mapToEntity(final TranscriptDTO transcriptDTO, final Transcript transcript) {
        transcript.setTranscriptData(transcriptDTO.getTranscriptData());
        transcript.setCreatedAt(transcriptDTO.getCreatedAt());
        final Student student = transcriptDTO.getStudent() == null ? null : studentRepository.findById(transcriptDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        transcript.setStudent(student);
        final AcademicYear academicYear = transcriptDTO.getAcademicYear() == null ? null : academicYearRepository.findById(transcriptDTO.getAcademicYear())
                .orElseThrow(() -> new NotFoundException("academicYear not found"));
        transcript.setAcademicYear(academicYear);
        final Staff generatedBy = transcriptDTO.getGeneratedBy() == null ? null : staffRepository.findById(transcriptDTO.getGeneratedBy())
                .orElseThrow(() -> new NotFoundException("generatedBy not found"));
        transcript.setGeneratedBy(generatedBy);
    }

    private TranscriptDTO mapToDTO(final Transcript transcript, final TranscriptDTO transcriptDTO) {
        transcriptDTO.setId(transcript.getId());
        transcriptDTO.setTranscriptData(transcript.getTranscriptData());
        transcriptDTO.setCreatedAt(transcript.getCreatedAt());
        transcriptDTO.setStudent(transcript.getStudent() == null ? null : transcript.getStudent().getId());
        transcriptDTO.setAcademicYear(transcript.getAcademicYear() == null ? null : transcript.getAcademicYear().getId());
        transcriptDTO.setGeneratedBy(transcript.getGeneratedBy() == null ? null : transcript.getGeneratedBy().getId());
        return transcriptDTO;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final Transcript studentTranscript = transcriptRepository.findFirstByStudentId(event.getId());
        if (studentTranscript != null) {
            throw new com.fezaschools.fezasmart.util.ReferencedException();
        }
    }

    @EventListener(BeforeDeleteAcademicYear.class)
    public void on(final BeforeDeleteAcademicYear event) {
        final Transcript academicYearTranscript = transcriptRepository.findFirstByAcademicYearId(event.getId());
        if (academicYearTranscript != null) {
            throw new com.fezaschools.fezasmart.util.ReferencedException();
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final Transcript generatedByTranscript = transcriptRepository.findFirstByGeneratedById(event.getId());
        if (generatedByTranscript != null) {
            throw new com.fezaschools.fezasmart.util.ReferencedException();
        }
    }

}
