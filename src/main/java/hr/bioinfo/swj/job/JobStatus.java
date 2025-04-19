package hr.bioinfo.swj.job;

/**
 * Represents the current status of a job in the workflow.
 * This enum is used to track a job's lifecycle from creation to completion.
 */
public enum JobStatus {
    /**
     * Job has been created but execution has not yet begun.
     */
    NOT_STARTED,

    /**
     * Job execution has begun and is currently in progress.
     */
    STARTED,

    /**
     * Job has completed execution successfully.
     */
    FINISHED,

    /**
     * Job execution failed due to an exception or error.
     */
    FAILED
}
