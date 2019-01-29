package com.dream.ssm.service.impl;

import com.dream.ssm.dto.JobDTO;
import com.dream.ssm.job.TestJob;
import com.dream.ssm.service.JobService;
import com.dream.ssm.utils.QuartzUtils;
import com.dream.ssm.utils.Result;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    public Result<Boolean> addJob() {
        Scheduler scheduler = schedulerFactory.getScheduler();
        String job_name = "JOB_NAME";
        String cron = "0/2 * * * * ? ";
        JobDTO jobDTO = new JobDTO(1, job_name, cron, new Date());
        QuartzUtils.addJob(scheduler, job_name, TestJob.class, jobDTO, cron);
        return Result.success(Boolean.TRUE);
    }

    @Override
    public Result<Boolean> delJob() {
        String job_name = "JOB_NAME";
        Scheduler scheduler = schedulerFactory.getScheduler();
        QuartzUtils.removeJob(scheduler, job_name);
        return Result.success(Boolean.TRUE);
    }

}
