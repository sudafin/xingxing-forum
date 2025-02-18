package com.xingxingforum.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class ElementRecordAspect {

	@Pointcut("@annotation(elementRecord)")
	public void elementRecordAspect(ElementRecord elementRecord) {
	}

	@Around(value = "elementRecordAspect(elementRecord)")
	public void around(ProceedingJoinPoint jp, ElementRecord elementRecord) {
		String acceptJson = "";
		LocalDateTime startTime = LocalDateTime.now();

		String url = "";
		String serviceName = "";
		Object result = null;
		String serailNumber = null;
		try {
			// 获取请求地址
			//获取request对象
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			url = request.getRequestURL().toString();
			// 获取入参
			Object[] args = jp.getArgs();
			for (Object arg : args) {
				acceptJson = arg.toString();
			}
			log.info("acceptJson{}", acceptJson);

			// 主要业务
			jp.proceed();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
    }


}
