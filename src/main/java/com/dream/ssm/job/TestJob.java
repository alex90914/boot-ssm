package com.dream.ssm.job;

import com.dream.ssm.constant.JobConstant;
import com.dream.ssm.dto.JobDTO;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * quartz任务调度测试任务
 */
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        JobDTO jobDTO = (JobDTO) jobDataMap.get(JobConstant.JOB_PARAMS_KEY);
        System.out.println("任务执行 : " + jobDTO);
    }
}
