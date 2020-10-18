package pl.surveyormanagement.api.payload.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class VehiclePostRequest {
	@NotBlank
	@Size(max = 255)
	private String mark;
	
	@NotBlank
	@Size(max = 255)
	private String model;
	
	@NotBlank
	@Size(max = 8)
	private String licensePlateNumber;
	
	@NotNull
	private LocalDate serviceDate;
	
    @NotNull
    private long managerId;

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public LocalDate getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(LocalDate serviceDate) {
		this.serviceDate = serviceDate;
	}
	
	public long getManagerId() {
		return managerId;
	}

	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}
}
