package com.example.cronjob.timerService;

import com.example.cronjob.info.TimerInfo;
import com.example.cronjob.util.TimerUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SchedulerService {
    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final Scheduler scheduler; // injecting a scheduler as a class

    @Autowired
    public SchedulerService(Scheduler scheduler){
        this.scheduler=scheduler;
    }

    public void scheduled(final Class jobClass, final TimerInfo info){
        final JobDetail jobDetail= TimerUtils.buildJobDetail(jobClass,info);
        final Trigger trigger=TimerUtils.buildTrigger(jobClass, info);

        try {
            scheduler.scheduleJob(jobDetail,trigger);
        }catch (SchedulerException e){
            log.error(e.getMessage(),e);
        }
    }

    public List<TimerInfo> getAllRunningTimers(){
        try{
            return scheduler.getJobKeys(GroupMatcher.anyGroup())
                    .stream()
                    .map(jobKey -> {
                        try{
                            final JobDetail jobDetail=scheduler.getJobDetail(jobKey);
                            return (TimerInfo) jobDetail.getJobDataMap().get(jobKey.getName());
                        }catch (SchedulerException e){
                            log.error(e.getMessage(),e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }catch (SchedulerException e){
            log.error(e.getMessage(),e);
            return Collections.emptyList();
        }
    }

    public TimerInfo getRunningTimer(String timerId) {
        try{
            final JobDetail jobDetail=scheduler.getJobDetail(new JobKey(timerId));
            if(jobDetail==null) {
                log.error("Faild to find timer with ID {}",timerId);
                return null;
            }
            return (TimerInfo) jobDetail.getJobDataMap().get(timerId);
        }catch (SchedulerException e){
            log.error(e.getMessage(),e);
            return null;
        }
    }

    public void updateTimer(final String timerId, final TimerInfo info) {
        try {
            final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
            if (jobDetail == null) {
                log.error("Failed to find timer with ID '{}'", timerId);
                return;
            }

            jobDetail.getJobDataMap().put(timerId, info);

            scheduler.addJob(jobDetail, true, true);
        } catch (final SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Boolean deleteTimer(final String timerId){
        try {
            return scheduler.deleteJob(new JobKey(timerId));
        }catch (SchedulerException e){
            log.error(e.getMessage(),e);
            return false;
        }
    }

    @PostConstruct
    public void init(){
        try{
            scheduler.start();
            scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener(this));
        }catch (SchedulerException e) {
            log.error(e.getMessage(),e);
        }
    }

    @PreDestroy
    public void preDestroy(){
        try{
            scheduler.shutdown();
        }catch (SchedulerException e) {
            log.error(e.getMessage(),e);
        }
    }
}
