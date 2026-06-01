package com.dev.main.dto;

public class SitemapUrl {
    private final String loc;
    private final String changefreq;
    private final String priority;
    private final String lastmod;

    public SitemapUrl(String loc, String changefreq, String priority, String lastmod) {
		super();
		this.loc = loc;
		this.changefreq = changefreq;
		this.priority = priority;
		this.lastmod = lastmod;
	}
    
    public SitemapUrl(String loc, String changefreq, String priority) {
		super();
		this.loc = loc;
		this.changefreq = changefreq;
		this.priority = priority;
		this.lastmod = null;
	}
    
	public String getLoc() { return loc; }
    public String getChangefreq() { return changefreq; }
    public String getPriority() { return priority; }
	public String getLastmod() { return lastmod; }
    
}