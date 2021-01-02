package com.nurbk.ps.finalproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ApiError {


    @SerializedName("code")
    @Expose
    private int code;


    @SerializedName("message")
    @Expose
    private String message;


    @Expose(serialize = false, deserialize = false)
    private boolean isRecoverable;


    public ApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiError(String message) {
        this.message = message;
        this.isRecoverable = true;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRecoverable() {
        return isRecoverable;
    }

    public void setRecoverable(boolean recoverable) {
        isRecoverable = recoverable;
    }

}