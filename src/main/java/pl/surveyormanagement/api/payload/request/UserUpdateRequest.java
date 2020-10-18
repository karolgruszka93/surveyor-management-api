package pl.surveyormanagement.api.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserUpdateRequest {
	@NotBlank
	@Pattern(regexp="^(([^<>()\\\\.,;:\\s@\"]+(\\.[^<>()\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")
    @Size(max = 320)
    private String email;
 
    @NotBlank
    @Pattern(regexp="^[a-zA-Zą-żĄ-Ż ]+$")
    @Size(max = 25)
    private String firstName;
    
    @NotBlank
    @Pattern(regexp="^[a-zA-Zą-żĄ-Ż ]+$")
    @Size(max = 25)
    private String lastName;
    
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
