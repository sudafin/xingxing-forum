package com.xingxingforum.constants;

public interface BadRequestConstant {
    String REQUEST_PARAM_MISSING = "缺少必要的参数";
    String SERVER_INTER_ERROR = "服务器内部错误";
    String INVALID_USER_TYPE = "无效的用户类型";
    String INVALID_ROLE_TYPE = "无效的角色类型";
    String ACCOUNT_NOT_EXIST = "账号不存在";
    String ACCOUNT_DISABLED = "账号被禁用";
    String TOKEN_EXPIRED = "Token已过期";
    String INVALID_TOKEN = "无效的Token";
    String TOKEN_GENERATE_FAILED = "Token生成失败";
    String ACCOUNT_PASSWORD_ERROR = "账号或密码错误";
    String ROLE_NOT_EXIST = "角色不存在";
    String DATA_FIELD_NAME_CREATE_TIME = "createTime";
    String USER_ACCOUNT_PASSWORD_ERROR = "用户账号密码有误";
    String USER_ALREADY_EXIST = "用户已存在";
    String REQUEST_ID_HEADER = "X-Request-ID";
    String USER_NOT_EXIST = "用户不存在";
    String FORUM_NOT_EXIST =  "版块不存在";
    String PARENT_FORUM_NOT_EXIST = "父模板不存在";
    String FORUM__FAVORITE_CONFLICT = "喜爱操作发生冲突";
    String THREAD_NOT_EXIST = "帖子不存在";
    String USER_PRIVACY_NOT_EXIST = "用户隐私数据不存在";
    String USER_FAVORITE_NOT_EXIST = "用户没有收藏帖子";
}
