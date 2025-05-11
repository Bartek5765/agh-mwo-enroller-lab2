package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

	@Autowired
	ParticipantService participantService;

	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = this.session.createQuery(hql);
		return query.list();
	}

	public Meeting getMeetingById(long id) {
		String hql = "FROM Meeting WHERE id = :id";
		Query query = this.session.createQuery(hql);
		query.setParameter("id", id);
		return (Meeting) query.uniqueResult();
	}

	public Meeting createMeeting(Meeting meeting) {
		Transaction tx = session.beginTransaction();
		session.save(meeting);
		tx.commit();
		return meeting;
	}
	public Meeting removeMeetingById(long id) {
		Transaction tx = session.beginTransaction();
		Meeting meeting = getMeetingById(id);
		if (meeting == null) {
			tx.commit();
			return null;
		}
		session.delete(meeting);
		tx.commit();
		return meeting;
	}

	public Meeting updateMeetingById(Meeting meeting, long id) {
		Transaction tx = session.beginTransaction();
		Meeting updatedMeeting = getMeetingById(id);
		updatedMeeting.setTitle(meeting.getTitle());
		updatedMeeting.setDescription(meeting.getDescription());
		updatedMeeting.setDate(meeting.getDate());
		session.update(updatedMeeting);
		tx.commit();
		return updatedMeeting;
	}

	public Collection<Participant> getParticipants(long meetingId) {
		Meeting meeting = getMeetingById(meetingId);
		return (meeting != null) ? meeting.getParticipants() : null;
	}

	public Meeting addParticipant(long meetingId, String login) {
		Meeting meeting = getMeetingById(meetingId);
		Participant participant = participantService.findByLogin(login);
		if (meeting == null || participant == null) {
			return null;
		}
		Transaction tx = session.beginTransaction();
		meeting.addParticipant(participant);
		session.merge(meeting);
		tx.commit();
		return meeting;
	}

	public Meeting removeParticipant(long meetingId, String login) {
		Meeting meeting = getMeetingById(meetingId);
		Participant participant = participantService.findByLogin(login);
		if (meeting == null || participant == null) {
			return null;
		}
		Transaction tx = session.beginTransaction();
		meeting.removeParticipant(participant);
		session.merge(meeting);
		tx.commit();
		return meeting;
	}
}




