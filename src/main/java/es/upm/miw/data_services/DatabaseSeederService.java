package es.upm.miw.data_services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import es.upm.miw.business_services.Barcode;
import es.upm.miw.documents.Article;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.repositories.ArticleRepository;

@Service
public class DatabaseSeederService {

	private static final String VARIOUS_CODE = "1";

	private static final String VARIOUS_NAME = "Varios";

	private static final String NO_PROVIDER = "";

	private static final String PREFIX_CODE_ARTICLE = "8400000";

	private static final Long FIRST_CODE_ARTICLE = 840000000000L;

	private static final Long LAST_CODE_ARTICLE = 840000099999L;

	@Autowired
	private Environment environment;

	@Value("${miw.admin.mobile}")
	private String mobile;
	@Value("${miw.admin.username}")
	private String username;
	@Value("${miw.admin.password}")
	private String password;

	@Value("${miw.databaseSeeder.ymlFileName:#{null}}")
	private String ymlFileName;

	@Autowired
	private ArticleRepository articleRepository;

	@PostConstruct
	public void constructor() {
		String[] profiles = this.environment.getActiveProfiles();
		if (Arrays.stream(profiles).anyMatch("dev"::equals)) {
			this.deleteAllAndInitializeAndLoadYml();
		} else if (Arrays.stream(profiles).anyMatch("prod"::equals)) {
			this.initialize();
		}
	}

	private void initialize() {
		if (!this.articleRepository.existsById(VARIOUS_CODE)) {
			LogManager.getLogger(this.getClass()).warn("------- Create Article Various -----------");
			this.articleRepository.save(Article.builder(VARIOUS_CODE).reference(VARIOUS_NAME).description(VARIOUS_NAME)
					.retailPrice("100.00").stock(1000).provider(NO_PROVIDER).build());
		}
	}

	public void deleteAllAndInitialize() {
		LogManager.getLogger(this.getClass()).warn("------- Delete All -----------");
		// Delete Repositories -----------------------------------------------------
		this.articleRepository.deleteAll();
		// -------------------------------------------------------------------------
		this.initialize();
	}

	public void deleteAllAndInitializeAndLoadYml() {
		this.deleteAllAndInitialize();
		this.seedDatabase();
		this.initialize();
	}

	public void seedDatabase() {
		if (this.ymlFileName != null) {
			try {
				LogManager.getLogger(this.getClass()).warn("------- Initial Load: " + this.ymlFileName + "-----------");
				this.seedDatabase(new ClassPathResource(this.ymlFileName).getInputStream());
			} catch (IOException e) {
				LogManager.getLogger(this.getClass())
						.error("File " + this.ymlFileName + " doesn't exist or can't be opened");
			}
		} else {
			LogManager.getLogger(this.getClass()).error("File db.yml doesn't configured");
		}
	}

	public void seedDatabase(InputStream input) {
		Yaml yamlParser = new Yaml(new Constructor(DatabaseGraph.class));
		DatabaseGraph tpvGraph = yamlParser.load(input);

		// Save Repositories -----------------------------------------------------
		this.articleRepository.saveAll(tpvGraph.getArticleList());
		// -----------------------------------------------------------------------

		LogManager.getLogger(this.getClass()).warn("------- Seed...   " + "-----------");
	}

	public String nextCodeEan() {

		Article article = this.articleRepository.findFirstByCodeStartingWithOrderByCodeDesc(PREFIX_CODE_ARTICLE);

		Long nextCodeWithoutRedundancy = FIRST_CODE_ARTICLE;

		if (article != null) {
			String code = article.getCode();
			String codeWithoutRedundancy = code.substring(0, code.length() - 1);

			nextCodeWithoutRedundancy = Long.parseLong(codeWithoutRedundancy) + 1L;
		}

		if (nextCodeWithoutRedundancy > LAST_CODE_ARTICLE) {
			throw new ConflictException("There is not next code EAN");
		}

		return new Barcode().generateEan13code(nextCodeWithoutRedundancy);
	}
}
