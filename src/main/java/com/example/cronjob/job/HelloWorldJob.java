package com.example.cronjob.job;

import com.example.cronjob.info.TimerInfo;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldJob.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    org.springframework.batch.core.Job job;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap=context.getJobDetail().getJobDataMap();
        // HelloWorldJob.class.getSimpleName(), is the job key / job id
        TimerInfo info=(TimerInfo) jobDataMap.get(HelloWorldJob.class.getSimpleName());

        try {
            load();
        } catch (JobParametersInvalidException | JobRestartException | JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        }

        log.info("Remaining fire count is '{}'", info.getRemainingFireCount());
    }

    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters);

        System.out.println("JobExecution: " + jobExecution.getStatus());

        System.out.println("Batch is Running...");
        while (jobExecution.isRunning()) {
            System.out.println("...");
        }

        return jobExecution.getStatus();
    }
}
