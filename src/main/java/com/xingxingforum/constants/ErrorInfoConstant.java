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
        int FAILED = 400;
        // 用户相关错误
        int USER_NOT_FOUND = 1001; // 用户不存在
        int INVALID_CREDENTIALS = 1002; // 无效的凭证
        int USER_ALREADY_EXISTS = 1003; // 用户已存在
        int INVALID_EMAIL_FORMAT = 1004; // 邮箱格式无效
        int PASSWORD_TOO_WEAK = 1005; // 密码强度不足
        int EMAIL_NOT_VERIFIED = 1006; // 邮箱未验证
        int USERNAME_ALREADY_TAKEN = 1007; // 用户名已被占用
        int INVALID_USERNAME_FORMAT = 1008; // 用户名格式无效

        // 帖子相关错误
        int POST_NOT_FOUND = 2001; // 帖子不存在
        int INVALID_POST_CONTENT = 2002; // 帖子内容无效
        int CANNOT_DELETE_POST = 2003; // 无法删除帖子
        int CANNOT_EDIT_POST = 2004; // 无法编辑帖子
        int POST_TITLE_TOO_LONG = 2005; // 帖子标题过长
        int POST_CONTENT_TOO_LONG = 2006; // 帖子内容过长
        int POST_ATTACHMENT_LIMIT_EXCEEDED = 2007; // 帖子附件数量超出限制

        // 评论相关错误
        int COMMENT_NOT_FOUND = 3001; // 评论不存在
        int INVALID_COMMENT_CONTENT = 3002; // 评论内容无效
        int CANNOT_DELETE_COMMENT = 3003; // 无法删除评论
        int CANNOT_EDIT_COMMENT = 3004; // 无法编辑评论
        int COMMENT_CONTENT_TOO_LONG = 3005; // 评论内容过长
        int TOO_MANY_COMMENTS = 3006; // 评论数量过多

        // 论坛相关错误
        int FORUM_NOT_FOUND = 4001; // 论坛不存在
        int INVALID_forum_NAME = 4002; // 论坛名称无效
        int CANNOT_CREATE_forum = 4003; // 无法创建论坛
        int CANNOT_DELETE_forum = 4004; // 无法删除论坛
        int FORUM_ALREADY_EXISTS = 4005; // 论坛已存在
        int FORUM_DESCRIPTION_TOO_LONG = 4006; // 论坛描述过长
        int FORUM_CATEGORY_NOT_FOUND = 4007; // 论坛分类不存在

        // 隐私相关错误
        int PRIVACY_PERMISSION_DENIED = 5001; // 隐私权限被拒绝
        int USER_PROFILE_PRIVATE = 5002; // 用户资料为私密状态
        int POST_FROM_PRIVATE_USER = 5003; // 帖子来自私密用户，无法查看
        int COMMENT_FROM_PRIVATE_USER = 5004; // 评论来自私密用户，无法查看
    }
}
