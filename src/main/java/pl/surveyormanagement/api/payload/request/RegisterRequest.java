package pl.surveyormanagement.api.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

public class RegisterRequest {
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
    
    @NotBlank
    private String password;
    
    private Set<String> role;
    
    private long managerId;
    
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

	public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Set<String> getRole() {
      return this.role;
    }
    
    public void setRole(Set<String> role) {
      this.role = role;
    }

	public long getManagerId() {
		return managerId;
	}

	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}
}