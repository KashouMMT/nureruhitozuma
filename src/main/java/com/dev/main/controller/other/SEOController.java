package com.dev.main.controller.other;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.main.dto.SitemapUrl;
import com.dev.main.model.Product;
import com.dev.main.service.ProductService;

@RestController
public class SEOController {

    private static final String BASE_URL = "https://nureruhitozuma.com";

    private final ProductService productService;

    public SEOController(ProductService productService) {
		super();
		this.productService = productService;
	}

	@GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> siteMapUrl() {
        List<SitemapUrl> urls = getSiteUrls();

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        for (SitemapUrl u : urls) {
            sb.append("  <url>\n");
            sb.append("    <loc>").append(u.getLoc()).append("</loc>\n");
            if(u.getLastmod() != null) sb.append("    <lastmod>").append(u.getLastmod()).append("</lastmod>\n");
            sb.append("    <changefreq>").append(u.getChangefreq()).append("</changefreq>\n");
            sb.append("    <priority>").append(u.getPriority()).append("</priority>\n");
            sb.append("  </url>\n");
        }

        sb.append("</urlset>");
        String body = sb.toString();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_ATOM_XML).body(body);
    }

    private List<SitemapUrl> getSiteUrls() {
        List<SitemapUrl> urls = new ArrayList<>();

//        urls.add(new SitemapUrl(BASE_URL + "/en/home", "daily", "1.0"));
//        urls.add(new SitemapUrl(BASE_URL + "/en/home/cast", "daily", "0.9"));
//        urls.add(new SitemapUrl(BASE_URL + "/en/home/schedule","daily","0.8"));
//        urls.add(new SitemapUrl(BASE_URL + "/en/home/reservation","weekly","0.7"));
//        urls.add(new SitemapUrl(BASE_URL + "/en/home/guide", "weekly", "0.6"));
//        urls.add(new SitemapUrl(BASE_URL + "/en/home/system","yearly","0.6"));
//        urls.add(new SitemapUrl(BASE_URL + "/en/home/access", "weekly", "0.5"));
//        urls.add(new SitemapUrl(BASE_URL + "/en/home/recruit","yearly","0.5"));
//        urls.add(new SitemapUrl(BASE_URL + "/en/home/privacy", "yearly", "0.4"));
//        urls.add(new SitemapUrl(BASE_URL + "/en/home/terms","yearly","0.3"));
        
        urls.add(new SitemapUrl(BASE_URL + "/ja/home", "daily", "1.0"));
        urls.add(new SitemapUrl(BASE_URL + "/ja/home/cast", "daily", "0.9"));
        urls.add(new SitemapUrl(BASE_URL + "/ja/home/schedule","daily","0.8"));
        urls.add(new SitemapUrl(BASE_URL + "/ja/home/newcomer","weekly","0.7"));
        urls.add(new SitemapUrl(BASE_URL + "/ja/home/ranking", "weekly", "0.6"));
        urls.add(new SitemapUrl(BASE_URL + "/ja/home/system","yearly","0.6"));
        
//        urls.add(new SitemapUrl(BASE_URL + "/ko/home", "daily", "1.0"));
//        urls.add(new SitemapUrl(BASE_URL + "/ko/home/cast", "daily", "0.9"));
//        urls.add(new SitemapUrl(BASE_URL + "/ko/home/blog", "weekly", "0.8"));
//        urls.add(new SitemapUrl(BASE_URL + "/ko/home/newcomers", "daily", "0.9"));
//        urls.add(new SitemapUrl(BASE_URL + "/ko/home/about", "yearly", "0.5"));
//        urls.add(new SitemapUrl(BASE_URL + "/ko/home/news","weekly","0.6"));
//        
//        urls.add(new SitemapUrl(BASE_URL + "/zh/home", "daily", "1.0"));
//        urls.add(new SitemapUrl(BASE_URL + "/zh/home/cast", "daily", "0.9"));
//        urls.add(new SitemapUrl(BASE_URL + "/zh/home/blog", "weekly", "0.8"));
//        urls.add(new SitemapUrl(BASE_URL + "/zh/home/newcomers", "daily", "0.9"));
//        urls.add(new SitemapUrl(BASE_URL + "/zh/home/about", "yearly", "0.5"));
//        urls.add(new SitemapUrl(BASE_URL + "/zh/home/news","weekly","0.6"));

        for (Product product : productService.getAllProducts()) {
            String lastmod = null;
            if(product.getUpdatedAt() != null) lastmod = product.getUpdatedAt().toLocalDate().toString();
            else lastmod = product.getCreatedAt().toLocalDate().toString();
            
//            String loc = BASE_URL + "/en/home/cast/" + product.getId();
//            urls.add(new SitemapUrl(loc, "daily", "0.7",lastmod));
            
            String loc = BASE_URL + "/ja/home/cast/" + product.getId();
            urls.add(new SitemapUrl(loc, "daily", "0.7",lastmod));

//          loc = BASE_URL + "/ko/home/cast" + product.getId();
//          urls.add(new SitemapUrl(loc,"daily", "0.7",lastmod));
//          loc = BASE_URL + "/zh/home/cast" + product.getId();
//          urls.add(new SitemapUrl(loc,"daily", "0.7",lastmod));
        }
        
//        for(Blog blog : blogService.getAllBlogs()) {
//        	String lastmod = null;
//        	if(blog.getUpdatedAt() != null) lastmod = blog.getUpdatedAt().toLocalDate().toString();
//        	else lastmod = blog.getCreatedAt().toLocalDate().toString();
//        	String loc = BASE_URL + "/en/home/blog/" + blog.getId();
//        	urls.add(new SitemapUrl(loc, "weekly", "0.8",lastmod));
//        	loc = BASE_URL + "/ja/home/blog/" + blog.getId();
//        	urls.add(new SitemapUrl(loc, "weekly", "0.8",lastmod));
//        	loc = BASE_URL + "/ko/home/blog/" + blog.getId();
//        	urls.add(new SitemapUrl(loc, "weekly", "0.8",lastmod));
//        	loc = BASE_URL + "/zh/home/blog/" + blog.getId();
//        	urls.add(new SitemapUrl(loc, "weekly", "0.8",lastmod));
//        }

        return urls;
    }
}