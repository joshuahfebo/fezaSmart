package com.fezaschools.fezasmart.leave_request;

import org.springframework.data.jpa.repository.JpaRepository;


public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    LeaveRequest findFirstByStudentId(Integer id);

    LeaveRequest findFirstByRequesterUserId(Integer id);

    LeaveRequest findFirstByProcessedByStaffId(Integer id);

    LeaveRequest findFirstByPermissionId(Integer id);

}
