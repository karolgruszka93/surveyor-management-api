package pl.surveyormanagement.api.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EquipmentUpdateRequest {
	@NotBlank
	@Size(max = 255)
	private String name;

	@NotBlank
	@Size(max = 255)
	private String model;

	@NotBlank
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
