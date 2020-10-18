package pl.surveyormanagement.api.payload.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrderUpdateRequest {
	@NotBlank
    private String type;

	@NotBlank
    @Size(max = 255)
    private String customer;
    
    @NotNull
    private LocalDate endDate;
    
    @NotBlank
    private String status;
    
    public String getType() {
 		return type;
 	}

 	public void setType(String type) {
 		this.type = type;
 	}

 	public String getCustomer() {
 		return customer;
 	}

 	public void setCustomer(String customer) {
 		this.customer = customer;
 	}

 	public LocalDate getEndDate() {
 		return endDate;
 	}

 	public void setEndDate(LocalDate endDate) {
 		this.endDate = endDate;
 	}

 	public String getStatus() {
 		return status;
 	}

 	public void setStatus(String status) {
 		this.status = status;
 	}
}
