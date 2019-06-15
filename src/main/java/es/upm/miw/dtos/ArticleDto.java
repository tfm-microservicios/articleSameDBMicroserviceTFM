package es.upm.miw.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.upm.miw.documents.Article;
import es.upm.miw.dtos.validations.BigDecimalPositive;

public class ArticleDto extends ArticleMinimumDto {

	@JsonInclude(Include.NON_NULL)
	private String reference;

	@BigDecimalPositive
	@JsonInclude(Include.NON_NULL)
	private BigDecimal retailPrice;

	private Integer stock;

	@JsonInclude(Include.NON_NULL)
	private String providerId;

	@JsonInclude(Include.NON_NULL)
	private Boolean discontinued;

	@JsonInclude(Include.NON_NULL)
	private LocalDateTime registrationDate;

	public ArticleDto() {
		// Empty for framework
	}

	public ArticleDto(String code, String description, String reference, BigDecimal retailPrice, Integer stock) {
		super(code, description);
		this.reference = reference;
		this.retailPrice = retailPrice;
		this.stock = stock;
	}

	public ArticleDto(Article article) {
		this(article.getCode(), article.getDescription(), article.getReference(), article.getRetailPrice(),
				article.getStock());
		this.setDiscontinued(article.getDiscontinued());
		this.registrationDate = article.getRegistrationDate();
		if (article.getProviderId() != null) {
			this.setProviderId(article.getProviderId());
		}
	}

	public String getReference() {
		return reference;
	}

	public ArticleDto setReference(String reference) {
		this.reference = reference;
		return this;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public ArticleDto setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
		return this;
	}

	public Integer getStock() {
		return stock;
	}

	public ArticleDto setStock(Integer stock) {
		this.stock = stock;
		return this;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public Boolean getDiscontinued() {
		return discontinued;
	}

	public ArticleDto setDiscontinued(Boolean discontinued) {
		this.discontinued = discontinued;
		return this;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	@Override
	public String toString() {
		return "ArticleDto{" + "reference='" + reference + '\'' + ", retailPrice=" + retailPrice + ", stock=" + stock
				+ ", providerId='" + providerId + '\'' + ", discontinued=" + discontinued + ", registrationDate="
				+ registrationDate + '}';
	}
}
