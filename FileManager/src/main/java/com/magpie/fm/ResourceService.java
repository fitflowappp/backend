package com.magpie.fm;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.base.utils.Codec;
import com.magpie.resource.constant.ResourceContentType;
import com.magpie.resource.constant.ResourceType;
import com.magpie.resource.model.Resource;


@Service
public class ResourceService {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private ResourceDao resourceDao;

	@Autowired
	private UploadConfig uploadConfig;
	
	@Autowired
	private ImgService imgService;

	public Resource getLocalResource(String fileUri) {
		Resource resource = this.getResourceByUri(fileUri);
		this.generateLocalPath(resource);
		return resource;
	}

	public void generateLocalPath(Resource resource) {
		if (resource == null) {
			return;
		}

		String contentUri = resource.getContentUri();
		// // 临时资源
		// if (resource.isTemp()) {
		// String temp = contentUri.substring(contentUri.indexOf(uploadConfig
		// .getShowTemp()) + uploadConfig.getShowTemp().length());
		// resource.setNativeContentUri(uploadConfig.getUploadTemp() + "/"
		// + temp);
		// } else {
		// // 正式资源

		String baseDir = uploadConfig.getPrivatePath();
		if (resource.isPublic()) {
			// 公共文件
			baseDir = uploadConfig.getPublicPath();
		}

		if (ResourceContentType.IMG.getCode().equalsIgnoreCase(resource.getContentType())) {

			String temp = contentUri.substring(contentUri.indexOf(uploadConfig.getShowImgPath())
					+ uploadConfig.getShowImgPath().length());
			resource.setNativeContentUri(baseDir + uploadConfig.getUploadImgPath() + "/" + temp);

		} else if (ResourceContentType.VIDEO.getCode().equalsIgnoreCase(resource.getContentType())) {
			String temp = contentUri.substring(contentUri.indexOf(uploadConfig.getShowVideoPath())
					+ uploadConfig.getShowVideoPath().length());
			resource.setNativeContentUri(baseDir + uploadConfig.getUploadVideoPath() + "/" + temp);

			String coverPage = resource.getCoverUri();
			if (!StringUtils.isEmpty(coverPage)) {
				String coverTemp = coverPage.substring(coverPage.indexOf(uploadConfig.getShowImgPath())
						+ uploadConfig.getShowImgPath().length());
				resource.setNativeCoverPageUri(baseDir + uploadConfig.getUploadImgPath() + "/" + coverTemp);
			}

		} else {
			String temp = contentUri.substring(contentUri.indexOf(uploadConfig.getShowOtherPath())
					+ uploadConfig.getShowOtherPath().length());
			resource.setNativeContentUri(baseDir + uploadConfig.getUploadOtherPath() + temp);
		}
		// }
	}

