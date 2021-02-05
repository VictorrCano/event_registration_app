package ca.mcgill.ecse321.eventregistration.model;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Paypal {
	
	private String id;
	private int amount;
	private String paymentMethod;
	
	@Id
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
	public String getEmail() {
		return this.id;
	}
	
	public void setEmail(String id) {
		this.id = id;
	}
	
	public String getPaymentMethod() {
		return this.paymentMethod;
	}
	
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}