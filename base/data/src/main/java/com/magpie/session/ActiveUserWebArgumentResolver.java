package com.magpie.session;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.magpie.cache.AdminCacheService;
import com.magpie.cache.UserCacheService;
import com.magpie.user.model.UserRef;


@Service
public class ActiveUserWebArgumentResolver implements WebArgumentResolver {

	@Autowired
	private UserCacheService userCacheService;
	@Autowired
	private AdminCacheService adminCacheService;

	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) {
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
}