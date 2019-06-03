package es.upm.miw.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import es.upm.miw.business_services.JwtService;
import es.upm.miw.documents.Role;
import es.upm.miw.dtos.TokenOutputDto;
import es.upm.miw.exceptions.JwtException;

@Service
public class RestService {

	@Autowired
	private Environment environment;

	@Autowired
	private JwtService jwtService;

	@Value("${server.servlet.contextPath}")
	private String contextPath;

	@Value("${miw.admin.mobile}")
	private String adminMobile;

	@Value("${miw.admin.password}")
	private String adminPassword;

	@Value("${user.microservice}")
	private String userMicroservice;

	private int port = 0;

	private TokenOutputDto tokenDto;

	private int getPort() {
		if (this.port == 0) {
			this.port = Integer.parseInt(environment.getProperty("local.server.port"));
		}
		return this.port;
	}

	private boolean isRole(Role role) {
		try {
			return this.tokenDto != null
					&& this.jwtService.roles("Bearer " + tokenDto.getToken()).contains(role.name());
		} catch (JwtException e) {
			e.printStackTrace();
		}
		return false;
	}

	public <T> RestBuilder<T> restBuilder(RestBuilder<T> restBuilder) {
		restBuilder.port(this.getPort());
		restBuilder.path(contextPath);
		if (tokenDto != null) {
			restBuilder.bearerAuth(tokenDto.getToken());
		}
		return restBuilder;
	}

	public RestBuilder<Object> restBuilder() {
		RestBuilder<Object> restBuilder = new RestBuilder<>(this.port);
		restBuilder.path(contextPath);
		if (tokenDto != null) {
			restBuilder.bearerAuth(tokenDto.getToken());
		}
		return restBuilder;
	}

	public RestService loginAdmin() {
		if (!this.isRole(Role.ADMIN)) {
			this.tokenDto = new RestBuilder<TokenOutputDto>(userMicroservice).clazz(TokenOutputDto.class)
					.basicAuth(this.adminMobile, this.adminPassword).heroku().path(contextPath).path("/users/token")
					.post().log().build();
		}
		return this;
	}

	public RestService logout() {
		this.tokenDto = null;
		return this;
	}

	public TokenOutputDto getTokenDto() {
		return tokenDto;
	}

	public String getAdminMobile() {
		return adminMobile;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

}
