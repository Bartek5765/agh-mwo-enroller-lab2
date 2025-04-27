package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
        String hql = "FROM Participant";
        if (key != null && !key.isEmpty()) {
            hql += " WHERE login LIKE '%" + key + "%'";
            System.out.println("HQL: " + hql);
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            hql += " ORDER BY " + sortBy + " " + sortOrder;
            System.out.println("HQL: " + hql);
        }



        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

//    public List<Participant> getAll(String sortBy, String sortOrder, String key) {
//
//    }
//        List<Participant> allParticipants = participantRepository.findAll();
//        if (key != null && !key.isEmpty()) {
//            allParticipants = allParticipants.stream().filter(p -> p.getLogin().contains(key)).collect(Collectors.toList());
//        }
//        if ("login".equalsIgnoreCase(sortBy)) {
//            if ("DESC".equalsIgnoreCase(sortOrder)) {
//                allParticipants.sort((p1, p2) -> p2.getLogin().compareTo(p1.getLogin()));
//            } else {
//                allParticipants.sort(Comparator.comparing(Participant::getLogin));
//            }
//        }
//        return allParticipants;
//    }


//    public List<Participant> getAllParticipants(String sortBy, String sortOrder, String key) {
//        List<Participant> result = getAllParticipants(sortBy, sortOrder, key);
//
//        if (!key.isEmpty()) {
//            result = result.stream()
//                    .filter(p -> p.getLogin().contains(key))
//                    .collect(Collectors.toList());
//        }
//
//        if (!sortBy.isEmpty() && sortBy.equals("login")) {
//            Comparator<Participant> comparator = Comparator.comparing(Participant::getLogin);
//            if ("DESC".equalsIgnoreCase(sortOrder)) {
//                comparator = comparator.reversed();
//            }
//            result = result.stream().sorted(comparator).collect(Collectors.toList());
//        }
//        return result;
//    }




    public Participant findByLogin(String login) {
        return connector.getSession().get(Participant.class, login);
    }

    public Participant add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
        return participant;
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }




}
