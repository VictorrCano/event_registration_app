package ca.mcgill.ecse321.eventregistration.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.eventregistration.model.*;

public interface ConcertRepository extends CrudRepository<Concert, String>{
	
	Concert findByArtist(String artist);

}
