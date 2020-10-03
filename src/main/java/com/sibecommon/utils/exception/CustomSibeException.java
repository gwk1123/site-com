package com.sibecommon.utils.exception;

public class CustomSibeException extends RuntimeException {

    private final String status;
    private final String msg;
    private final String uid;
    private final String method;

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Instantiates a new Custom sibe exception.
     *
     * @param status the status
     * @param msg    the msg
     * @param uid    the uid
     * @param method the method
     */
    public CustomSibeException(String status, String msg, String uid,String method) {
        this.status = status;
        this.msg = msg;
        this.uid = uid;
        this.method = method;
    }

    /**
     * Gets custom ota error vm.
     *
     * @return the custom ota error vm
     */
    public CustomSibeException getCustomSibeException() {
        return new CustomSibeException(status,msg,uid,method);
    }


}
