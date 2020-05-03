package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.company.enroller.persistence.ParticipantService;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;
	ParticipantService participantService;


	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		Collection<Participant> participants = meeting.getParticipants();
		if (participants ==null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}


	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {

		Meeting foundMeeting = meetingService.findById(meeting.getId());
		if (foundMeeting != null) {
			return new ResponseEntity("Unable to create. A meeting with Id " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
		}
		meetingService.createMeeting(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		meetingService.deleteMeeting(meeting);
		return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{mid}/{pid}", method = RequestMethod.PUT)
	public ResponseEntity<?> addNewParticipant(@PathVariable("mid") long mid, @PathVariable("pid") String pid) {
		Meeting meeting = meetingService.findById(mid);
		Participant participant = participantService.findByLogin(pid);
		meeting.addParticipant(participant);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/{mid}/{pid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("mid") long mid, @PathVariable("pid") String pid) {
		Meeting meeting = meetingService.findById(mid);
		Participant participant = participantService.findByLogin(pid);
		meeting.removeParticipant(participant);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}


}
