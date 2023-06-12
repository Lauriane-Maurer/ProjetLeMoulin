package fr.simplon.projetlemoulin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String type;
    @NotNull
    private String title;
    @NotNull
    @Column(length = 600)
    private String description;
    @Future(message = "La date de début doit être dans le futur")
    @NotNull
    private LocalDateTime start_date;
    @Future(message = "La date de fin doit être dans le futur")
    @NotNull
    private LocalDateTime end_date;

    private boolean limit_places;
    private Integer total_places;
    private Integer available_places;
    private Double price;
    private String speaker;
    private String photo;

    @ManyToMany
    @JoinTable(name = "event_partner",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "partner_id"))
    private List<Partner> partners;

    @JsonIgnore
    @OneToMany(mappedBy = "event")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<ParticipantEvent> participantEvents;


    public Event() {
    }

    public Event(String type, String title, String description, LocalDateTime start_date, LocalDateTime end_date, boolean limit_places, Integer total_places, Integer available_places, Double price, String speaker, String photo, List<ParticipantEvent> participantEvents, List<Partner> partners) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.limit_places = limit_places;
        this.total_places = total_places;
        this.available_places = available_places;
        this.price = price;
        this.speaker = speaker;
        this.photo = photo;
        this.participantEvents = participantEvents;
        this.partners = partners;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    public boolean isLimit_places() {
        return limit_places;
    }

    public void setLimit_places(boolean limit_places) {
        this.limit_places = limit_places;
    }

    public Integer getTotal_places() {
        return total_places;
    }

    public void setTotal_places(Integer total_places) {
        this.total_places = total_places;
    }

    public Integer getAvailable_places() {
        return available_places;
    }

    public void setAvailable_places(Integer available_places) {
        this.available_places = available_places;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<ParticipantEvent> getParticipantEvents() {
        return participantEvents;
    }

    public void setParticipantEvents(List<ParticipantEvent> participantEvents) {
        this.participantEvents = participantEvents;
    }

    public List<Partner> getPartners() {
        return partners;
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }

    @PrePersist
    private void initAvalaiblePlaces() {
        if (total_places != null) {
            available_places = total_places;
        }
    }

    public void decrementAvailablePlaces() {
        if (this.available_places != null) {
            this.available_places--;
        }
    }

    public void incrementAvailablePlaces() {
        if (this.available_places != null) {
            this.available_places++;
        }
    }

}
