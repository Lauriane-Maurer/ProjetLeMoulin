package fr.simplon.projetlemoulin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Future;
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
    private LocalDateTime startDate;
    @Future(message = "La date de fin doit être dans le futur")
    @NotNull
    private LocalDateTime EndDate;

    private boolean limit_places;
    private Integer total_places;
    private Integer available_places;
    private Double price;
    private String speaker;
    private String photo;

    @JsonIgnore
    @OneToMany(mappedBy = "event")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<ParticipantEvent> participantEvents;

    public Event() {
    }

    public Event(String type, String title, String description, LocalDateTime startDate, LocalDateTime endDate, boolean limit_places, Integer total_places, Integer available_places, Double price, String speaker, String photo) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        EndDate = endDate;
        this.limit_places = limit_places;
        this.total_places = total_places;
        this.available_places = available_places;
        this.price = price;
        this.speaker = speaker;
        this.photo = photo;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        EndDate = endDate;
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
}
