package com.StaticVoidGames.spring.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.StaticVoidGames.games.Game;
import com.StaticVoidGames.games.GameForm;
import com.StaticVoidGames.spring.controller.interfaces.EditGameControllerInterface;
import com.StaticVoidGames.spring.dao.GameDao;
import com.StaticVoidGames.spring.util.AttributeNames;
import com.StaticVoidGames.spring.util.FormField;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

/**
 * Controller that handles the game edit pages.
 * Each form field is represented by a FormField Object, and each edit page url is mapped to a List of FormFields to display on that page.
 * The form field values are backed by a GameForm Object.
 * This is a bit convoluted, but it means that we can do all of the edit handling from a single page.
 */
@Component
public class EditGameController implements EditGameControllerInterface{


	@Autowired
	private Environment env;

	@Autowired
	private GameDao gameDao;

	/**
	 * Edit page URLs are mapped to Lists of FormFields to display on each page.
	 */
	Map<String, List<FormField>> formFields = new HashMap<String, List<FormField>>();

	/**
	 * Constructs a new EditGameControllr, creates the FormField instances and populates the formFieldsMap.
	 */
	public EditGameController(){

		FormField title = new FormField("Title", "Enter your game's title:", "title", "input");
		FormField shortDescription = new FormField("Short Description", "Enter a short description shown on thumbnails for your game", "shortDescription", "input");
		FormField description = new FormField("Description", "Enter a description for your game", "description", "textarea");
		FormField website = new FormField("Website", "If your game is hosted elsewhere, specify that here", "website", "input");
		
		FormField jarFile = new FormField("Jar File", "Upload a RUNNABLE jar. Applets and Webstarts are HIGHLY discouraged.", "jarFile", "file");
		
		FormField sourceFile = new FormField("Source File", "Upload a zip file containing your source. This is optional.", "source", "file");
		FormField sourcePermissions = new FormField("Source Permissions", "Specify a copyright to apply to your source", "sourcePermissionsText", "textarea");
	
		FormField faviconFile = new FormField("Favicon", "Upload an optional favicon. This is a 16x16 or 32x32 image that will show as the icon of your game's browser tab.", "faviconFile", "file");
		FormField backgroundFile = new FormField("Background", "Upload a background image. This is optional.", "backgroundFile", "file");
		FormField thumbnailFile = new FormField("Thumbnail", "Upload an image to be shown as the game's thumbnail.", "thumbnailFile", "file");

		FormField mainClass = new FormField("Main Class", "DEPRECATED: specify the main class to create a webstart version of your game.", "mainClass", "input");
		FormField webstartWidth = new FormField("Webstart Width", "How wide should the webstart version be?", "webstartWidth", "input");
		FormField webstartHeight = new FormField("Webstart Height", "How tall should the webstart version be?", "webstartHeight", "input");

		FormField appletClass = new FormField("Applet Class", "DEPRECATED: specify the applet class to create an applet version of your game.", "appletClass", "input");
		FormField appletWidth = new FormField("Applet Width", "How wide should the applet version be?", "appletWidth", "input");
		FormField appletHeight = new FormField("Applet Height", "How tall should the applet version be?", "applettHeight", "input");
		FormField appletDescription = new FormField("Applet Description", "Enter a short description (such as controls) shown below the applet:", "appletDescription", "textarea");

		FormField javaVersion = new FormField("Java Version", "What version of Java did you use to compile your game?", "javaVersion", "input");
		FormField rating = new FormField("Rating", "Is your game for all ages, teens, or adults only?", "rating", "input");

		FormField adText = new FormField("Ad Code", "You can specify an ad code here. BE COOL.", "adText", "textarea");
		FormField showAdBorder = new FormField("Ad Border", "Show ad border?", "showAdBorder", "checkbox");

		FormField language = new FormField("Language", "What language was your game written in?", "language", "input");
		
		FormField published = new FormField("Publish", "Do you want to publish your game? Unpublished games are hidden.", "published", "checkbox");

		FormField lwjgl = new FormField("LWJGL", "DEPRECATED: Was your game made with LWJGL?", "lwjgl", "checkbox");
		FormField signed = new FormField("signed", "DEPRECATED: Is your jar signed?", "signed", "checkbox");

		FormField apkFile = new FormField("Android APK", "Upload an Android APK", "apkFile", "file");
		FormField androidText = new FormField("Android Text", "What text should show under the Android tab? Include links to Google Play, etc.", "androidText", "textarea");
		FormField android = new FormField("android", "Include an Android tab?", "android", "checkbox");

		formFields.put("title", Arrays.asList(title));
		formFields.put("description", Arrays.asList(shortDescription, description, website));
		formFields.put("jar", Arrays.asList(language, javaVersion, jarFile));
		formFields.put("source", Arrays.asList(sourceFile, sourcePermissions));
		formFields.put("images", Arrays.asList(thumbnailFile, faviconFile, backgroundFile));
		formFields.put("webstart", Arrays.asList(mainClass, webstartWidth, webstartHeight));
		formFields.put("applet", Arrays.asList(appletClass, appletWidth, appletHeight, appletDescription));
		formFields.put("rating", Arrays.asList(rating));
		formFields.put("advertising", Arrays.asList(adText, showAdBorder));
		formFields.put("deprecated", Arrays.asList(lwjgl, signed));
		formFields.put("android", Arrays.asList(android, apkFile, androidText));
		formFields.put("publish", Arrays.asList(published));
	}

