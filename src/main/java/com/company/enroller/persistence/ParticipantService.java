package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("participantService")
public class ParticipantService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    DatabaseConnector connector;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
        String hql = "FROM Participant";
        if (key != null && !key.isEmpty()) {
            hql += " WHERE login LIKE :key";
        }
        if ("login".equalsIgnoreCase(sortBy)) {
            hql += " ORDER BY login " + ("DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
        }
        Query query = connector.getSession().createQuery(hql);
        if (key != null && !key.isEmpty()) {
            query.setParameter("key", "%" + key + "%");
        }
        return query.list();
    }

    public Participant add(Participant participant) {
        String raw = participant.getPassword();
        participant.setPassword(passwordEncoder.encode(raw));

        Transaction tx = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        tx.commit();
        return participant;
    }


    public Participant findByLogin(String login) {
        return connector.getSession().get(Participant.class, login);
    }

    public void update(Participant participant) {
        Transaction tx = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        tx.commit();
    }

    public void delete(Participant participant) {
        Transaction tx = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        tx.commit();
    }
}

