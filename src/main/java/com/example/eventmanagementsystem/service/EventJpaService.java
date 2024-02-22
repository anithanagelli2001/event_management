package com.example.eventmanagementsystem.service;

import com.example.eventmanagementsystem.model.Sponsor;
import com.example.eventmanagementsystem.model.Event;
import com.example.eventmanagementsystem.repository.SponsorJpaRepository;
import com.example.eventmanagementsystem.repository.EventJpaRepository;
import com.example.eventmanagementsystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventJpaService implements EventRepository {

    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Autowired
    private SponsorJpaRepository sponsorJpaRepository;

    @Override
    public ArrayList<Event> getEvents() {
        List<Event> eventList = eventJpaRepository.findAll();
        ArrayList<Event> events = new ArrayList<>(eventList);
        return events;
    }

    @Override
    public Event getEventById(int eventId) {
        try {
            return eventJpaRepository.findById(eventId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Event addEvent(Event event) {
        return eventJpaRepository.save(event);
    }

    @Override
    public Event updateEvent(int eventId, Event event) {
        try {
            Event newEvent = eventJpaRepository.findById(eventId).get();
            if (event.getEventName() != null) {
                newEvent.setEventName(event.getEventName());
            }
            if (event.getDate() != null) {
                newEvent.setDate(event.getDate());
            }
            return eventJpaRepository.save(newEvent);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteEvent(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();
            List<Sponsor> sponsorList = sponsorJpaRepository.findByEvent(event);
            for (Sponsor sponsor : sponsorList) {
                sponsor.setEvent(null);
            }
            sponsorJpaRepository.saveAll(sponsorList);
            eventJpaRepository.deleteById(eventId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Sponsor> getEventSponsors(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();
            return sponsorJpaRepository.findByEvent(event);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}