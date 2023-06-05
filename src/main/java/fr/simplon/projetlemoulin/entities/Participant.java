package fr.simplon.projetlemoulin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name="participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    private String phone;

    private String email;
    private int zip_code;
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "participant")
    private Set<ParticipantEvent> participantEvents;

    public Participant() {
    }

    public Participant(String firstname, String lastname, String phone, String email, int zip_code, String username, Set<ParticipantEvent> participantEvents) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.zip_code = zip_code;
        this.username = username;
        this.participantEvents = participantEvents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getZip_code() {
        return zip_code;
    }

    public void setZip_code(int zip_code) {
        this.zip_code = zip_code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<ParticipantEvent> getParticipantEvents() {
        return participantEvents;
    }

    public void setParticipantEvents(Set<ParticipantEvent> participantEvents) {
        this.participantEvents = participantEvents;
    }
}
