package hr.bioinfo.swj.repository;

import hr.bioinfo.swj.job.JobExecution;
import hr.bioinfo.swj.job.JobStatus;
import hr.bioinfo.swj.model.JobDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobDetailRepository extends JpaRepository<JobDetail, Long> {

    JobDetail findByName(String name);

    List<JobDetail> findByStatus(JobStatus status);

    void deleteByName(String name);

    void deleteByDescription(String description);

    void deleteByStatus(JobStatus status);
}