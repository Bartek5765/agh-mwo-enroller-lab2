package com.company.enroller.controllers;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

@Autowired
MeetingService meetingService;

@RequestMapping(value = "", method = RequestMethod.GET)
public ResponseEntity<?> getAllMeetings(
        @RequestParam(value = "sortBy", required = false) String sortBy,
        @RequestParam(value = "sortOrder", required = false, defaultValue = "") String sortOrder,
        @RequestParam(value = "key", required = false, defaultValue = "") String key) {
    Collection<Participant> participants = participantService.getAll(sortBy, sortOrder, key);
    return new ResponseEntity<>(participants, HttpStatus.OK);
}