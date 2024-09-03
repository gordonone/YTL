package com.ytl.crm.service.ws.define.task;

public interface IMarketingTaskExecLogic {

    /**
     * 触发任务
     * 1.选取可以执行的任务
     * 2.创建一个触发记录，今天不再触发
     */
    void triggerTask();

    /**
     * 获取任务数据
     */
    void pullData();

    /**
     * 创建任务动作
     */
    void createTaskAction();

    /**
     * 执行任务动作
     */
    void execTaskAction();

    /**
     * 任务主动回调
     * 1.仅发消息动作回调
     * 2.根据回调结果确定是否需要进行任务补偿
     */
    void callBackTask();

    /**
     * 任务补偿 - 对于未执行的任务进行补偿
     * 任务补偿的回调也是callBackTask()方法
     */
    void compensateTask();


}