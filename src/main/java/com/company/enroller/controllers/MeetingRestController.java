package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {
    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingById(@PathVariable("id") long id) {
        Meeting meeting = meetingService.getMeetingById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.getMeetingById(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity("Unable to add meeting", HttpStatus.CONFLICT);
        }
        Meeting meeting1 = meetingService.createMeeting(meeting);
        return new ResponseEntity<Meeting>(meeting1, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeMeetingById(@PathVariable("id") long id) {
        Meeting meeting = meetingService.getMeetingById(id);
        if(meeting == null) {
            return new ResponseEntity<>("unable to remove meeting", HttpStatus.NOT_FOUND);
        }
        Meeting meeting1 = meetingService.removeMeetingById(id);
        return new ResponseEntity<Meeting>(meeting1, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeetingById(@PathVariable("id") long id, @RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.getMeetingById(id);
        if (foundMeeting != null) {
            return new ResponseEntity<>("Unable to update meeting", HttpStatus.CONFLICT);
        }
        Meeting meeting1 = meetingService.updateMeetingById(meeting,id);
        return new ResponseEntity<>(meeting1, HttpStatus.OK);
    }
    @RequestMapping(value="/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
        Collection<Participant> participants = meetingService.getParticipants(id);
        if (participants == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id,
                                                     @RequestBody Participant participant) {
        Meeting meeting = meetingService.getMeetingById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Participant p = participantService.findByLogin(participant.getLogin());
        if (p == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Meeting updated = meetingService.addParticipant(id, participant.getLogin());
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Meeting>(updated, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipantFromMeeting(@PathVariable("id") long id,
                                                          @PathVariable("login") String login) {
        Meeting meeting = meetingService.getMeetingById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Participant p = participantService.findByLogin(login);
        if (p == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Meeting updated = meetingService.removeParticipant(id, login);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(updated, HttpStatus.NO_CONTENT);
    }
}

