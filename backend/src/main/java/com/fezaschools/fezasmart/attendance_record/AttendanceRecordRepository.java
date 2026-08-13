package com.fezaschools.fezasmart.attendance_record;

import com.fezaschools.fezasmart.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {

    AttendanceRecord findFirstByStudentId(Integer id);

    AttendanceRecord findFirstByClasssId(Integer id);

    AttendanceRecord findFirstByMarkedByStaffId(Integer id);

    List<AttendanceRecord> findByStudent(Student student);

    List<AttendanceRecord> findByStudentIdAndDateBetween(Integer studentId, LocalDate startDate, LocalDate endDate);

    List<AttendanceRecord> findByClasssIdAndDate(Integer classsId, LocalDate date);

    @Query("SELECT COUNT(ar) FROM AttendanceRecord ar WHERE ar.student.id = :studentId AND ar.status = :status")
    long countByStudentIdAndStatus(@Param("studentId") Integer studentId, @Param("status") String status);

    @Query("SELECT COUNT(ar) FROM AttendanceRecord ar WHERE ar.student.id = :studentId AND ar.date BETWEEN :startDate AND :endDate")
    long countByStudentIdAndDateBetween(@Param("studentId") Integer studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT ar.status, COUNT(ar) FROM AttendanceRecord ar WHERE ar.student.id = :studentId AND ar.date BETWEEN :startDate AND :endDate GROUP BY ar.status")
    List<Object[]> countByStatusGroupedByStudentIdAndDateRange(@Param("studentId") Integer studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
