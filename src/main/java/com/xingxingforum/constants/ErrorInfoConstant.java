package com.xingxingforum.constants;

public interface ErrorInfoConstant {

    interface Msg {
        String OK = "OK";
       String INVALID_VERIFY_CODE = "验证码错误";

        String SERVER_INTER_ERROR = "服务器内部错误";
        String CLIENT_ERROR = "客户端错误";
        String DB_SORT_FIELD_NOT_FOUND = "排序字段不存在";
        String OPERATE_FAILED = "操作失败";

        String REQUEST_PARAM_ILLEGAL = "请求参数不合法";
        String REQUEST_OPERATE_FREQUENTLY = "操作频繁,请稍后重试";
        String REQUEST_TIME_OUT = "请求超时";

        String USER_NOT_EXISTS = "用户信息不存在";
        String INVALID_USER_TYPE = "无效的用户类型";
        String INVALID_TOKEN = "无效的token";
        String EXPIRED_TOKEN = "token已过期";
        String INVALID_TOKEN_PAYLOAD = "token参数格式错误";
        String DATA_FIELD_NAME_CREATE_TIME = "createTime";
    }

    interface Code {
        int SUCCESS = 200;
        int FAILED = 0;
    }
}
