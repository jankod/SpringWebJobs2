package hr.bioinfo.swj.rest;

import hr.bioinfo.swj.job.Job;
import hr.bioinfo.swj.model.JobDetail;
import hr.bioinfo.swj.repository.JobDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobRestController {

    private final JobDetailRepository jobDetailRepository;

    /**
     * Get all jobs
     */
    @GetMapping
    public List<JobDetail> getAllJobs() {
        return jobDetailRepository.findAll();
    }

    /**
     * Create a new job
     */
    @PostMapping
    public ResponseEntity<JobDetail> createJob(@RequestBody Map<String, Object> jobData) {
        try {
            // Extract job data
            String name = (String) jobData.get("name");
            String description = (String) jobData.get("description");
            String type = (String) jobData.get("type");

            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Job name is required");
            }

            // Check if job with the same name already exists
            if (jobDetailRepository.findByName(name) != null) {
                throw new IllegalArgumentException("Job with name '" + name + "' already exists");
            }

            // Create and save the job
            JobDetail jobDetail = new JobDetail();
            jobDetail.setName(name);
            jobDetail.setDescription(description);

            // TODO: Store the job implementation (Groovy code or Java class)
            // This would typically involve storing the Groovy code in a file or database,
            // or storing the Java class name for later instantiation

            JobDetail savedJob = jobDetailRepository.save(jobDetail);
            log.info("Created new job: {}", savedJob.getName());

            return ResponseEntity.ok(savedJob);
        } catch (IllegalArgumentException e) {
            log.error("Error creating job: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error creating job", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    /**
     * Get a list of Java classes that implement the Job interface
     */
    @GetMapping("/java-classes")
    public List<String> getJobClasses() {
        // This is a placeholder implementation
        // In a real application, you would scan the classpath for classes that implement the Job interface
        return List.of(
            "hr.bioinfo.swj.job.impl.DataProcessingJob",
            "hr.bioinfo.swj.job.impl.ReportGenerationJob",
            "hr.bioinfo.swj.job.impl.DataImportJob"
        );
    }

    /**
     * Handle validation errors
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleException(ResponseStatusException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", ex.getStatusCode().value());
        response.put("message", ex.getReason());

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
}
