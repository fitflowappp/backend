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

import com.magpie.cache.UserCacheService;
import com.magpie.fm.ResourceService;
import com.magpie.resource.constant.ResourceType;
import com.magpie.resource.model.Resource;
import com.magpie.resource.model.ResourceRef;
import com.magpie.session.ActiveUser;
import com.magpie.session.AdminUser;
import com.magpie.user.model.UserRef;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * 文件上传
 * 
 * @author msdj0
 * 
 */
@RestController
@RequestMapping(value = "/upload")
public class FileUploadApi {

	// 资源处理服务类
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private UserCacheService userCacheService;

	@RequestMapping(method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	@ResponseBody
	@ApiOperation("上传文件")
	public List<ResourceRef> uploadStandaloneFile(@ActiveUser UserRef userRef, final HttpServletRequest request,
			@RequestParam("file") MultipartFile[] multipartFiles) {

		String code = request.getHeader("Authorization");
		UserRef user = userCacheService.getBySessionId(code);
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

	// /**
	// * 功能：微信端头像保存，base64转图片并保存<br>
	// *
	// * @param userRef
	// * @param image
	// * base64Image:必需<br>
	// * uid:必需<br>
	// * @return Resource
	// * @time 20160310
	// */
	// @RequestMapping(value = "/base64image", method = RequestMethod.POST)
	// @ResponseBody
	// public ResourceRef saveBase64Image(@ActiveUser UserRef userRef,
	// @RequestBody Base64Image image) {
	// image.setUid(userRef.getId());
	// Resource resource = resourceService.saveBase64Image(image);
	// ResourceRef ref = new ResourceRef();
	// BeanUtils.copyProperties(resource, ref);
	// return ref;
	// }
}
