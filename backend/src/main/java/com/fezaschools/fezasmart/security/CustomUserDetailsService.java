package com.fezaschools.fezasmart.security;

import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;

    public CustomUserDetailsService(UserRepository userRepository,
            StaffRepository staffRepository,
            StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        UserPrincipal principal = UserPrincipal.from(user);

        // Resolve school id from staff or student relationship
        Optional<Staff> staff = staffRepository.findOptionalByUserId(user.getId());
        if (staff.isPresent()) {
            principal = new UserPrincipal(principal.getId(), principal.getUsername(),
                    principal.getPassword(), principal.isEnabled(),
                    staff.get().getSchool().getId(), principal.getAuthorities());
        } else {
            Optional<Student> student = studentRepository.findOptionalByUserId(user.getId());
            if (student.isPresent()) {
                principal = new UserPrincipal(principal.getId(), principal.getUsername(),
                        principal.getPassword(), principal.isEnabled(),
                        student.get().getSchool().getId(), principal.getAuthorities());
            }
        }
        return principal;
    }
}