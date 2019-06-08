package es.upm.miw.business_controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.ArticleSearchOutputDto;
import es.upm.miw.dtos.ProviderDto;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.rest_controllers.RestBuilder;
import es.upm.miw.rest_controllers.RestService;

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

	public ArticleDto createArticle(ArticleDto articleDto) {
		String code = articleDto.getCode();
		if (code == null) {
			code = this.databaseSeederService.nextCodeEan();
		}
		if (this.articleRepository.findById(code).isPresent()) {
			throw new ConflictException("Article code (" + code + ")");
		}
		int stock = (articleDto.getStock() == null) ? 10 : articleDto.getStock();
		Provider provider = null;
		if (articleDto.getProvider() != null) {
			provider = findProviderById(articleDto.getProvider());
		}

		Article article = Article.builder(code).description(articleDto.getDescription())
				.retailPrice(articleDto.getRetailPrice()).reference(articleDto.getReference()).stock(stock)
				.provider(provider).build();
		this.articleRepository.save(article);
		return new ArticleDto(article);
	}

	private Provider findProviderById(String providerId) {
		Provider provider = null;

		try {
			ProviderDto providerDto = this.restService.loginAdmin()
					.restBuilder(new RestBuilder<ProviderDto>(providerMicroservice)).heroku().clazz(ProviderDto.class)
					.path("/providers/" + providerId).get().build();

			provider = new Provider();
			provider.setNif(providerDto.getNif());
			provider.setCompany(providerDto.getCompany());
			provider.setId(providerDto.getId());

		} catch (Exception e) {
			throw new NotFoundException("Product id (" + providerId + ") does not exist");
		}
		return provider;
	}

	public ArticleDto update(String code, ArticleDto articleDto) {
		ArticleDto articleBBDD = readArticle(code);

		Article article = prepareArticle(articleDto, articleBBDD.getCode());

		this.articleRepository.save(article);
		return new ArticleDto(article);
	}

	private Article prepareArticle(ArticleDto articleDto, String code) {

		int stock = (articleDto.getStock() == null) ? 10 : articleDto.getStock();
		Provider provider = null;
		if (articleDto.getProvider() != null) {
			provider = findProviderById(articleDto.getProvider());
		}

		return Article.builder(code).description(articleDto.getDescription()).retailPrice(articleDto.getRetailPrice())
				.reference(articleDto.getReference()).stock(stock).provider(provider)
				.discontinued(articleDto.getDiscontinued()).build();
	}

	public void delete(String code) {
		if (this.articleRepository.findById(code).isPresent()) {
			this.articleRepository.deleteById(code);
		}
	}

}
