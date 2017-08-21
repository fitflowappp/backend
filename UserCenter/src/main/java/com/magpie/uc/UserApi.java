package com.magpie.uc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.user.model.UserRef;
import com.magpie.cache.UserCacheService;

@RestController
@RequestMapping(value="/user")
public class UserApi {

	@Autowired
	private UserCacheService UserCacheService;
	
	
	@RequestMapping(method=RequestMethod.GET)
	public void register() {
		
		UserRef userRef = new UserRef();
		userRef.setId("123456");
		userRef.setPhone("31245");
		UserCacheService.saveUser(userRef);
		
	}
	
	
}
