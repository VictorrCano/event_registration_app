package ca.mcgill.ecse321.eventregistration.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.eventregistration.model.*;

public interface PaypalRepository extends CrudRepository<Paypal, String>{
	
	//Paypal findPaypalByName(String name);
	//Paypal findPaypalByRegistration(Registration registration);

}
