package es.upm.miw.dtos;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

import es.upm.miw.documents.Article;

public class ArticleSearchOutputDto {
	@Id
	private String code;

	private String description;

	private Integer stock;

	private BigDecimal retailPrice;

	public ArticleSearchOutputDto() {
	}

	public ArticleSearchOutputDto(String code, String description, Integer stock, BigDecimal retailPrice) {
		this.code = code;
		this.description = description;
		this.stock = stock;
		this.retailPrice = retailPrice;
	}

	public ArticleSearchOutputDto(Article article) {
		this.code = article.getCode();
		this.description = article.getDescription();
		this.stock = article.getStock();
		this.retailPrice = article.getRetailPrice();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	@Override
	public String toString() {
		return "ArticleSearchOutputDto{" + "code='" + code + '\'' + ", description='" + description + '\'' + ", stock="
				+ stock + ", retailPrice=" + retailPrice + '}';
	}
}