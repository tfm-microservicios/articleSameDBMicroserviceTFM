package es.upm.miw.rest_controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.upm.miw.business_controllers.ArticleController;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.ArticleSearchOutputDto;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(ArticleResource.ARTICLES)
public class ArticleResource {

	public static final String ARTICLES = "/articles";
	public static final String CODE_ID = "/{code}";
	public static final String MINIMUM = "/minimum";
	public static final String SEARCH = "/search";
	public static final String VALIDATE = "/validate";

	@Autowired
	private ArticleController articleController;

	@GetMapping
	public List<ArticleSearchOutputDto> readAll() {
		return this.articleController.readAll();
	}

	@GetMapping(value = CODE_ID)
	public ArticleDto readArticle(@PathVariable String code) {
		return this.articleController.readArticle(code);
	}
	
	@GetMapping(value = CODE_ID + VALIDATE)
	public boolean isPresent(@PathVariable String code) {
		return this.articleController.isPresent(code);
	}

	@GetMapping(value = MINIMUM)
	public List<ArticleMinimumDto> readArticlesMinimum() {
		return this.articleController.readArticlesMinimum();
	}

	@PostMapping
	public ArticleDto createArticle(@Valid @RequestBody ArticleDto articleDto,
			@RequestHeader("Authorization") String token) {
		return this.articleController.createArticle(articleDto, token);
	}

	@PutMapping(value = CODE_ID)
	public ArticleDto update(@PathVariable String code, @Valid @RequestBody ArticleDto articleDto,
			@RequestHeader("Authorization") String token) {
		return this.articleController.update(code, articleDto, token);
	}

	@DeleteMapping(value = CODE_ID)
	public void delete(@PathVariable String code) {
		this.articleController.delete(code);
	}

}