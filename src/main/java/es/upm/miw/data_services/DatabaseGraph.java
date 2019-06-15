package es.upm.miw.data_services;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.documents.Article;

public class DatabaseGraph {

	private List<Article> articleList;

	public DatabaseGraph() {
		this.articleList = new ArrayList<>();
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}
}
