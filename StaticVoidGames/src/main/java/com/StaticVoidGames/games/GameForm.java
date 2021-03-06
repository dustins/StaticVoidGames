package com.StaticVoidGames.games;

import org.springframework.web.multipart.MultipartFile;

/**
 * Class used by the game edit pages. Forms shown to (and submitted by) the user are backed by an instance of this class, so all of the handling can be done in one place.
 * @see com.StaticVoidGames.spring.controller.EditGameController
 *
 */
public class GameForm {

	private String title;
	private String description;

	private String shortDescription;

	private String website;

	private MultipartFile jarFile;
	private MultipartFile sourceFile;

	private MultipartFile faviconFile;
	private MultipartFile backgroundFile;
	private MultipartFile thumbnailFile;

	private String mainClass;
	private Integer webstartWidth;
	private Integer webstartHeight;

	private String appletClass;
	private Integer appletWidth;
	private Integer appletHeight;


	private String appletDescription;

	private String javaVersion;

	private String rating;


	private String adText;
	private boolean showAdBorder;
	private String adPlacement;

	private String language;

	private boolean published;

	private boolean lwjgl;
	private boolean signed;

	private String sourcePermissionsText;

	private String apkUrl;
	private String androidText;
	private boolean android;


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

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public MultipartFile getJarFile() {
		return jarFile;
	}

	public void setJarFile(MultipartFile jarFile) {
		this.jarFile = jarFile;
	}

	public MultipartFile getSourceFile() {
		return sourceFile;
	}

	public void setSourceZipUrl(MultipartFile sourceFile) {
		this.sourceFile = sourceFile;
	}

	public MultipartFile getFaviconFile() {
		return faviconFile;
	}

	public void setFaviconUrl(MultipartFile faviconFile) {
		this.faviconFile = faviconFile;
	}

	public MultipartFile getBackgroundFile() {
		return backgroundFile;
	}

	public void setBackgroundUrl(MultipartFile backgroundFile) {
		this.backgroundFile = backgroundFile;
	}

	public MultipartFile getThumbnailFile() {
		return thumbnailFile;
	}

	public void setThumbnailFile(MultipartFile thumbnailFile) {
		this.thumbnailFile = thumbnailFile;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public Integer getWebstartWidth() {
		return webstartWidth;
	}

	public void setWebstartWidth(Integer webstartWidth) {
		this.webstartWidth = webstartWidth;
	}

	public Integer getWebstartHeight() {
		return webstartHeight;
	}

	public void setWebstartHeight(Integer webstartHeight) {
		this.webstartHeight = webstartHeight;
	}

	public String getAppletClass() {
		return appletClass;
	}

	public void setAppletClass(String appletClass) {
		this.appletClass = appletClass;
	}

	public Integer getAppletWidth() {
		return appletWidth;
	}

	public void setAppletWidth(Integer appletWidth) {
		this.appletWidth = appletWidth;
	}

	public Integer getAppletHeight() {
		return appletHeight;
	}

	public void setAppletHeight(Integer appletHeight) {
		this.appletHeight = appletHeight;
	}

	public String getAppletDescription() {
		return appletDescription;
	}

	public void setAppletDescription(String appletDescription) {
		this.appletDescription = appletDescription;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getAdText() {
		return adText;
	}

	public void setAdText(String adText) {
		this.adText = adText;
	}

	public boolean isShowAdBorder() {
		return showAdBorder;
	}

	public void setShowAdBorder(boolean showAdBorder) {
		this.showAdBorder = showAdBorder;
	}

	public String getAdPlacement() {
		return adPlacement;
	}

	public void setAdPlacement(String adPlacement) {
		this.adPlacement = adPlacement;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public boolean isLwjgl() {
		return lwjgl;
	}

	public void setLwjgl(boolean lwjgl) {
		this.lwjgl = lwjgl;
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public String getSourcePermissionsText() {
		return sourcePermissionsText;
	}

	public void setSourcePermissionsText(String sourcePermissionsText) {
		this.sourcePermissionsText = sourcePermissionsText;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getAndroidText() {
		return androidText;
	}

	public void setAndroidText(String androidText) {
		this.androidText = androidText;
	}

	public boolean isAndroid() {
		return android;
	}

	public void setAndroid(boolean android) {
		this.android = android;
	}
}
