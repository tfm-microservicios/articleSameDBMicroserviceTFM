package es.upm.miw.dtos;

import javax.validation.constraints.NotNull;

public class ProviderMinimunDto {

	private String id;

	@NotNull
	private String company;

	private String nif;

	public ProviderMinimunDto() {
    }

	protected ProviderMinimunDto(String id, String company, String nif) {
        this.id = id;
        this.company = company;
        this.nif = nif;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	@Override
	public String toString() {
		return "ProviderMinimunDto[" + "id='" + id + '\'' + ", company='" + company + '\'' + ", nif='" + nif + '\''
				+ ']';
	}
}
