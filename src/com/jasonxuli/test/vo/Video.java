package com.jasonxuli.test.vo;

import org.json.JSONException;
import org.json.JSONObject;


public class Video{

	
	public Video(JSONObject json)
	{
		try {
			setPublisherId(json.getString("publisherId"));
			setManagerId(json.getString("managerId"));
			setFolderId(json.getString("folderId"));
			setTitle(json.getString("title"));
			setDescription(json.getString("description"));
			setStatus(json.getString("status"));
			setDeleted(json.getBoolean("deleted"));
			setFileSize(json.getInt("fileSize"));
			setWidth(json.getInt("width"));
			setHeight(json.getInt("height"));
			setDuration(json.getInt("duration"));
			setKbps(json.getInt("kbps"));
			setFrameRate(json.getInt("frameRate"));
			setAudioChannels(json.getInt("audioChannels"));
			setAudioSampleRate(json.getInt("audioSampleRate"));
			setTags(json.getString("tags"));
			setVideoKey(json.getString("videoKey"));
			setVideoUrl(json.getString("videoUrl"));
			setThumbnailUrl(json.getString("thumbnailUrl"));
			setSnapshotUrl(json.getString("snapshotUrl"));
			setCoverUrl(json.getString("coverUrl"));
			setErrorMessage(json.getString("errorMessage"));
			setSnapshotGroupId(json.getString("snapshotGroupId"));
			setRenditionGroupId(json.getInt("renditionGroupId"));
			setIpRestriction(json.getBoolean("ipRestriction"));
			setPublicRestriction(json.getBoolean("publicRestriction"));
			setFileType(json.getString("fileType"));
			setLinkText(json.getString("linkText"));
			setLinkUrl(json.getString("linkUrl"));
			setLocal(json.getBoolean("local"));
			setRemote(json.getBoolean("remote"));
			setAutoBps(json.getBoolean("autoBps"));
			setMetadataUrl(json.getString("metadataUrl"));
			setLockVideo(json.getBoolean("lockVideo"));
			setSharedVideoId(json.getString("sharedVideoId"));
			setHasGeo(json.getBoolean("hasGeo"));
			setLatitude(json.getInt("latitude"));
			setLongitude(json.getInt("longitude"));
			setLastFinishedTime(json.getInt("lastFinishedTime"));
			setEncrypted(json.getBoolean("encrypted"));
			setAudioKbps(json.getInt("audioKbps"));
			setVideoKbps(json.getInt("videoKbps"));
			setCustomFields(json.getString("customFields"));
			setId(json.getString("id"));
			setVersion(json.getInt("version"));
			setCreationTime(json.getInt("creationTime"));
			setModifiedTime(json.getInt("modifiedTime"));
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String toString()
	{
		return this.title;
	}
	
	
    public static enum VideoStatus {

    	//@Label("等待上传")
    	WAITING_UPLOAD,

    	//@Label("文件错误")
    	FILE_ERROR,
    	
    	//@Label("审核转码")
    	AUDIT_TRANSCODING,
    	
    	//@Label("审核推送")
    	AUDIT_DISTRIBUTING,

    	//@Label("等待审核")
    	WAITING_APPROVE,

    	//@Label("审核未通过")
    	NOT_APPROVED,

    	//@Label("等待处理")
    	PENDING,
    	
    	//@Label("合并完成")
    	CLIPPED,

    	//@Label("正在转码")
    	TRANSCODING,

    	//@Label("正在推送")
    	DISTRIBUTING,
    	
    	//@Label("处理元数据")
    	META_MERGING,
    	
    	//@Label("推送元数据")
    	META_DISTRIBUTING,

    	//@Label("处理完成")
    	FINISHED,

    	//@Label("转码错误")
    	TRANSCODE_ERROR,

    	//@Label("推送错误")
    	DISTRIBUTE_ERROR,
    	
    	//@Label("截图错误")
        SNAPSHOT_ERROR,
        
        //@Label("处理元数据错误")
    	META_MERGE_ERROR,
    	
    	//@Label("推送元数据错误")
    	META_DISTRIBUTE_ERROR,
        
        //@Label("视频下线")
        OFFLINE,
        
    	@Deprecated
    	//@Label("NO_RENDITION")
        NO_RENDITION,
    }
    /**
     * 公司ID
     */
    private String publisherId;
    /**
     * 管理员ID
     */
    private String managerId;
    /**
     * 所属目录ID
     */
    private String folderId;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 视频状态
     */
    private String status;
    /**
     * 是否删除
     */
    private boolean deleted;
    /**
     * 文件大小
     */
    private long fileSize;
    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;
    /**
     * 时长
     */
    private int duration;
    /**
     * 码率
     */
    private int kbps;
    /**
     * 帧率
     */
    private int frameRate;
    /**
     * 音频频道
     */
    private int audioChannels;
    /**
     * 音频采样率
     */
    private int audioSampleRate;
    /**
     * 标签
     */
    private String tags;
    /**
     * videoKey
     */
    private String videoKey;
    /**
     * 视频URL地址
     */
    private String videoUrl;
    /**
     * 小图片地址
     */
    private String thumbnailUrl;
    /**
     * 缩略图地址
     */
    private String snapshotUrl;
    /**
     * 封面URL
     */
    private String coverUrl;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 缩略图分组ID
     */
    private String snapshotGroupId;
    /**
     * 编码版本分组ID
     */
    private int renditionGroupId;
    /**
     * 是否有IP限制
     */
    private boolean ipRestriction;
    /**
     * 是否公开限制
     */
    private boolean publicRestriction;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 链接文本
     */
    private String linkText;
    /**
     * 链接URL
     */
    private String linkUrl;
    /**
     * 是否在本地存储
     */
    private boolean local = true;
    /**
     * 是否远端存储
     */
    private boolean remote = false;
    /**
     * autobps
     */
    private boolean autoBps;
    /**
     * metadataURL地址
     */
    private String metadataUrl;
    /**
     * 是否锁定，禁止删除，重新转码等
     */
    private boolean lockVideo;
    /**
     * 共享的原视频ID
     */
    private String sharedVideoId;
    /**
     * 是否包含geo信息
     */
    private boolean hasGeo;
    /**
     * 维度
     */
    double latitude;
    /**
     * 经度
     */
    double longitude;
    /**
     * 最近一次转码成功的时间
     */
    private long lastFinishedTime;
    /**
     * 是否启用DRM
     */
    private boolean encrypted;
    /**
     * 音频码率
     */
    private int audioKbps;
    /**
     * 视频码率
     */
    private int videoKbps;
    
    private String customFields = "";
    private String id = "";
	private int version = 0;
    private long creationTime = 0L;
    private long modifiedTime = 0L;
    
    
    
    public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
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
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getKbps() {
		return kbps;
	}
	public void setKbps(int kbps) {
		this.kbps = kbps;
	}
	public int getFrameRate() {
		return frameRate;
	}
	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}
	public int getAudioChannels() {
		return audioChannels;
	}
	public void setAudioChannels(int audioChannels) {
		this.audioChannels = audioChannels;
	}
	public int getAudioSampleRate() {
		return audioSampleRate;
	}
	public void setAudioSampleRate(int audioSampleRate) {
		this.audioSampleRate = audioSampleRate;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getVideoKey() {
		return videoKey;
	}
	public void setVideoKey(String videoKey) {
		this.videoKey = videoKey;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getSnapshotUrl() {
		return snapshotUrl;
	}
	public void setSnapshotUrl(String snapshotUrl) {
		this.snapshotUrl = snapshotUrl;
	}
	public String getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getSnapshotGroupId() {
		return snapshotGroupId;
	}
	public void setSnapshotGroupId(String snapshotGroupId) {
		this.snapshotGroupId = snapshotGroupId;
	}
	public int getRenditionGroupId() {
		return renditionGroupId;
	}
	public void setRenditionGroupId(int renditionGroupId) {
		this.renditionGroupId = renditionGroupId;
	}
	public boolean isIpRestriction() {
		return ipRestriction;
	}
	public void setIpRestriction(boolean ipRestriction) {
		this.ipRestriction = ipRestriction;
	}
	public boolean isPublicRestriction() {
		return publicRestriction;
	}
	public void setPublicRestriction(boolean publicRestriction) {
		this.publicRestriction = publicRestriction;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getLinkText() {
		return linkText;
	}
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public boolean isLocal() {
		return local;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}
	public boolean isRemote() {
		return remote;
	}
	public void setRemote(boolean remote) {
		this.remote = remote;
	}
	public boolean isAutoBps() {
		return autoBps;
	}
	public void setAutoBps(boolean autoBps) {
		this.autoBps = autoBps;
	}
	public String getMetadataUrl() {
		return metadataUrl;
	}
	public void setMetadataUrl(String metadataUrl) {
		this.metadataUrl = metadataUrl;
	}
	public boolean isLockVideo() {
		return lockVideo;
	}
	public void setLockVideo(boolean lockVideo) {
		this.lockVideo = lockVideo;
	}
	public String getSharedVideoId() {
		return sharedVideoId;
	}
	public void setSharedVideoId(String sharedVideoId) {
		this.sharedVideoId = sharedVideoId;
	}
	public boolean isHasGeo() {
		return hasGeo;
	}
	public void setHasGeo(boolean hasGeo) {
		this.hasGeo = hasGeo;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public long getLastFinishedTime() {
		return lastFinishedTime;
	}
	public void setLastFinishedTime(long lastFinishedTime) {
		this.lastFinishedTime = lastFinishedTime;
	}
	public boolean isEncrypted() {
		return encrypted;
	}
	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}
	public int getAudioKbps() {
		return audioKbps;
	}
	public void setAudioKbps(int audioKbps) {
		this.audioKbps = audioKbps;
	}
	public int getVideoKbps() {
		return videoKbps;
	}
	public void setVideoKbps(int videoKbps) {
		this.videoKbps = videoKbps;
	}
	public String getCustomFields() {
		return customFields;
	}
	public void setCustomFields(String customFields) {
		this.customFields = customFields;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	public long getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
	
}
