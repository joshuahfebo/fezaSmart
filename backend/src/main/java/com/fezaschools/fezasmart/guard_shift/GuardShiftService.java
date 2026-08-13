package com.fezaschools.fezasmart.guard_shift;

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
public class GuardShiftService {

    private final GuardShiftRepository guardShiftRepository;
    private final StaffRepository staffRepository;

    public GuardShiftService(final GuardShiftRepository guardShiftRepository,
            final StaffRepository staffRepository) {
        this.guardShiftRepository = guardShiftRepository;
        this.staffRepository = staffRepository;
    }

    public List<GuardShiftDTO> findAll() {
        final List<GuardShift> guardShifts = guardShiftRepository.findAll(Sort.by("id"));
        return guardShifts.stream()
                .map(guardShift -> mapToDTO(guardShift, new GuardShiftDTO()))
                .toList();
    }

    public GuardShiftDTO get(final Integer id) {
        return guardShiftRepository.findById(id)
                .map(guardShift -> mapToDTO(guardShift, new GuardShiftDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final GuardShiftDTO guardShiftDTO) {
        final GuardShift guardShift = new GuardShift();
        mapToEntity(guardShiftDTO, guardShift);
        return guardShiftRepository.save(guardShift).getId();
    }

    public void update(final Integer id, final GuardShiftDTO guardShiftDTO) {
        final GuardShift guardShift = guardShiftRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(guardShiftDTO, guardShift);
        guardShiftRepository.save(guardShift);
    }

    public void delete(final Integer id) {
        final GuardShift guardShift = guardShiftRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        guardShiftRepository.delete(guardShift);
    }

    private GuardShiftDTO mapToDTO(final GuardShift guardShift, final GuardShiftDTO guardShiftDTO) {
        guardShiftDTO.setId(guardShift.getId());
        guardShiftDTO.setShiftDate(guardShift.getShiftDate());
        guardShiftDTO.setStartTime(guardShift.getStartTime());
        guardShiftDTO.setEndTime(guardShift.getEndTime());
        guardShiftDTO.setIsActive(guardShift.getIsActive());
        guardShiftDTO.setStaff(guardShift.getStaff() == null ? null : guardShift.getStaff().getId());
        return guardShiftDTO;
    }

    private GuardShift mapToEntity(final GuardShiftDTO guardShiftDTO, final GuardShift guardShift) {
        guardShift.setShiftDate(guardShiftDTO.getShiftDate());
        guardShift.setStartTime(guardShiftDTO.getStartTime());
        guardShift.setEndTime(guardShiftDTO.getEndTime());
        guardShift.setIsActive(guardShiftDTO.getIsActive());
        final Staff staff = guardShiftDTO.getStaff() == null ? null : staffRepository.findById(guardShiftDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("staff not found"));
        guardShift.setStaff(staff);
        return guardShift;
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final GuardShift staffGuardShift = guardShiftRepository.findFirstByStaffId(event.getId());
        if (staffGuardShift != null) {
            referencedException.setKey("staff.guardShift.staff.referenced");
            referencedException.addParam(staffGuardShift.getId());
            throw referencedException;
        }
    }

}
