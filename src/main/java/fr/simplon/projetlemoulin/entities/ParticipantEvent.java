package fr.simplon.projetlemoulin.entities;

import jakarta.persistence.*;

@Entity
@Table(name="participant_event")
public class ParticipantEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "id_evenement", referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "id_participant", referencedColumnName = "id")
    private Participant participant;

    public ParticipantEvent() {
    }

    public ParticipantEvent(Participant participant, Event event) {
        this.participant = participant;
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
