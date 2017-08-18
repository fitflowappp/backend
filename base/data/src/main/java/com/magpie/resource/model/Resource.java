package com.magpie.resource.model;


public class Resource {

	// 资源ID
	private String id;

	// 资源名
	private String name;

	// 原始文件名
	private String originalFileName;

	// 资源URI（外部浏览用）
	private String contentUri;

	// 内部存储uri
	private String nativeContentUri;
	// 内部存储uri
	private String nativeCoverPageUri;

	// 视频时长，其他文件无效
	private int duration;

	// 文件大小(byte)
	private long size;

	// 封面URI
	private String coverUri;

	private String contentDir;

	private String contentType;
	private String coverDir;
	private String coverExt;

	private String coverName;

	private String description;

	private String extName;
	private String fileCode;

	private int height;
	private int width;
	// 临时文件flag
	// 默认是临时文件
	private boolean temp = true;
	// 资源类型 see ResourceType
	private String type;

	// 已下载到本地服务器
	private boolean downloaded;

	// 是否有引用的资源
	private boolean alive;
	// 是否增加了logo
	private boolean watermark;
	// 是否公开文件
	private boolean isPublic;


	public Resource() {

	}


	public String getCoverUri() {
		return coverUri;
	}

	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getContentUri() {
		return contentUri;
	}

	public void setContentUri(String contentUri) {
		this.contentUri = contentUri;
	}

	public String getNativeContentUri() {
		return nativeContentUri;
	}

	public void setNativeContentUri(String nativeContentUri) {
		this.nativeContentUri = nativeContentUri;
	}

	public String getNativeCoverPageUri() {
		return nativeCoverPageUri;
	}

	public void setNativeCoverPageUri(String nativeCoverPageUri) {
		this.nativeCoverPageUri = nativeCoverPageUri;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContentDir() {
		return contentDir;
	}

	public String getContentType() {
		return contentType;
	}

	public String getCoverDir() {
		return coverDir;
	}

	public String getCoverExt() {
		return coverExt;
	}

	public String getCoverName() {
		return coverName;
	}

	public String getDescription() {
		return description;
	}

	public String getExtName() {
		return extName;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setContentDir(String contentDir) {
		this.contentDir = contentDir;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setCoverDir(String coverDir) {
		this.coverDir = coverDir;
	}

	public void setCoverExt(String coverExt) {
		this.coverExt = coverExt;
	}

	public void setCoverName(String coverName) {
		this.coverName = coverName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public boolean isTemp() {
		return temp;
	}

	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isWatermark() {
		return watermark;
	}

	public void setWatermark(boolean watermark) {
		this.watermark = watermark;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isDownloaded() {
		return downloaded;
	}

	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

}
