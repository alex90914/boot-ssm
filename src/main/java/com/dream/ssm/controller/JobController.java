package com.dream.ssm.controller;

import com.dream.ssm.service.JobService;
import com.dream.ssm.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping("/addJob")
    public Result<Boolean> addJob() {
        return jobService.addJob();
    }

    @PostMapping("/delJob")
    public Result<Boolean> delJob() {
        return jobService.delJob();
    }

}
