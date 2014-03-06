package com.jasonxuli.test.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class Publisher {

	private long sharding ;
    private String name ;
    private String url ;
    private String phone ;
    private long inputSize ;
    private long outputSize ;
    private boolean trial ;
    private long expiredTime ;
    private boolean acceptLicense ;
    private String type ;
    private String business ;
    private long maxStorage ;
    private long usedStorage ;
    private long maxTraffic ;
    private long usedTraffic ;
    private boolean storageOverflow ;
    private boolean trafficOverflow ;
    private long balance ;
    private boolean overdue ;
    private long billDay ;
    private long billDayUpdatedTime ;
    private long viewedTime ;
    private long maxViewedTime ;
    private long registeredTime ;
    private long formalTime ;
    private long chargeTime ;
    private String chargeType ;
    private String chargeDuration ;
    private long maxKbps ;
    private long latestResetTime ;
    private long videoNums ;
    private String id ;
    private long version ;
    private long creationTime ;
    private long modifiedTime ;
    
    
    public Publisher(JSONObject json){
    	try {
    		setSharding(json.getLong("sharding"));
    		setName(json.getString("name"));
    		setUrl(json.getString("url"));
    		setPhone(json.getString("phone"));
    		setInputSize(json.getLong("inputSize"));
    		setOutputSize(json.getLong("outputSize"));
    		setTrial(json.getBoolean("trial"));
    		setExpiredTime(json.getLong("expiredTime"));
    		setAcceptLicense(json.getBoolean("acceptLicense"));
    		setType(json.getString("type"));
    		setBusiness(json.getString("business"));
    		setMaxStorage(json.getLong("maxStorage"));
    		setUsedStorage(json.getLong("usedStorage"));
    		setMaxTraffic(json.getLong("maxTraffic"));
    		setUsedTraffic(json.getLong("usedTraffic"));
    		setStorageOverflow(json.getBoolean("storageOverflow"));
    		setTrafficOverflow(json.getBoolean("trafficOverflow"));
    		setBalance(json.getLong("balance"));
    		setOverdue(json.getBoolean("overdue"));
    		setBillDay(json.getLong("billDay"));
    		setBillDayUpdatedTime(json.getLong("billDayUpdatedTime"));
    		setViewedTime(json.getLong("viewedTime"));
    		setMaxViewedTime(json.getLong("maxViewedTime"));
    		setRegisteredTime(json.getLong("registeredTime"));
    		setFormalTime(json.getLong("formalTime"));
    		setChargeTime(json.getLong("chargeTime"));
    		setChargeType(json.getString("chargeType"));
    		setChargeDuration(json.getString("chargeDuration"));
    		setMaxKbps(json.getLong("maxKbps"));
    		setLatestResetTime(json.getLong("latestResetTime"));
    		setVideoNums(json.getLong("videoNums"));
    		setId(json.getString("id"));
    		setVersion(json.getLong("version"));
    		setCreationTime(json.getLong("creationTime"));
    		setModifiedTime(json.getLong("modifiedTime"));
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    
    
	public long getSharding() {
		return sharding;
	}
	public void setSharding(long sharding) {
		this.sharding = sharding;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public long getInputSize() {
		return inputSize;
	}
	public void setInputSize(long inputSize) {
		this.inputSize = inputSize;
	}
	public long getOutputSize() {
		return outputSize;
	}
	public void setOutputSize(long outputSize) {
		this.outputSize = outputSize;
	}
	public boolean isTrial() {
		return trial;
	}
	public void setTrial(boolean trial) {
		this.trial = trial;
	}
	public long getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}
	public boolean isAcceptLicense() {
		return acceptLicense;
	}
	public void setAcceptLicense(boolean acceptLicense) {
		this.acceptLicense = acceptLicense;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public long getMaxStorage() {
		return maxStorage;
	}
	public void setMaxStorage(long maxStorage) {
		this.maxStorage = maxStorage;
	}
	public long getUsedStorage() {
		return usedStorage;
	}
	public void setUsedStorage(long usedStorage) {
		this.usedStorage = usedStorage;
	}
	public long getMaxTraffic() {
		return maxTraffic;
	}
	public void setMaxTraffic(long maxTraffic) {
		this.maxTraffic = maxTraffic;
	}
	public long getUsedTraffic() {
		return usedTraffic;
	}
	public void setUsedTraffic(long usedTraffic) {
		this.usedTraffic = usedTraffic;
	}
	public boolean isStorageOverflow() {
		return storageOverflow;
	}
	public void setStorageOverflow(boolean storageOverflow) {
		this.storageOverflow = storageOverflow;
	}
	public boolean isTrafficOverflow() {
		return trafficOverflow;
	}
	public void setTrafficOverflow(boolean trafficOverflow) {
		this.trafficOverflow = trafficOverflow;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public boolean isOverdue() {
		return overdue;
	}
	public void setOverdue(boolean overdue) {
		this.overdue = overdue;
	}
	public long getBillDay() {
		return billDay;
	}
	public void setBillDay(long billDay) {
		this.billDay = billDay;
	}
	public long getBillDayUpdatedTime() {
		return billDayUpdatedTime;
	}
	public void setBillDayUpdatedTime(long billDayUpdatedTime) {
		this.billDayUpdatedTime = billDayUpdatedTime;
	}
	public long getViewedTime() {
		return viewedTime;
	}
	public void setViewedTime(long viewedTime) {
		this.viewedTime = viewedTime;
	}
	public long getMaxViewedTime() {
		return maxViewedTime;
	}
	public void setMaxViewedTime(long maxViewedTime) {
		this.maxViewedTime = maxViewedTime;
	}
	public long getRegisteredTime() {
		return registeredTime;
	}
	public void setRegisteredTime(long registeredTime) {
		this.registeredTime = registeredTime;
	}
	public long getFormalTime() {
		return formalTime;
	}
	public void setFormalTime(long formalTime) {
		this.formalTime = formalTime;
	}
	public long getChargeTime() {
		return chargeTime;
	}
	public void setChargeTime(long chargeTime) {
		this.chargeTime = chargeTime;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public String getChargeDuration() {
		return chargeDuration;
	}
	public void setChargeDuration(String chargeDuration) {
		this.chargeDuration = chargeDuration;
	}
	public long getMaxKbps() {
		return maxKbps;
	}
	public void setMaxKbps(long maxKbps) {
		this.maxKbps = maxKbps;
	}
	public long getLatestResetTime() {
		return latestResetTime;
	}
	public void setLatestResetTime(long latestResetTime) {
		this.latestResetTime = latestResetTime;
	}
	public long getVideoNums() {
		return videoNums;
	}
	public void setVideoNums(long videoNums) {
		this.videoNums = videoNums;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
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
