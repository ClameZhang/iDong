/**
 * @copyright JCBC limited Co.
 */
package com.jbcb.idong.model;

import java.util.Date;
import java.util.Set;

/**
 * Data model of iDong party. 
 * Should not be extended.
 * @author Jiayu
 */
public final class Party {
	
	public Party() {}
	
	public Long getPartyID() {
		return partyID;
	}

	public void setPartyID(Long partyID) {
		this.partyID = partyID;
	}

	public Long getLauncherUserID() {
		return launcherUserID;
	}

	public void setLauncherUserID(Long launcherUserID) {
		this.launcherUserID = launcherUserID;
	}

	public Long getStadiumID() {
		return stadiumID;
	}

	public void setStadiumID(Long stadiumID) {
		this.stadiumID = stadiumID;
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

	public String getLocation() {
		return location;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<String> getPartyImageURLSet() {
		return partyImageURLSet;
	}

	public void setPartyImageURLSet(Set<String> partyImageURLSet) {
		this.partyImageURLSet = partyImageURLSet;
	}

	public Date getPartyStartTime() {
		return partyStartTime;
	}

	public void setPartyStartTime(Date partyStartTime) {
		this.partyStartTime = partyStartTime;
	}

	public Date getPartyEndTime() {
		return partyEndTime;
	}

	public void setPartyEndTime(Date partyEndTime) {
		this.partyEndTime = partyEndTime;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	private Long partyID;					//PK for party
	private Long launcherUserID; 			//User id of party launcher
	private Long stadiumID;					//Stadium id of party held
	
	private String title;
	private String description;
	private String location;
	
	private String thumbnailURL;
	
	private Set<String> partyImageURLSet;	//Image URLs for party, should not be repeated
	
	private Date partyStartTime;
	private Date partyEndTime;
	
	private Integer category;				//Party category, 0 stands for basketball game etc.
	
}
