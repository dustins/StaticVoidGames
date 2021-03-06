package com.StaticVoidGames.blog;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.StaticVoidGames.TimestampedEvent;
import com.StaticVoidGames.spring.util.PageDownUtils;

@Entity
@Table( name = "BlogEntries" )
public class BlogEntry implements TimestampedEvent{
	
	/**
	 * The id of the blog. This is automatically generated from the title and used in the url: StaticVoidGames.com/blog/urlName
	 */
	@Id
	String urlName;
	
	/**
	 * The member who wrote the blog.
	 */
	private String member;
	
	/**
	 * @deprecated This was used for game blogs. Currently not used. Maybe it will be in the future?
	 */
	private String game;
	
	/**
	 * The title of the blog.
	 */
	private String title;
	
	/**
	 * The text of the blog. This should be in markup.
	 */
	private String text;
	
	/**
	 * The time (in MS since epoch) that the blog was published.
	 */
	private long timestamp;

	/**
	 * Constructs a new blogEntry with the supplied parameters.
	 * Sets the timestamp to the current time.
	 */
	public BlogEntry(String urlName, String member, String game, String title, String text){
		this.urlName = urlName;
		this.member = member;
		this.game = game;
		this.title = title;
		this.text = text;
		
		timestamp = System.currentTimeMillis();
		
	}
	
	/**
	 * Empty constructor needed for Hibernate
	 */
	public BlogEntry(){}


	public String getMember() {
		return member;
	}

	public String getGame() {
		return game;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getUrlName() {
		return urlName;
	}

	
	/**
	 * Returns HTML by parsing this BlogEntry's markdown text.
	 */
	public String getParsedText(){
		return PageDownUtils.getSanitizedHtml(text);
	}

	/**
	 * Returns: MEMBER wrote a blog: TITLE
	 */
	@Override
	public String getEventText() {
		return member + " wrote a blog: " + title;
	}

	/**
	 * Returns: /blog/urlName
	 */
	@Override
	public String getRelativeUrl() {
		return "/blog/" + urlName;
	}
}