	/**
	 * Checks to make sure the member can edit the game and returns the index view.
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public String showIndex(@PathVariable("game") String game, HttpSession session, ModelMap model) {

		Game gameObj = gameDao.getGame(game);

		if(gameObj == null){
			return "redirect:/games/";
		}

		String loggedInMember = (String) session.getAttribute(AttributeNames.loggedInUser);
		
		if(!gameObj.getMember().equals(loggedInMember)){
			return "redirect:/games/"+game;
		}

		if(gameObj.getBackgroundUrl() != null){
			String s3Endpoint = env.getProperty("s3.endpoint");
			model.addAttribute("backgroundImage", s3Endpoint + "/games/" + game + "/" + gameObj.getBackgroundUrl());
		}

		session.setAttribute("gameObj", gameObj);

		return "editGame/index";
	}




	/**
	 * Handles the submission of a game edit page.
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public String post(@PathVariable("game") String game,  @ModelAttribute("gameForm") GameForm gameForm, HttpSession session, HttpServletRequest request) {

		Game gameObj = gameDao.getGame(game);

		if(gameObj == null){
			return "redirect:/games/";
		}

		String loggedInMember = (String) session.getAttribute(AttributeNames.loggedInUser);

		if(!gameObj.getMember().equals(loggedInMember)){
			return "redirect:/games/"+game;
		}


		//TODO: probably a smarter way to do this?
		gameObj.setTitle(gameForm.getTitle());
		gameObj.setGameDescription(gameForm.getDescription());
		gameObj.setShortDescription(gameForm.getShortDescription());
		gameObj.setWebsite(gameForm.getWebsite());
		gameObj.setMainClass(gameForm.getMainClass());
		gameObj.setWebstartWidth(gameForm.getWebstartWidth());
		gameObj.setWebstartHeight(gameForm.getWebstartHeight());
		gameObj.setAppletClass(gameForm.getAppletClass());
		gameObj.setAppletWidth(gameForm.getAppletWidth());
		gameObj.setAppletHeight(gameForm.getAppletHeight());
		gameObj.setAppletDescription(gameForm.getAppletDescription());
		gameObj.setJavaVersion(gameForm.getJavaVersion());
		gameObj.setRating(gameForm.getRating());
		gameObj.setLanguage(gameForm.getLanguage());
		gameObj.setAdText(gameForm.getAdText());
		gameObj.setShowAdBorder(gameForm.isShowAdBorder());
		gameObj.setPublished(gameForm.isPublished());
		gameObj.setLwjgl(gameForm.isLwjgl());
		gameObj.setSigned(gameForm.isSigned());
		gameObj.setSourcePermissionsText(gameForm.getSourcePermissionsText());
		gameObj.setApkUrl(gameForm.getApkUrl());
		gameObj.setAndroidText(gameForm.getAndroidText());
		gameObj.setAndroid(gameForm.isAndroid());

		if(gameForm.getJarFile() != null && !"".equals(gameForm.getJarFile())){

			String awsAccessKey = env.getProperty("aws.accessKey");
			String awsSecretKey = env.getProperty("aws.secretKey");
			String bucket = env.getProperty("s3.bucket");

			String key = "games/" + game + "/" + gameForm.getJarFile().getOriginalFilename();

			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(gameForm.getJarFile().getSize());

			try{
				AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
				s3.putObject(bucket, key, gameForm.getJarFile().getInputStream(), meta);
				s3.setObjectAcl(bucket, key, CannedAccessControlList.PublicRead);
				gameObj.setJarFileUrl(gameForm.getJarFile().getOriginalFilename());
			}
			catch(Exception e){
				//TODO: let the user know something went wrong
				e.printStackTrace();
			}
		}

		if(gameForm.getSourceFile() != null && !"".equals(gameForm.getSourceFile())){

			String awsAccessKey = env.getProperty("aws.accessKey");
			String awsSecretKey = env.getProperty("aws.secretKey");

			String bucket = env.getProperty("s3.bucket");
			String key = "games/" + game + "/" + gameForm.getSourceFile().getOriginalFilename();

			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(gameForm.getSourceFile().getSize());

			try{
				AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
				s3.putObject(bucket, key, gameForm.getSourceFile().getInputStream(), meta);
				s3.setObjectAcl(bucket, key, CannedAccessControlList.PublicRead);
				gameObj.setSourceZipUrl(gameForm.getSourceFile().getOriginalFilename());
			}
			catch(Exception e){
				//TODO: let the user know something went wrong
				e.printStackTrace();
			}
		}

		if(gameForm.getFaviconFile() != null && !"".equals(gameForm.getFaviconFile())){

			String awsAccessKey = env.getProperty("aws.accessKey");
			String awsSecretKey = env.getProperty("aws.secretKey");

			String bucket = env.getProperty("s3.bucket");
			String key = "games/" + game + "/" + gameForm.getFaviconFile().getOriginalFilename();

			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(gameForm.getFaviconFile().getSize());

			try{
				AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
				s3.putObject(bucket, key, gameForm.getFaviconFile().getInputStream(), meta);
				s3.setObjectAcl(bucket, key, CannedAccessControlList.PublicRead);
				gameObj.setFaviconUrl(gameForm.getFaviconFile().getOriginalFilename());
			}
			catch(Exception e){
				//TODO: let the user know something went wrong
				e.printStackTrace();
			}
		}

		if(gameForm.getBackgroundFile() != null && !"".equals(gameForm.getBackgroundFile())){

			String awsAccessKey = env.getProperty("aws.accessKey");
			String awsSecretKey = env.getProperty("aws.secretKey");
			String bucket = env.getProperty("s3.bucket");
			String key = "games/" + game + "/" + gameForm.getBackgroundFile().getOriginalFilename();

			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(gameForm.getBackgroundFile().getSize());

			try{
				AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
				s3.putObject(bucket, key, gameForm.getBackgroundFile().getInputStream(), meta);
				s3.setObjectAcl(bucket, key, CannedAccessControlList.PublicRead);
				gameObj.setBackgroundUrl(gameForm.getBackgroundFile().getOriginalFilename());
			}
			catch(Exception e){
				//TODO: let the user know something went wrong
				e.printStackTrace();
			}
		}


		if(gameForm.getThumbnailFile() != null && !"".equals(gameForm.getThumbnailFile())){

			String awsAccessKey = env.getProperty("aws.accessKey");
			String awsSecretKey = env.getProperty("aws.secretKey");
			String bucket = env.getProperty("s3.bucket");
			String key = "games/" + game + "/" + gameForm.getThumbnailFile().getOriginalFilename();
			
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(gameForm.getThumbnailFile().getSize());
			
			try{
				AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
				s3.putObject(bucket, key, gameForm.getThumbnailFile().getInputStream(), meta);
				s3.setObjectAcl(bucket, key, CannedAccessControlList.PublicRead);
				gameObj.setThumbnailUrl(gameForm.getThumbnailFile().getOriginalFilename());
			}
			catch(Exception e){
				//TODO: let the user know something went wrong
				e.printStackTrace();
			}
		}

		gameDao.updateGame(gameObj);
		return "redirect:/games/"+game +"/edit";
	}


	/**
	 * Returns the view for a particular game edit page.
	 */
	@Override
	@RequestMapping(value="/{section}", method=RequestMethod.GET)
	public String editSection(@PathVariable(value="game") String game, @PathVariable(value="section") String section, ModelMap model, HttpSession session, HttpServletRequest request){

		Game gameObj = gameDao.getGame(game);

		if(gameObj == null){
			return "redirect:/games/";
		}

		String loggedInMember = (String) request.getSession().getAttribute(AttributeNames.loggedInUser);

		if(!gameObj.getMember().equals(loggedInMember)){
			return "redirect:/games/"+game;
		}

		if(!formFields.containsKey(section)){
			return "redirect:/games/" + game + "/edit";
		}

		if(gameObj.getBackgroundUrl() != null){
			String s3Endpoint = env.getProperty("s3.endpoint");
			model.addAttribute("backgroundImage", s3Endpoint + "/games/" + game + "/" + gameObj.getBackgroundUrl());
		}

		model.addAttribute("gameForm", gameObj.getGameForm());
		model.addAttribute("formFields", formFields.get(section));
		model.addAttribute("gameObj", gameObj);

		return "editGame/editGame";
	}
}