package com.alex10011.repush.exception;

public class RepushConfigException extends Exception {

    private static final long serialVersionUID = -2942008062274647617L;

    public RepushConfigException() {
        super();
    }
    public RepushConfigException(String msg) {
        super(msg);
    }
    public RepushConfigException(String msg,Exception e){
        super(msg,e);
    }
}
