package com.xingxingforum.interceptor;

import cn.hutool.core.util.ObjectUtil;

import cn.hutool.json.JSONUtil;

import com.xingxingforum.entity.dto.users.UserDTO;
import com.xingxingforum.utils.UserContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInfoInterceptor implements HandlerInterceptor {
    //拦截器
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userinfo = request.getAttribute("userInfo");
        UserDTO userDTO = JSONUtil.parseObj(userinfo).toBean(UserDTO.class);
        //转为Object对象转为adminDTO
        if(ObjectUtil.isEmpty(userDTO)){
            response.setStatus(401);
            return false;
        }
        UserContextUtils.setUser(userDTO.getId());
        return true;
    }
    //拦截后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextUtils.removeUser();
    }
}
