package com.example.cronjob.job;

import com.example.cronjob.info.TimerInfo;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap=context.getJobDetail().getJobDataMap();
        // HelloWorldJob.class.getSimpleName(), is the job key / job id
        TimerInfo info=(TimerInfo) jobDataMap.get(HelloWorldJob.class.getSimpleName());
        log.info("Remaining fire count is '{}'", info.getRemainingFireCount());
    }
}
