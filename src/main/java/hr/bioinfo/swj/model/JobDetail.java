package hr.bioinfo.swj.model;

import hr.bioinfo.swj.job.JobExecution;
import hr.bioinfo.swj.job.JobStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Setter
@Getter
@Entity
@Table (name = "job_detail", indexes = {
        @Index (name = "idx_jobdetail_name", columnList = "name", unique = true),
})
@FieldDefaults (level = AccessLevel.PUBLIC)
public class JobDetail extends AbstractPersistable<Long> {

    @Column (nullable = false, unique = true, length = 200)
    private String name;

    @Column (length = 2000)
    private String description;

    @Enumerated (EnumType.ORDINAL)
    private JobStatus status = JobStatus.NOT_STARTED;

}
