package com.dream.ssm.service;

import com.dream.ssm.utils.Result;

public interface JobService {
    Result<Boolean> addJob();

    Result<Boolean> delJob();
}
