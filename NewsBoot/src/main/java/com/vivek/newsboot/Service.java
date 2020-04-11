package com.vivek.newsboot;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.github.ccincharge.newsapi.NewsApi;
import io.github.ccincharge.newsapi.datamodels.Source;
import io.github.ccincharge.newsapi.requests.RequestBuilder;
import io.github.ccincharge.newsapi.responses.ApiArticlesResponse;
import io.github.ccincharge.newsapi.responses.ApiSourcesResponse;

@Component
public class Service {


	String processRequest(HttpServletRequest request) {

		NewsApi news=new NewsApi(request.getParameter("apiKey"));

		RequestBuilder builder=new RequestBuilder();
		populateBuilder(builder,request);
		
		ApiArticlesResponse aar;
		String[] uris=request.getRequestURI().split("/");
		
		if(uris.length==0)return "BAD URL:: must be of form /v2/";
		
		if( uris[2].equalsIgnoreCase(String.valueOf("everything")))
			aar=news.sendEverythingRequest(builder);
		
		else if(uris[2].equalsIgnoreCase(String.valueOf("top-headlines")))
			aar=news.sendTopRequest(builder);
		
		else if(uris[2].equalsIgnoreCase(String.valueOf("sources")))
			return processSource(builder,news,request.getParameter("k"));
		
		else return "BAD URL. Must be /v2/{either everything or sources or top-headlines}";
		
		return aar.rawJSON();
	}
	
	//returns only source names... if k specified will give first k source-names.
	private String processSource(RequestBuilder builder, NewsApi news, String k) {
		ApiSourcesResponse asr=news.sendSourcesRequest(builder);
		if(k!=null) {
			int num=Integer.valueOf(k);
			if(num==0)return "";
			if(num<asr.sources().size())
				asr.sources().subList(num, asr.sources().size()).clear();
		}

		ArrayList<String> nameList=new ArrayList<String>();
		Iterator<Source> itr=asr.sources().iterator();

		while(itr.hasNext()) {
			nameList.add(itr.next().name());
		}

		String st=new Gson().toJson(nameList);
		return st;
	}
	
	private void populateBuilder(RequestBuilder builder,HttpServletRequest request){
		
		if(request.getParameter("sources")!=null) builder.setSources(request.getParameter("sources"));
		if(request.getParameter("q")!=null) builder.setQ(request.getParameter("q"));
	    if(request.getParameter("category")!=null) builder.setCategory(request.getParameter("category"));
	    if(request.getParameter("language")!=null) builder.setLanguage(request.getParameter("language"));
	    if(request.getParameter("country")!=null) builder.setCountry(request.getParameter("country"));
	    if(request.getParameter("domains")!=null) builder.setDomains(request.getParameter("domains"));
	    if(request.getParameter("from")!=null) builder.setFrom(request.getParameter("from"));
	    if(request.getParameter("to")!=null) builder.setTo(request.getParameter("to"));
	    if(request.getParameter("sortBy")!=null) builder.setSortBy(request.getParameter("sortBy"));
	    if(request.getParameter("page")!=null) builder.setPage(Integer.valueOf(request.getParameter("page")));
	}


	
}