	// 输入流保存为文件
	public boolean saveFile(String file, InputStream in) {
		Path pathFile = Paths.get(file);
		Path pathDir = Paths.get(file.substring(0, file.lastIndexOf("/")));
		try {
			if (!Files.exists(pathDir)) {
				Files.createDirectories(pathDir);
			}
			Files.copy(in, pathFile, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	// 创建资源
	public Resource createResource(String uid, String originalFileName, InputStream in, ResourceType type,
			boolean isPublic) {
		Resource resource = new Resource();

		String baseDir = uploadConfig.getPrivatePath();
		String preDir = "";
		if (isPublic) {
			preDir = "/public";
			// 公共文件
			baseDir = uploadConfig.getPublicPath();
		}
		resource.setPublic(isPublic);

		String dir = "/" + Calendar.getInstance().get(Calendar.YEAR) + "" + Calendar.getInstance().get(Calendar.MONTH)
				+ "/";

		if (!StringUtils.isEmpty(uid)) {
			dir += (uid + "/");
		}

		// 原始文件名
		resource.setOriginalFileName(originalFileName);
		// 原始扩展名 .jpg等
		resource.setExtName((originalFileName.lastIndexOf(".") == -1) ? "" : originalFileName
				.substring(originalFileName.lastIndexOf(".")));
		// 存储文件夹 年/月
		resource.setContentDir(dir);

		// 上传的文件是视频
		if (uploadConfig.getAllowedVideoTypes().indexOf(resource.getExtName().toLowerCase()) != -1) {
			resource.setContentType(ResourceContentType.VIDEO.getCode());
			// 存储文件名
			resource.setName(this.generateName(ResourceContentType.VIDEO, originalFileName));
			String showFile = preDir + uploadConfig.getShowVideoPath() + dir + resource.getName()
					+ resource.getExtName();
			// 文件查看Uri
			resource.setContentUri(showFile);

			String file = baseDir + uploadConfig.getUploadVideoPath() + dir + resource.getName()
					+ resource.getExtName();
			// 保存为文件
			if (!saveFile(file, in)) {
				return null;
			}
			resource.setSize(new File(file).length());

			// // 获取视频信息，时长，分辨率，bit率等
			// VideoModel videoModel = videoCaptureService.fetchVideoInfo(file);
			// // 视频详细信息
			// resource.setVideoModel(videoModel);
			// // 视频时长
			// resource.setDuration(videoModel.getIntDuration());
			//
			// // 视频封面扩展名
			// resource.setCoverExt(uploadConfig.getDefaultImgType());
			// // 视频封面名称
			// resource.setCoverName(this.generateName(ResourceContentType.IMG,
			// originalFileName));
			// String coverPageFile = baseDir + uploadConfig.getUploadImgPath()
			// + dir + resource.getCoverName() + resource.getCoverExt();
			// // 截屏
			// videoCaptureService.snapshotMovie(videoModel, coverPageFile);
			// // 封面uri
			// resource.setCoverUri(preDir + uploadConfig.getShowImgPath() + dir
			// + resource.getCoverName() + resource.getCoverExt());

		} else if (uploadConfig.getAllowedImgTypes().indexOf(resource.getExtName().toLowerCase()) != -1) {

			// 图片
			resource.setContentType(ResourceContentType.IMG.getCode());

			resource.setName(this.generateName(ResourceContentType.IMG, originalFileName));
			String file = baseDir + uploadConfig.getUploadImgPath() + dir + resource.getName() + resource.getExtName();
			// 保存为文件
			if (!saveFile(file, in)) {
				return null;
			}
			resource.setSize(new File(file).length());
			try {
				// // 取得图片尺寸大小
				Dimension dimension = imgService.getImageDimension(new File(file));
				resource.setHeight((int) dimension.getHeight());
				resource.setWidth((int) dimension.getWidth());
			} catch (Throwable e) {
				System.out.println("获取图片尺寸出错!");
			}

			String showFile = preDir + uploadConfig.getShowImgPath() + dir + resource.getName() + resource.getExtName();
			resource.setContentUri(showFile);

		} else {

			resource.setContentType(ResourceContentType.OTHER.getCode());
			resource.setName(this.generateName(ResourceContentType.OTHER, originalFileName));
			String file = baseDir + uploadConfig.getUploadOtherPath() + dir + resource.getName()
					+ resource.getExtName();
			// 保存为文件
			if (!saveFile(file, in)) {
				return null;
			}
			resource.setSize(new File(file).length());
			String showFile = preDir + uploadConfig.getShowOtherPath() + dir + resource.getName()
					+ resource.getExtName();
			resource.setContentUri(showFile);
		}
		// 资源来源
		if (type != null) {
			resource.setType(type.getCode());
			// 抓取资源，直接保存为正式文件,且直接保存到
			if (type == ResourceType.OUTERSOURCE) {

				resource.setTemp(false);
			} else if (type == ResourceType.STANDALONE_WORK) {
				// 作品集页面本地上传的图片，直接升级为正式资源
				resource.setTemp(false);

			}
		}
		this.saveResource(uid, resource);
		return resource;
	}

	// 生成文件名
	private String generateName(ResourceContentType type, String key) {
		String value = System.currentTimeMillis() + key;
		return type.getCode() + "_" + Codec.generateToken(value);
	}

	private void saveResource(String uid, Resource resource) {

		resource.setDownloaded(true);// 已下载到本地服务器
		resourceDao.save(resource);
	}

	public void savePermanentResource(String uid, Resource resource) {
		// 资源状态变为正式
		resource.setTemp(false);
		this.saveResource(uid, resource);
	}

	public Resource savePermanentResource(String uid, String uri) {
		Resource resource = this.getResourceByUri(uri);
		if (resource != null) {

			// 资源状态变为正式
			resource.setTemp(false);
			this.savePermanentResource(uid, resource);
			return resource;
		} else {
			return null;
		}
	}

	public Resource savePermanentResource(String uid, String uri, String type) {
		Resource resource = this.getResourceByUri(uri);
		if (resource != null) {
			// 资源状态变为正式
			resource.setTemp(false);
			resource.setType(type);
			this.savePermanentResource(uid, resource);
			return resource;
		} else {
			return null;
		}
	}

	// @Override
	// public void deletePersistResource(Resource resource) {
	// if (resource == null) {
	// return;
	// }
	//
	// String fullName = resource.getName() + resource.getExtName();
	// String fullCoverName = resource.getCoverName() + resource.getCoverExt();
	// String defaultFullCoverName = resource.getName()
	// + uploadConfig.getDefaultImgType();
	//
	// if (ResourceContentType.IMG.getCode().equalsIgnoreCase(
	// resource.getContentType())) {
	// new File(uploadConfig.getUploadImgPath() + resource.getContentDir()
	// + fullName).delete();
	// } else if (ResourceContentType.VIDEO.getCode().equalsIgnoreCase(
	// resource.getContentType())) {
	// // 删除视频
	// new File(uploadConfig.getUploadVideoPath()
	// + resource.getContentDir() + fullName).delete();
	// // 删除封面
	// new File(uploadConfig.getUploadImgPath() + resource.getContentDir()
	// + fullCoverName).delete();
	// // 删除默认封面
	// new File(uploadConfig.getUploadImgPath() + resource.getContentDir()
	// + defaultFullCoverName).delete();
	//
	// } else {
	// new File(uploadConfig.getUploadOtherPath()
	// + resource.getContentDir() + fullName).delete();
	// }
	// this.deleteResource(resource.getId());
	//
	// }

	/**
	 * 根据Uri取得资源
	 */
	public Resource getResourceByUri(String uri) {
		return resourceDao.getResourceByUri(uri);
	}

	public void deleteResource(String id) {
		// Duplicate duplicate = new Duplicate();
		// duplicate.setResource(resourceDao.findOne(id));
		// duplicateDao.save(duplicate);
		resourceDao.delete(id);
	}

	public Resource getResource(String id) {
		return resourceDao.findOne(id);
	}

	private int parseMS(String duration) {

		// 字符串 02:30:23.35
		if (duration.indexOf(".") != -1) {
			duration = duration.substring(0, duration.indexOf("."));
		}
		String[] parts = duration.split(":");
		return Integer.parseInt(parts[0], 10) * 60 * 60 + Integer.parseInt(parts[1], 10) * 60
				+ Integer.parseInt(parts[2], 10);
	}

	// @Override
	// public ResourceRef getResourceRef(String id) {
	// Resource resource = resourceDao.findOne(id);
	// ResourceRef ref = new ResourceRef();
	// BeanUtils.copyProperties(resource, ref);
	// return ref;
	// }

	public Resource download(String uid, String url) {
		return download(uid, url, false);
	}

	// cover:是否覆盖之前的文件
	public Resource download(String uid, String url, boolean cover) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		url = url.replaceAll("&amp;|amp;", "&");

		String href = "http:/www.chsi.com.cn/report/img/" + uid + ".pdf";
		String fileName = href.lastIndexOf("/") != -1 ? href.substring(url.lastIndexOf("/") + 1) : url;
		// String fileName = uid + ".jpg";
		Resource persistResource = resourceDao.getResourseByOriginalName(fileName);
		if (!cover && persistResource != null) {
			// 资源已经存在
			return persistResource;
		}

		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(url);
		try {
			httpGet.setRequestHeader(new Header("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36"));
			client.executeMethod(httpGet);
			Resource resource = createResource(uid, fileName, httpGet.getResponseBodyAsStream(),
			// IOUtil.getInputStream(file.getAbsolutePath()),
					ResourceType.OUTERSOURCE, false);
			return resource;

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}

		return null;
	}

	public Resource download(String uid, GetMethod httpGet, boolean cover) {
		String fileName = "xuexinxj" + uid + ".jpg";
		Resource persistResource = resourceDao.getResourseByOriginalName(fileName);
		if (!cover && persistResource != null) {
			// 资源已经存在
			return persistResource;
		}
		HttpClient client = new HttpClient();
		try {
			client.executeMethod(httpGet);
			Resource resource = createResource(uid, fileName, httpGet.getResponseBodyAsStream(),
					ResourceType.OUTERSOURCE, false);
			return resource;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
		return null;
	}

	public Resource downloadByUrl(String uid, String url, boolean cover) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		url = url.replaceAll("&amp;|amp;", "&");

		// String href = "http:/www.chsi.com.cn/report/img/" + uid + ".pdf";
		String fileName = url.lastIndexOf("/") != -1 ? url.substring(url.lastIndexOf("/") + 1) : url;
		// String fileName = uid + ".jpg";
		Resource persistResource = resourceDao.getResourseByOriginalName(fileName);
		if (!cover && persistResource != null) {
			// 资源已经存在
			return persistResource;
		}

		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(url);
		try {
			httpGet.setRequestHeader(new Header("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36"));
			client.executeMethod(httpGet);
			Resource resource = createResource(uid, fileName, httpGet.getResponseBodyAsStream(),
			// IOUtil.getInputStream(file.getAbsolutePath()),
					ResourceType.OUTERSOURCE, false);
			return resource;

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}

		return null;
	}
	
}
