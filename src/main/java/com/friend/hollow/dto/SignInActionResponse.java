package com.friend.hollow.dto;

/**
 * 执行签到后的返回对象。
 */
public class SignInActionResponse extends SignInStatusResponse {

    /**
     * 操作结果提示语。
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
