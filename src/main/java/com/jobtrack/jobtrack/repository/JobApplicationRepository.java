package com.jobtrack.jobtrack.repository;

import com.jobtrack.jobtrack.model.ApplicationStatus;
import com.jobtrack.jobtrack.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobApplicationRepository
                extends JpaRepository<JobApplication, Long> {

        Page<JobApplication> findAllByOwner_Email(
                        String email,
                        Pageable pageable);

        Page<JobApplication> findAllByOwner_EmailAndStatus(
                        String email,
                        ApplicationStatus status,
                        Pageable pageable);

        Optional<JobApplication> findByIdAndOwner_Email(
                        Long id,
                        String email);

        @Query("""
                        select application
                        from JobApplication application
                        where application.owner.email = :email
                          and (
                                lower(application.company) like lower(concat('%', :search, '%'))
                                or lower(application.position) like lower(concat('%', :search, '%'))
                          )
                        """)
        Page<JobApplication> searchByOwnerEmail(
                        @Param("email") String email,
                        @Param("search") String search,
                        Pageable pageable);

        @Query("""
                        select application
                        from JobApplication application
                        where application.owner.email = :email
                          and application.status = :status
                          and (
                                lower(application.company) like lower(concat('%', :search, '%'))
                                or lower(application.position) like lower(concat('%', :search, '%'))
                          )
                        """)
        Page<JobApplication> searchByOwnerEmailAndStatus(
                        @Param("email") String email,
                        @Param("status") ApplicationStatus status,
                        @Param("search") String search,
                        Pageable pageable);
}