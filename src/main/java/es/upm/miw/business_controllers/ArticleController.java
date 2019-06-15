package es.upm.miw.business_controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import es.upm.miw.business_services.RestBuilder;
import es.upm.miw.business_services.RestService;
import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.documents.Article;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.ArticleSearchOutputDto;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;

@Controller
public class ArticleController {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private DatabaseSeederService databaseSeederService;

	@Autowired
	private RestService restService;

	@Value("${provider.microservice}")
	private String providerMicroservice;

	public List<ArticleSearchOutputDto> readAll() {
		return this.articleRepository.findAll().stream().map(ArticleSearchOutputDto::new).collect(Collectors.toList());
	}

	public List<ArticleMinimumDto> readArticlesMinimum() {
		List<Article> articles = articleRepository.findAll();
		List<ArticleMinimumDto> dtos = new ArrayList<>();
		for (Article article : articles) {
			dtos.add(new ArticleMinimumDto(article));
		}
		return dtos;
	}

	public ArticleDto readArticle(String code) {
		return new ArticleDto(this.articleRepository.findById(code)
				.orElseThrow(() -> new NotFoundException("Article code (" + code + ")")));
	}

	public ArticleDto createArticle(ArticleDto articleDto, String token) {
		String code = articleDto.getCode();
		if (code == null || code == "") {
			code = this.databaseSeederService.nextCodeEan();
		}
		if (this.articleRepository.findById(code).isPresent()) {
			throw new ConflictException("Article code (" + code + ")");
		}

		Article article = prepareArticle(articleDto, code, token);

		this.articleRepository.save(article);
		return new ArticleDto(article);
	}

	private boolean checkProviderId(String providerId, String token) {

		if (providerId == null || providerId == "") {
			return false;
		}

		try {
			this.restService.setToken(token).restBuilder(new RestBuilder<Boolean>(providerMicroservice)).heroku()
					.clazz(Boolean.class).path("/providers/" + providerId + "/validate").get().build();
		} catch (Exception e) {
			throw new NotFoundException("Product id (" + providerId + ") does not exist");
		}
		return true;
	}

	public ArticleDto update(String code, ArticleDto articleDto, String token) {
		ArticleDto articleBBDD = readArticle(code);

		Article article = prepareArticle(articleDto, articleBBDD.getCode(), token);

		this.articleRepository.save(article);
		return new ArticleDto(article);
	}

	private Article prepareArticle(ArticleDto articleDto, String code, String token) {

		int stock = (articleDto.getStock() == null) ? 10 : articleDto.getStock();
		String providerId = articleDto.getProviderId();

		checkProviderId(providerId, token);

		return Article.builder(code).description(articleDto.getDescription()).retailPrice(articleDto.getRetailPrice())
				.reference(articleDto.getReference()).stock(stock).provider(providerId).build();
	}

	public void delete(String code) {
		if (this.articleRepository.findById(code).isPresent()) {
			this.articleRepository.deleteById(code);
		}
	}

}
