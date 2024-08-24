package com.ytl.crm.service.ws.define.exec;

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
    void acquireTaskBizData();

    /**
     * 创建任务动作
     */
    void createAction();

    /**
     * 执行任务动作
     */
    void execTaskAction();

    /**
     * 任务回调
     * 1.发消息动作回调
     */
    void callBackTask();

    /**
     * 任务补偿 - 对于未执行的任务进行补偿
     */
    void compensateTask();

    /**
     * 任务补偿 - 对于未执行的任务进行补偿
     */
    void compensateCallBack();

}
