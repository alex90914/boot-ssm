package com.dream.ssm.utils;

import com.dream.ssm.constant.JobConstant;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz调度管理器
 */
public class QuartzUtils {
    private static String JOB_GROUP_NAME = "JOB_GROUP_SYSTEM";
    private static String TRIGGER_GROUP_NAME = "TRIGGER_GROUP_SYSTEM";

    /**
     * @param scheduler 调度器
     * @param jobName   任务名
     * @param jobClazz  任务
     * @param params    任务参数
     * @param time      时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     * @Title: QuartzManager.java
     */
    public static void addJob(Scheduler scheduler, String jobName, Class<? extends Job> jobClazz, Object params, String time) {
        try {
            JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);// 任务名，任务组，任务执行类
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(JobConstant.JOB_PARAMS_KEY, params);
            JobDetail jobDetail = newJob(jobClazz).withIdentity(jobKey).setJobData(jobDataMap).build();
            TriggerKey triggerKey = new TriggerKey(jobName, TRIGGER_GROUP_NAME);// 触发器
            // CronTrigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(cronSchedule(time)).build();// 触发器时间设定
            CronTrigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(cronSchedule(time).withMisfireHandlingInstructionDoNothing()).build();// 触发器时间设定
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();// 启动
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param scheduler        调度器
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClazz         任务
     * @param params           任务参数
     * @param time             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     * @Title: QuartzManager.java
     */
    public static void addJob(Scheduler scheduler, String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class<? extends Job> jobClazz, Object params, String time) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroupName);
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(JobConstant.JOB_PARAMS_KEY, params);
            @SuppressWarnings("unchecked")
            JobDetail jobDetail = newJob(jobClazz).withIdentity(jobKey).setJobData(jobDataMap).build();
            // 触发器
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
            Trigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(cronSchedule(time)).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param scheduler 调度器
     * @param jobName
     * @param time
     * @Description: 修改一个任务的触发时间(使用默认的任务组名 ， 触发器名 ， 触发器组名)
     * @Title: QuartzManager.java
     */
    public static void modifyJobTime(Scheduler scheduler, String jobName, String time) {
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                Class objJobClass = jobDetail.getJobClass();
                Object params = jobDetail.getJobDataMap().get("params");
                removeJob(scheduler, jobName);
                System.out.println("修改任务：" + jobName);
                addJob(scheduler, jobName, objJobClass, params, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param scheduler        调度器 *
     * @param scheduler        调度器
     * @param triggerName
     * @param triggerGroupName
     * @param time
     * @Description: 修改一个任务的触发时间
     * @Title: QuartzManager.java
     */
    public static void modifyJobTime(Scheduler scheduler, String triggerName, String triggerGroupName, String time) {
        try {
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                // 修改时间
                trigger.getTriggerBuilder().withSchedule(cronSchedule(time));
                // 重启触发器
                scheduler.resumeTrigger(triggerKey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param scheduler 调度器
     * @param jobName
     * @Description: 移除一个任务(使用默认的任务组名 ， 触发器名 ， 触发器组名)
     * @Title: QuartzManager.java
     */
    public static void removeJob(Scheduler scheduler, String jobName) {
        removeJob(scheduler, jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME);
    }

    /**
     * @param scheduler        调度器
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     * @Title: QuartzManager.java
     */
    public static void removeJob(Scheduler scheduler, String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            JobKey jobKey = new JobKey(jobName, jobGroupName);
            scheduler.deleteJob(jobKey);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param scheduler 调度器
     * @Description:启动所有定时任务
     * @Title: QuartzManager.java
     */
    public static void startJobs(Scheduler scheduler) {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param scheduler 调度器
     * @Description:关闭所有定时任务
     */
    public static void shutdownJobs(Scheduler scheduler) {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}