package hr.bioinfo.swj.job;

import lombok.Data;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Data
public class JobExecution<R> implements Callable<R> {
    private JobWorkflowContext jobWorkflowContext;
    private Job<R> job;
    private JobResult<R> result;
    private Exception exception;
    private Future<R> future;
    private JobStatus jobStatus = JobStatus.NOT_STARTED;

    long timeStart;
    long timeEnd;


    public JobExecution(JobWorkflowContext context, Job<R> job) {
        Objects.requireNonNull(job);
        Objects.requireNonNull(context);
        this.jobWorkflowContext = context;
        this.job = job;
    }

    @Override
    public R call() {
        JobResult<R> jobResult = new JobResult<>();
        try {
            jobStatus = JobStatus.STARTED;
            timeStart = System.currentTimeMillis();
            R jobOutput = getJob().start(jobWorkflowContext);
            jobStatus = JobStatus.FINISHED;
            jobResult.setStartTime(timeStart);
            jobResult.setResult(jobOutput);
            return jobOutput;
        } catch (Exception e) {
            timeEnd = System.currentTimeMillis();
            jobStatus = JobStatus.FAILED;
            exception = e;
            this.result = jobResult;
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void setFuture(Future<?> future) {
        this.future = (Future<R>) future;
    }
}