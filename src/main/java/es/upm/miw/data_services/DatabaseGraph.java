package es.upm.miw.data_services;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Provider;

public class DatabaseGraph {

	private List<Article> articleList;
	private List<Provider> providerList;

	public DatabaseGraph() {
		this.articleList = new ArrayList<>();
		this.providerList = new ArrayList<>();
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public List<Provider> getProviderList() {
		return providerList;
	}

	public void setProviderList(List<Provider> providerList) {
		this.providerList = providerList;
	}
}
