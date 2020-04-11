package com.vivek.newsboot;


import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@ComponentScan({"com.vivek.newsboot"})
public class Fetch {

	@Autowired
	Service ser;
	
	
	
	@RequestMapping({"/v2/everything","/v2/sources","/v2/top-headlines"})
	public String getNews(HttpServletRequest request,HttpServletResponse response) {
		//System.out.println("Inside getNews:: "+request+"-->"+request.getRequestURI());
		return ser.processRequest(request);
	}
	
		
	public static void main(String... arg)throws IOException {
		System.out.println("Starting Spring..");
		SpringApplication.run(Fetch.class, arg);
		System.out.println("Started!");
		/*String key="";
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Enter API Key");
		key=br.readLine();
		
		NewsApi news=new NewsApi(key);
		
		RequestBuilder builder=new RequestBuilder().setQ("corona");
		
		ApiArticlesResponse aar;
	    aar=news.sendTopRequest(builder);
	    
		System.out.println(aar.totalResults());
		*/
	}
	
	
}
