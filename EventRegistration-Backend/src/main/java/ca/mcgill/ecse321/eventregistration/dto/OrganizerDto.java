package ca.mcgill.ecse321.eventregistration.dto;
import java.util.Collections;
import java.util.List;


public class OrganizerDto {
	
	private String name;
	private List<EventDto> organizedEvents;
	
	public OrganizerDto() {}
	
	@SuppressWarnings("unchecked")
	public OrganizerDto(String name) {
		this(name, Collections.EMPTY_LIST);
	}
	
	public OrganizerDto(String name, List<EventDto> organizedEvents) {
		this.name = name;
		this.organizedEvents = organizedEvents;
	}
	
	public String getName() {
		return name;
	}
	
	public List<EventDto> getOrganizeddEvents(){
		return organizedEvents;
	}
	
	public void setOrganizedEvents(List<EventDto> organizedEvents) {
		this.organizedEvents = organizedEvents;
	}

}
