package com.xingxingforum.filter;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
//处理requestBody只能被获取一次的问题 靠CxmHttpServletRequestWrapper和CachedServletInputStream然后这个过滤器和MangerWebConfig配置过滤器
public class CxmRequestValidFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //包装请求和响应,用于程序中获取body
        HttpServletRequest requestWrapper = new CxmHttpServletRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse(); // 确保将缓存内容写回原始响应
    }
}
