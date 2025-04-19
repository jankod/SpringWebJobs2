package hr.bioinfo.swj

import hr.bioinfo.swj.job.Job
import hr.bioinfo.swj.job.JobWorkflowContext

class Myjob1 implements Job<String> {

    @Override
    String start(JobWorkflowContext context) throws Exception {
        println "Myjob1 started"

        context.setParam("data", "Hello from Myjob1")

        // Simulate some processing
        Thread.sleep(1000)
        println "Myjob1 finished"

        return "Hello from Myjob1"
    }
}
