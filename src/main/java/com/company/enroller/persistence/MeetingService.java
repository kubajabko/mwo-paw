package com.company.enroller.persistence;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	private Session session;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
		this.session = connector.getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = this.session.createQuery(hql);
		return query.list();
	}

	public Meeting findById(long id) {
		return (Meeting) this.session.get(Meeting.class, id);
	}

	public Meeting createMeeting(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}

	public void deleteMeeting(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
	}

	public void addParticipant(Participant participant, Meeting meeting) {
		meeting.addParticipant(participant);
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
	}

	public void removeParticipant(Participant participant, Meeting meeting) {
		meeting.removeParticipant(participant);
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
	}

}
