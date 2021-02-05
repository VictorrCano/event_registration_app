package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;

@Entity
public class Registration {

	private int id;

	public void setId(int value) {
		this.id = value;
	}

	@Id
	public int getId() {
		return this.id;
	}

	private Person person;

	@ManyToOne(optional=true, cascade = CascadeType.ALL)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	private Event event;

	@ManyToOne(optional=true, cascade = CascadeType.ALL)
	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	public Paypal payment;
	
	//adding Paypal methods
	@OneToOne
	public Paypal getPaypal() {
		return this.payment;
	}
	
	public void setPaypal(Paypal payment) {
		this.payment = payment;
	}

}
