package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.dto.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;

	// POST Mappings

	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}
	@PostMapping(value = { "/organizers/{name}", "/organizers/{name}/" })
	public OrganizerDto createOrganizer(@PathVariable("name") String name) throws IllegalArgumentException {
		Organizer organizer = service.createOrganizer(name);
		return convertToDto(organizer);
	}

	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, 
			@RequestParam(name = "date") Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws IllegalArgumentException {
		// @formatter:on
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}
	@PostMapping(value = { "/concerts/{name}/{artist}", "/concerts/{name}/{artist}/" })
	public ConcertDto createConcert(@PathVariable("name") String name, 
			@PathVariable("artist") String artist, 
			@RequestParam(name = "date") Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws IllegalArgumentException {
		Concert concert = service.createConcert(name, date, Time.valueOf(startTime), Time.valueOf(endTime), artist);
		return convertToDto(concert);
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}
	
	@PostMapping(value = {"/payment", "/payment/"})
	public PersonDto createPaypalPay(@RequestParam(name = "email") String id,
			@RequestParam(name = "amount") int amt,
			@RequestParam(name = "person") String personName,
			@RequestParam(name = "event") String eventName) 
					throws IllegalArgumentException{
		Person p = service.getPerson(personName);
		Event e = service.getEvent(eventName);
		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		Paypal paypal = service.createPaypalPay(id, amt);
		service.pay(r, paypal);
		PersonDto personDto = convertToDto(p);
		return personDto;
	}
	
	
	@PostMapping(value = {"/assignPromoter", "/assignPromoter/"})
	public OrganizerDto assignOrganizerToEvent(@RequestParam(name = "person") String personName,
			@RequestParam(name = "event") String eventName) throws IllegalArgumentException{

		Organizer p = service.getOrganizer(personName);
		Event e = service.getEvent(eventName);

		service.organizesEvent(p, e);
		
		return convertToDto(p);
	}

	// GET Mappings ////////////////////////////////////////////////////////////////////

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		Boolean isConcert = false;
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			isConcert = false;
			for(Concert concert : service.getAllConcerts()) {
				if(event.getName() == concert.getName()) {
					isConcert = true;
					eventDtos.add(convertConcertToDto(concert));
					break;
				}
			}
			if(!isConcert)
				eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}
	
	@GetMapping(value = { "/concerts", "/concerts/" })
	public List<ConcertDto> getAllConcerts() {
		List<ConcertDto> concertDto = new ArrayList<>();
		for (Concert concert : service.getAllConcerts()) {
			concertDto.add(convertToDto(concert));
		}
		return concertDto;
	}

	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/events/person/{name}", "/events/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerson(p);
	}

	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}
	
	@GetMapping(value = { "/organizers/{name}", "/organizers/{name}/" })
	public OrganizerDto getOrganizerByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getOrganizer(name));
	}

	@GetMapping(value = { "/registrations", "/registrations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}

	
	
	
	@GetMapping(value = { "/registrations/paypal", "/registrations/paypal/" })
	public PaypalDto getPaypalForRegistration(@RequestParam(name = "person") String personName,
		@RequestParam(name = "event") String eventName) throws IllegalArgumentException {
		Person p = service.getPerson(personName);
		Event e = service.getEvent(eventName);

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		Paypal paypal = r.getPaypal();
		return convertToDto(paypal);
	}
	
	
	
	
	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<RegistrationDto> getRegistrationsForPerson(@PathVariable("name") PersonDto pDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());

		return createRegistrationDtosForPerson(p);
	}

	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}
	@GetMapping(value = { "/organizers", "/organizers/"})
	public List<OrganizerDto> getAllorganizers() {
		List<OrganizerDto> organizers = new ArrayList<>();
		for (Organizer organizer : service.getAllOrganizers()) {
			organizers.add(convertToDto(organizer));
		}
		return organizers;
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}
	
	@GetMapping(value = { "/concerts/{name}", "/concerts/{name}/" })
	public ConcertDto getConcertByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getConcert(name));
	}

	
	
	
	////////////////convertToDto////////////////////

	
	
	
	
	
	
	private EventDto convertConcertToDto(Concert t) {
		if(t == null) {
			throw new IllegalArgumentException("There is no such Concert!");
		}
		return (new EventDto(t.getName(), t.getDate(), t.getStartTime(), t.getEndTime(), t.getArtist()));
	}
	
	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		return eventDto;
	}
	

	
	
	private PaypalDto convertToDto(Paypal e) {
		if(e == null) {
			throw new IllegalArgumentException("There is no such Paypal!");
		}
		
		PaypalDto paypalDto = new PaypalDto(e.getEmail(), e.getAmount());
		return paypalDto;
	}

	
	private PaypalDto convertToDtoNullPaypal() {
		return new PaypalDto(null, -1);
	}
	
	
	private ConcertDto convertToDto(Concert e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Concert!");
		}
		ConcertDto concertDto = new ConcertDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime(), e.getArtist());
		return concertDto;
	}
	
	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		return personDto;
	}
	
	
	private OrganizerDto convertToDto(Organizer o) {
		if (o == null) {
			throw new IllegalArgumentException("There is no such Organizer!");
		}
		OrganizerDto organizerDto = new OrganizerDto(o.getName());
		if(o.getOrganizes() != null) {
			organizerDto.setOrganizedEvents(createOrganizedEventDtosForOrganizer(o));
		}
		return organizerDto;
	}
	
	

	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto);
		return rDto;
	}

	
	private RegistrationDto convertToDtoWithoutPerson(Registration r) {
		RegistrationDto rDto = convertToDto(r);
		rDto.setPerson(null);
		return rDto;
	}

	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}

	// Other extracted methods (not part of the API)

	private List<EventDto> createAttendedEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		List<EventDto> events = new ArrayList<>();
		EventDto eventDto;
		for (Event event : eventsForPerson) {
			eventDto = convertToDto(event);
			eventDto.setPaypal(createPaypalDtosForPerson(event, p));
			events.add(convertToDto(event));
		}
		return events;
	}

	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}
	
	private PaypalDto createPaypalDtosForPerson(Event e, Person p) {	
			Registration r = service.getRegistrationByPersonAndEvent(p, e);
			if(r != null) {
				if(r.getPaypal() != null) 
					return convertToDto(r.getPaypal());
			}
		return null;
	}
		
	private List<EventDto> createOrganizedEventDtosForOrganizer(Organizer p){
		List<Event> eventsOrganized = service.getEventsOrganizedByOrganizer(p);
		List<EventDto> events = new ArrayList<>();
		
		for(Event event : eventsOrganized) {
			events.add(convertToDto(event));
		}
		return events;
	}
}








































