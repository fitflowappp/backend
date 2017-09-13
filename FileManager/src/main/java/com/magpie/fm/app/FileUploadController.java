package com.magpie.fm.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.magpie.cache.AdminCacheService;
import com.magpie.cache.UserCacheService;
import com.magpie.fm.ResourceService;
import com.magpie.resource.constant.ResourceType;
import com.magpie.resource.model.Resource;
import com.magpie.session.ActiveUser;
import com.magpie.session.AdminUser;
import com.magpie.share.ResourceRef;
import com.magpie.share.UserRef;

/**
 * 
 * 文件上传
 * 
 * @author msdj0
 * 
 */
@RestController
@RequestMapping(value = "/manage/upload")
public class FileUploadController {

	// 资源处理服务类
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private AdminCacheService adminCacheService;

	@RequestMapping(method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	@ResponseBody
	public List<ResourceRef> uploadStandaloneFile(@AdminUser UserRef userRef, final HttpServletRequest request,
			@RequestParam("file") MultipartFile[] multipartFiles) {

		String code = request.getHeader("Authorization");
		UserRef user = adminCacheService.getBySessionId(code);
		if (user == null || StringUtils.isEmpty(user.getId())) {
			return null;
		}

		List<ResourceRef> resources = new ArrayList<>();
		// handle regular MultipartFile
		if (multipartFiles == null) {
			return resources;
		}
		try {
			for (MultipartFile multipartFile : multipartFiles) {
				Resource resource = resourceService.createResource(user.getId(), multipartFile.getOriginalFilename(),
						multipartFile.getInputStream(), ResourceType.STANDALONE, false);
				if (resource == null) {
					continue;
				}
				ResourceRef ref = new ResourceRef();
				BeanUtils.copyProperties(resource, ref);
				resources.add(ref);
			}
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		return resources;

	}

}
