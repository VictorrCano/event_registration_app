package ca.mcgill.ecse321.eventregistration.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.eventregistration.model.*;

public interface OrganizerRepository extends CrudRepository<Organizer, String>{
	
	Organizer findByName(String name);
	//Organizer findByRegistration(Registration registration); maybe?
	//List<Organizer> findOrganizerByEvent(Event event); maybe?

}
