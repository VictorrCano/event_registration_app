package ca.mcgill.ecse321.eventregistration.dto;

public class PaypalDto {
	
	private String id;
	private int amount;
	private String paymentMethod;
	
	public PaypalDto() {}
	

	public PaypalDto(String id, int amount) {
		this.id = id;
		this.amount = amount;
		this.paymentMethod = "Paypal";
	}
	
	
	
	public void setId(String id) {
		this.id = id;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setType(String type) {
		this.paymentMethod = type;
	}
	
	
	
	public int getAmount() {
		return amount;
	}
	public String getType() {
		return paymentMethod;
	}
	public String getId() {
		return id;
	}
	
}
