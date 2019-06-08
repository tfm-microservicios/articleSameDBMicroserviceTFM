package es.upm.miw.dtos;

import es.upm.miw.documents.Provider;

public class ProviderDto extends ProviderMinimunDto {

	private String address;

    private String phone;

    private String email;

    private String note;

    private Boolean active;

    public ProviderDto() {
    }

    public ProviderDto(Provider provider) {
        super(provider.getId(), provider.getCompany(), provider.getNif());
        this.address = provider.getAddress();
        this.phone = provider.getPhone();
        this.email = provider.getEmail();
        this.note = provider.getNote();
        this.active = provider.isActive();
    }

    public ProviderDto(String company) {
        this.setCompany(company);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "ProviderDto[" +
                "id='" + this.getId() + '\'' +
                ", company='" + this.getCompany() + '\'' +
                ", nif='" + this.getNif() + '\'' +
                "address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", note='" + note + '\'' +
                ", active=" + active +
                ']';
    }
}
