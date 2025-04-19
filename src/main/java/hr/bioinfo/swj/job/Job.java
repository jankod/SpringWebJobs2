package hr.bioinfo.swj.job;

/**
 * Job interface.
 *
 * @param <R> result type
 */
public interface Job<R> {

    R start(JobWorkflowContext context) throws Exception;

}
