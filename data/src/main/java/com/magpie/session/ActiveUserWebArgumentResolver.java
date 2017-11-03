package com.magpie.session;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.magpie.cache.AdminCacheService;
import com.magpie.cache.UserCacheService;
import com.magpie.share.UserRef;


@Service
public class ActiveUserWebArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private UserCacheService userCacheService;
	@Autowired
	private AdminCacheService adminCacheService;


	private Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) {
		Annotation[] annotations = methodParameter.getParameterAnnotations();

		if (methodParameter.getParameterType().equals(UserRef.class)) {
			for (Annotation annotation : annotations) {
				if (ActiveUser.class.isInstance(annotation)) {
					if (webRequest.getHeader("Authorization") != null) {
						return this.getUserRef(webRequest.getHeader("Authorization"));
					} else {
						return new UserRef();
					}
				} else if (AdminUser.class.isInstance(annotation)) {
					if (webRequest.getHeader("Authorization") != null) {
						return this.getAdminUser(webRequest.getHeader("Authorization"));
					} else {
						return new UserRef();
					}
				}
			}
		}

		return WebArgumentResolver.UNRESOLVED;
	}

	private UserRef getAdminUser(String sessionId) {
		UserRef userRef = adminCacheService.getBySessionId(sessionId);
		if (userRef != null && !StringUtils.isEmpty(userRef.getId())) {
			adminCacheService.saveBySessionId(sessionId, userRef);
			return userRef;
		}
		return new UserRef();
	}

	private UserRef getUserRef(String sessionId) {
		UserRef userRef = userCacheService.getBySessionId(sessionId);
		if (userRef != null && !StringUtils.isEmpty(userRef.getId())) {
			return userRef;
		}
		return new UserRef();
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return true;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return resolveArgument(parameter, webRequest);
	}
}