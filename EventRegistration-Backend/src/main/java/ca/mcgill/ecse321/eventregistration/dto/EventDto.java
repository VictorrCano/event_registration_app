package ca.mcgill.ecse321.eventregistration.dto;

import java.sql.Date;
import java.sql.Time;

public class EventDto {

	private String name;
	private Date date;
	private Time startTime;
	private Time endTime;
	private String artist;
	private PaypalDto paypalDto;

	public EventDto() {
	}

	public EventDto(String name) {
		this(name, Date.valueOf("1971-01-01"), Time.valueOf("00:00:00"), Time.valueOf("23:59:59"));
	}
	
	public EventDto(String name, Date date, Time startTime, Time endTime, String artist) {
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.artist = artist;
	}

	public EventDto(String name, Date date, Time startTime, Time endTime) {
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.artist = "--";
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Time getEndTime() {
		return endTime;
	}
	
	public String getArtist() {
		return artist;
	}
	public PaypalDto getPaypal(){
		return paypalDto;
	}
	
	public void setPaypal(PaypalDto paypalDto) {
		this.paypalDto = paypalDto;
	}
}
