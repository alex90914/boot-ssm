package com.dream.ssm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO implements Serializable {
    private static final long serialVersionUID = -7954883731046205277L;
    /**
     * 任务类型
     */
    private Integer type;
    /**
     * 任务名称唯一
     */
    private String jobName;
    /**
     * 该任务需要的参数
     */
    //private Map<String, Object> params;
    /**
     * 任务执行的时机
     */
    private String cronJ;
    private Date fireDate;
}
