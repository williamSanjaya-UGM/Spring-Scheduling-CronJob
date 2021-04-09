package com.example.cronjob.controller;

import com.example.cronjob.job.HelloWorldJob;
import com.example.cronjob.info.TimerInfo;
import com.example.cronjob.timerService.SchedulerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaygroundService {
    private final SchedulerService scheduler;

    public PlaygroundService(final SchedulerService scheduler){
        this.scheduler=scheduler;
    }

    public void runHelloWorldJob(){
        final TimerInfo info=new TimerInfo();
        info.setTotalFireCount(5);
        info.setRemainingFireCount(info.getTotalFireCount());
        info.setRepeatIntervalMs(5000);
        info.setInitialOffsetMs(1000);
        info.setCallbackData("My callback data");

        scheduler.scheduled(HelloWorldJob.class,info);
    }

    public Boolean deleteTimer(final String timerId){
        return scheduler.deleteTimer(timerId);
    }

    // Fetch the list of running Timers
    public List<TimerInfo> getAllRunningTimers(){
        return scheduler.getAllRunningTimers();
    }

    public TimerInfo getRunningTimer(String timerId) {
        return scheduler.getRunningTimer(timerId);
    }
}
