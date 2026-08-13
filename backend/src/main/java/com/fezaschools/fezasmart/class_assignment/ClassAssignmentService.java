package com.fezaschools.fezasmart.class_assignment;

import com.fezaschools.fezasmart.classs.Classs;
import com.fezaschools.fezasmart.classs.ClasssRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteClasss;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ClassAssignmentService {

    private final ClassAssignmentRepository classAssignmentRepository;
    private final ClasssRepository classsRepository;
    private final StaffRepository staffRepository;

    public ClassAssignmentService(final ClassAssignmentRepository classAssignmentRepository,
            final ClasssRepository classsRepository, final StaffRepository staffRepository) {
        this.classAssignmentRepository = classAssignmentRepository;
        this.classsRepository = classsRepository;
        this.staffRepository = staffRepository;
    }

    public List<ClassAssignmentDTO> findAll() {
        final List<ClassAssignment> classAssignments = classAssignmentRepository.findAll(Sort.by("id"));
        return classAssignments.stream()
                .map(classAssignment -> mapToDTO(classAssignment, new ClassAssignmentDTO()))
                .toList();
    }

    public ClassAssignmentDTO get(final Integer id) {
        return classAssignmentRepository.findById(id)
                .map(classAssignment -> mapToDTO(classAssignment, new ClassAssignmentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ClassAssignmentDTO classAssignmentDTO) {
        final ClassAssignment classAssignment = new ClassAssignment();
        mapToEntity(classAssignmentDTO, classAssignment);
        return classAssignmentRepository.save(classAssignment).getId();
    }

    public void update(final Integer id, final ClassAssignmentDTO classAssignmentDTO) {
        final ClassAssignment classAssignment = classAssignmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(classAssignmentDTO, classAssignment);
        classAssignmentRepository.save(classAssignment);
    }

    public void delete(final Integer id) {
        final ClassAssignment classAssignment = classAssignmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        classAssignmentRepository.delete(classAssignment);
    }

    private ClassAssignmentDTO mapToDTO(final ClassAssignment classAssignment,
            final ClassAssignmentDTO classAssignmentDTO) {
        classAssignmentDTO.setId(classAssignment.getId());
        classAssignmentDTO.setRoleInClass(classAssignment.getRoleInClass());
        classAssignmentDTO.setAssignedDate(classAssignment.getAssignedDate());
        classAssignmentDTO.setClasss(classAssignment.getClasss() == null ? null : classAssignment.getClasss().getId());
        classAssignmentDTO.setStaff(classAssignment.getStaff() == null ? null : classAssignment.getStaff().getId());
        return classAssignmentDTO;
    }

    private ClassAssignment mapToEntity(final ClassAssignmentDTO classAssignmentDTO,
            final ClassAssignment classAssignment) {
        classAssignment.setRoleInClass(classAssignmentDTO.getRoleInClass());
        classAssignment.setAssignedDate(classAssignmentDTO.getAssignedDate());
        final Classs classs = classAssignmentDTO.getClasss() == null ? null : classsRepository.findById(classAssignmentDTO.getClasss())
                .orElseThrow(() -> new NotFoundException("classs not found"));
        classAssignment.setClasss(classs);
        final Staff staff = classAssignmentDTO.getStaff() == null ? null : staffRepository.findById(classAssignmentDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("staff not found"));
        classAssignment.setStaff(staff);
        return classAssignment;
    }

    @EventListener(BeforeDeleteClasss.class)
    public void on(final BeforeDeleteClasss event) {
        final ReferencedException referencedException = new ReferencedException();
        final ClassAssignment classsClassAssignment = classAssignmentRepository.findFirstByClasssId(event.getId());
        if (classsClassAssignment != null) {
            referencedException.setKey("classs.classAssignment.classs.referenced");
            referencedException.addParam(classsClassAssignment.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final ClassAssignment staffClassAssignment = classAssignmentRepository.findFirstByStaffId(event.getId());
        if (staffClassAssignment != null) {
            referencedException.setKey("staff.classAssignment.staff.referenced");
            referencedException.addParam(staffClassAssignment.getId());
            throw referencedException;
        }
    }

}
