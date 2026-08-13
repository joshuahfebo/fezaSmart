package com.fezaschools.fezasmart.club;

import com.fezaschools.fezasmart.events.BeforeDeleteClub;
import com.fezaschools.fezasmart.events.BeforeDeleteSchool;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.school.SchoolRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ClubService {

    private final ClubRepository clubRepository;
    private final SchoolRepository schoolRepository;
    private final StaffRepository staffRepository;
    private final ApplicationEventPublisher publisher;

    public ClubService(final ClubRepository clubRepository, final SchoolRepository schoolRepository,
            final StaffRepository staffRepository, final ApplicationEventPublisher publisher) {
        this.clubRepository = clubRepository;
        this.schoolRepository = schoolRepository;
        this.staffRepository = staffRepository;
        this.publisher = publisher;
    }

    public List<ClubDTO> findAll() {
        final List<Club> clubs = clubRepository.findAll(Sort.by("id"));
        return clubs.stream()
                .map(club -> mapToDTO(club, new ClubDTO()))
                .toList();
    }

    public ClubDTO get(final Integer id) {
        return clubRepository.findById(id)
                .map(club -> mapToDTO(club, new ClubDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ClubDTO clubDTO) {
        final Club club = new Club();
        mapToEntity(clubDTO, club);
        return clubRepository.save(club).getId();
    }

    public void update(final Integer id, final ClubDTO clubDTO) {
        final Club club = clubRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(clubDTO, club);
        clubRepository.save(club);
    }

    public void delete(final Integer id) {
        final Club club = clubRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteClub(id));
        clubRepository.delete(club);
    }

    private ClubDTO mapToDTO(final Club club, final ClubDTO clubDTO) {
        clubDTO.setId(club.getId());
        clubDTO.setName(club.getName());
        clubDTO.setDescription(club.getDescription());
        clubDTO.setCreatedAt(club.getCreatedAt());
        clubDTO.setIsActive(club.getIsActive());
        clubDTO.setSchool(club.getSchool() == null ? null : club.getSchool().getId());
        clubDTO.setPatronStaff(club.getPatronStaff() == null ? null : club.getPatronStaff().getId());
        return clubDTO;
    }

    private Club mapToEntity(final ClubDTO clubDTO, final Club club) {
        club.setName(clubDTO.getName());
        club.setDescription(clubDTO.getDescription());
        club.setCreatedAt(clubDTO.getCreatedAt());
        club.setIsActive(clubDTO.getIsActive());
        final School school = clubDTO.getSchool() == null ? null : schoolRepository.findById(clubDTO.getSchool())
                .orElseThrow(() -> new NotFoundException("school not found"));
        club.setSchool(school);
        final Staff patronStaff = clubDTO.getPatronStaff() == null ? null : staffRepository.findById(clubDTO.getPatronStaff())
                .orElseThrow(() -> new NotFoundException("patronStaff not found"));
        club.setPatronStaff(patronStaff);
        return club;
    }

    @EventListener(BeforeDeleteSchool.class)
    public void on(final BeforeDeleteSchool event) {
        final ReferencedException referencedException = new ReferencedException();
        final Club schoolClub = clubRepository.findFirstBySchoolId(event.getId());
        if (schoolClub != null) {
            referencedException.setKey("school.club.school.referenced");
            referencedException.addParam(schoolClub.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final Club patronStaffClub = clubRepository.findFirstByPatronStaffId(event.getId());
        if (patronStaffClub != null) {
            referencedException.setKey("staff.club.patronStaff.referenced");
            referencedException.addParam(patronStaffClub.getId());
            throw referencedException;
        }
    }

}
