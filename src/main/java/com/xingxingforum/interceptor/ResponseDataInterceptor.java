package com.xingxingforum.interceptor;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.xingxingforum.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ResponseDataInterceptor implements HandlerInterceptor {


    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        try {
            // 获取缓存的响应内容
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            byte[] content = responseWrapper.getContentAsByteArray();
            String contentStr = new String(content, StandardCharsets.UTF_8);
            // 反序列化和修改数据
            R<Object> r = JSONUtil.toBean(contentStr, R.class, true);
            //设置requestId
            r.setRequestId(IdUtil.randomUUID());
            // 序列化和写回响应
            String newContent = JSONUtil.toJsonStr(r);
            byte[] newBytes = newContent.getBytes(StandardCharsets.UTF_8);
            responseWrapper.resetBuffer();
            responseWrapper.setContentLength(newBytes.length);  // 更新内容长度
            responseWrapper.getOutputStream().write(newBytes);  // 写入新内容
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
