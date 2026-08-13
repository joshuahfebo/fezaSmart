package com.fezaschools.fezasmart.club_member;

import com.fezaschools.fezasmart.club.Club;
import com.fezaschools.fezasmart.club.ClubRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteClub;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;
    private final ClubRepository clubRepository;
    private final StudentRepository studentRepository;

    public ClubMemberService(final ClubMemberRepository clubMemberRepository,
            final ClubRepository clubRepository, final StudentRepository studentRepository) {
        this.clubMemberRepository = clubMemberRepository;
        this.clubRepository = clubRepository;
        this.studentRepository = studentRepository;
    }

    public List<ClubMemberDTO> findAll() {
        final List<ClubMember> clubMembers = clubMemberRepository.findAll(Sort.by("id"));
        return clubMembers.stream()
                .map(clubMember -> mapToDTO(clubMember, new ClubMemberDTO()))
                .toList();
    }

    public ClubMemberDTO get(final Integer id) {
        return clubMemberRepository.findById(id)
                .map(clubMember -> mapToDTO(clubMember, new ClubMemberDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ClubMemberDTO clubMemberDTO) {
        final ClubMember clubMember = new ClubMember();
        mapToEntity(clubMemberDTO, clubMember);
        return clubMemberRepository.save(clubMember).getId();
    }

    public void update(final Integer id, final ClubMemberDTO clubMemberDTO) {
        final ClubMember clubMember = clubMemberRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(clubMemberDTO, clubMember);
        clubMemberRepository.save(clubMember);
    }

    public void delete(final Integer id) {
        final ClubMember clubMember = clubMemberRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        clubMemberRepository.delete(clubMember);
    }

    private ClubMemberDTO mapToDTO(final ClubMember clubMember, final ClubMemberDTO clubMemberDTO) {
        clubMemberDTO.setId(clubMember.getId());
        clubMemberDTO.setJoinedDate(clubMember.getJoinedDate());
        clubMemberDTO.setRoleInClub(clubMember.getRoleInClub());
        clubMemberDTO.setIsActive(clubMember.getIsActive());
        clubMemberDTO.setClub(clubMember.getClub() == null ? null : clubMember.getClub().getId());
        clubMemberDTO.setStudent(clubMember.getStudent() == null ? null : clubMember.getStudent().getId());
        return clubMemberDTO;
    }

    private ClubMember mapToEntity(final ClubMemberDTO clubMemberDTO, final ClubMember clubMember) {
        clubMember.setJoinedDate(clubMemberDTO.getJoinedDate());
        clubMember.setRoleInClub(clubMemberDTO.getRoleInClub());
        clubMember.setIsActive(clubMemberDTO.getIsActive());
        final Club club = clubMemberDTO.getClub() == null ? null : clubRepository.findById(clubMemberDTO.getClub())
                .orElseThrow(() -> new NotFoundException("club not found"));
        clubMember.setClub(club);
        final Student student = clubMemberDTO.getStudent() == null ? null : studentRepository.findById(clubMemberDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        clubMember.setStudent(student);
        return clubMember;
    }

    @EventListener(BeforeDeleteClub.class)
    public void on(final BeforeDeleteClub event) {
        final ReferencedException referencedException = new ReferencedException();
        final ClubMember clubClubMember = clubMemberRepository.findFirstByClubId(event.getId());
        if (clubClubMember != null) {
            referencedException.setKey("club.clubMember.club.referenced");
            referencedException.addParam(clubClubMember.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final ClubMember studentClubMember = clubMemberRepository.findFirstByStudentId(event.getId());
        if (studentClubMember != null) {
            referencedException.setKey("student.clubMember.student.referenced");
            referencedException.addParam(studentClubMember.getId());
            throw referencedException;
        }
    }

}
