package hr.bioinfo.swj.repository;

import hr.bioinfo.swj.model.JobDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobDetailRepositoryTest {

    @Autowired
    private JobDetailRepository jobDetailRepository;

    @Test
    void testFindById() {
        // Given
        Long jobId = 1L;
        JobDetail job = new JobDetail();
        job.setName("Test Job");
        jobDetailRepository.saveAndFlush(job);

        // When
        JobDetail jobDetail = jobDetailRepository.findById(jobId).orElse(null);

        // Then
        assertNotNull(jobDetail);
        assertEquals(jobId, jobDetail.getId());
    }


}