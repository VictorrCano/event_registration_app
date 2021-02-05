package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.regex.Pattern;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;

@Service
public class EventRegistrationService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	
	//new features
	
	@Autowired
	private ConcertRepository concertRepository;
	@Autowired
	private PaypalRepository paypalRepository;
	@Autowired
	private OrganizerRepository organizerRepository;
	
	

	
	@Transactional
	public Person createPerson(String name) {
		if (name == null || name.trim().length() == 0 || name == "" || name.replaceAll("\\s", "").length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		} else if (personRepository.existsById(name)) {
			throw new IllegalArgumentException("Person has already been created!");
		}
		Person person = new Person();
		person.setName(name);
		personRepository.save(person);
		return person;
	}
	@Transactional
	public Organizer createOrganizer(String name) {
		if (name == null || name.trim().length() == 0 || name == "" || name.replaceAll("\\s", "").length() == 0) {
			throw new IllegalArgumentException("Organizer name cannot be empty!");
		} else if (organizerRepository.existsById(name)) {
			throw new IllegalArgumentException("Organizer has already been created!");
		}
		Organizer organizer = new Organizer();
		organizer.setName(name);
		organizerRepository.save(organizer);
		return organizer;
	}
	
	
	
	


	@Transactional
	public Person getPerson(String name) {
		if (name == null || name.trim().length() == 0 || name == "" || name.replaceAll("\\s", "").length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Person person = personRepository.findByName(name);
		return person;
	}
	@Transactional
	public Organizer getOrganizer(String name) {
		if (name == null || name.trim().length() == 0 || name == "" || name.replaceAll("\\s", "").length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Organizer organizer = organizerRepository.findByName(name);
		return organizer;
	}
	
	
	
	
	

	@Transactional
	public List<Person> getAllPersons() {
		return toList(personRepository.findAll());
	}
	@Transactional
	public List<Organizer> getAllOrganizers() {
		return toList(organizerRepository.findAll());
	}
	
	
	
	

	@Transactional
	public Event buildEvent(Event event, String name, Date date, Time startTime, Time endTime) {
		// Input validation
		String error = "";
		if (name == null || name.trim().length() == 0 || name == "" || name.replaceAll("\\s", "").length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		return event;
	}
	
	@Transactional
	public Concert buildConcert(Concert concert, String name, Date date, Time startTime, Time endTime, String artist) {
		// Input validation
		String error = "";
		if (name == null || name.trim().length() == 0 || name == "" || name.replaceAll("\\s", "").length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (concertRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		if (artist == null || artist == "" || artist.replaceAll("\\s", "").length() == 0) {
			error = error + "Concert artist cannot be empty! ";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		concert.setName(name);
		concert.setDate(date);
		concert.setStartTime(startTime);
		concert.setEndTime(endTime);
		concert.setArtist(artist);
		return concert;
	}
	
	
	
	
	

	@Transactional
	public Event createEvent(String name, Date date, Time startTime, Time endTime) {
		Event event = new Event();
		buildEvent(event, name, date, startTime, endTime);
		eventRepository.save(event);
		return event;
	}
	
	@Transactional
	public Concert createConcert(String name, Date date, Time startTime, Time endTime, String artist) {
		Concert concert = new Concert();
		buildConcert(concert, name, date, startTime, endTime, artist);
		eventRepository.save(concert);
		return concert;
	}
	
	
	
	
	

	@Transactional
	public Event getEvent(String name) {
		if (name == null || name.trim().length() == 0 || name == "" || name.replaceAll("\\s", "").length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Event event = eventRepository.findByName(name);
		if(event == null) {
			throw new IllegalArgumentException("Event does not exist!");
		}
		return event;
	}
	@Transactional
	public Concert getConcert(String artist) {
		if (artist == null || artist.trim().length() == 0 || artist == "" || artist.replaceAll("\\s", "").length() == 0) {
			throw new IllegalArgumentException("Concert artist cannot be empty!");
		}
		Concert concert = concertRepository.findByArtist(artist);
		if(concert == null) {
			throw new IllegalArgumentException("Concert does not exist!");
		}
		return concert;
	}
	
	
	
	
	
	

	// This returns all objects of instance "Event" (Subclasses are filtered out)
	@Transactional
	public List<Event> getAllEvents() {
		return toList(eventRepository.findAll());
	}
	@Transactional
	public List<Concert> getAllConcerts() {
		return toList(concertRepository.findAll()).stream().filter(e -> e.getClass().equals(Concert.class)).collect(Collectors.toList());
	}
	
	

	@Transactional
	public Registration register(Person person, Event event) {
		String error = "";
		if (person == null) {
			error = error + "Person needs to be selected for registration! ";
		} else if (!personRepository.existsById(person.getName())) {
			error = error + "Person does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for registration!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(person, event)) {
			error = error + "Person is already registered to this event!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Registration registration = new Registration();
		registration.setId(person.getName().hashCode() * event.getName().hashCode());
		registration.setPerson(person);
		registration.setEvent(event);

		registrationRepository.save(registration);

		return registration;
	}

	@Transactional
	public List<Registration> getAllRegistrations() {
		return toList(registrationRepository.findAll());
	}

	@Transactional
	public Registration getRegistrationByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(person, event);
	}
	
	@Transactional
	public List<Registration> getRegistrationsForPerson(Person person){
		if(person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		return registrationRepository.findByPerson(person);
	}

	@Transactional
	public List<Registration> getRegistrationsByPerson(Person person) {
		return toList(registrationRepository.findByPerson(person));
	}

	
	
	
	
	
	@Transactional
	public List<Event> getEventsAttendedByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsAttendedByPerson = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(person)) {
			eventsAttendedByPerson.add(r.getEvent());
		}
		return eventsAttendedByPerson;
	}
	@Transactional
	public List<Event> getEventsOrganizedByOrganizer(Organizer organizer) {
		if (organizer == null) {
			throw new IllegalArgumentException("Organizer cannot be null!");
		}
		List<Event> eventsAttendedByOrganizer = new ArrayList<>();
		for (Event e : organizer.getOrganizes()) {
			eventsAttendedByOrganizer.add(e);
		}
		return eventsAttendedByOrganizer;
	}
	
	
	
	
	
	@Transactional 
	public Paypal createPaypalPay(String id, int amount) {
		if (id == null || id == "" || id.trim().length() == 0 || id.replaceAll("\\s", "").length() == 0 || !Pattern.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9\\.]+", id)){
		      throw new IllegalArgumentException("Email is null or has wrong format!");
		    } else if(paypalRepository.existsById(id)){
		      throw new IllegalArgumentException("Paypal has already been created!");
		    } else if(amount < 0){
		      throw new IllegalArgumentException("Payment amount cannot be negative!");
		    } 
		Paypal paypal = new Paypal();
		paypal.setAmount(amount);
		paypal.setId(id);
		paypalRepository.save(paypal);
		return paypal;
	}
	
	
	@Transactional 
	public Organizer organizesEvent(Organizer organizer, Event event) {
		String error = "";
		if(organizer == null) {
			error = error + "Organizer needs to be selected for organizes!";
		} else if (!organizerRepository.existsById(organizer.getName())) {
			error = error + "Organizer does not exist!";
		}
		
		if(event == null) {
			error = error + "Event needs to be selected for organizes!";
		} else if(!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		
		error = error.trim();
		
		if(error.length() > 0)
			throw new IllegalArgumentException(error.trim());
		
		HashSet<Event> eventsOrganized = new HashSet<Event>();
        if(organizer.getOrganizes() != null){
          for(Event newEvent: organizer.getOrganizes()){
        	  if(newEvent == event)
        		  throw new IllegalArgumentException("Organizer already organizing event!");
        	  eventsOrganized.add(newEvent);
          }
        }
		
        eventsOrganized.add(event);
		organizer.setOrganizes(eventsOrganized);
		organizerRepository.save(organizer);
		return organizer;
	}
	
	
	
	
	
	@Transactional
	public void pay(Registration registration, Paypal paypal) {
		if(registration == null || paypal == null) {
			throw new IllegalArgumentException("Registration and payment cannot be null!");
		} else if (!registrationRepository.existsById(registration.getId())) {
			throw new IllegalArgumentException("Registration does not exist!");
		} else if (!paypalRepository.existsById(paypal.getEmail())) {
			throw new IllegalArgumentException("Paypal does not exist!");
		} 
		registration.setPaypal(paypal);
		registrationRepository.save(registration);
	}
	
	
	
	
	
	

	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
