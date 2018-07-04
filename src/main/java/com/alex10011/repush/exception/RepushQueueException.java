package com.alex10011.repush.exception;

/**
 * @Auther: yl
 * @Date: 2018/6/21 11:00
 * @Description:
 */
public class RepushQueueException extends Exception {
    public RepushQueueException() {
        super();
    }
    public RepushQueueException(String msg) {
        super(msg);
    }
    public RepushQueueException(String msg,Exception e){
        super(msg,e);
    }
}
