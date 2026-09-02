package com.jobtrack.jobtrack.repository;

import com.jobtrack.jobtrack.model.ApplicationStatus;
import com.jobtrack.jobtrack.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository
                extends JpaRepository<JobApplication, Long> {

        List<JobApplication> findAllByOwner_EmailOrderByApplicationDateDesc(String email);

        List<JobApplication> findAllByOwner_EmailAndStatusOrderByApplicationDateDesc(
                        String email,
                        ApplicationStatus status);

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
                        order by application.applicationDate desc
                        """)
        List<JobApplication> searchByOwnerEmailOrderByApplicationDateDesc(
                        @Param("email") String email,
                        @Param("search") String search);

        @Query("""
                        select application
                        from JobApplication application
                        where application.owner.email = :email
                          and application.status = :status
                          and (
                                lower(application.company) like lower(concat('%', :search, '%'))
                                or lower(application.position) like lower(concat('%', :search, '%'))
                          )
                        order by application.applicationDate desc
                        """)
        List<JobApplication> searchByOwnerEmailAndStatusOrderByApplicationDateDesc(
                        @Param("email") String email,
                        @Param("status") ApplicationStatus status,
                        @Param("search") String search);
}
