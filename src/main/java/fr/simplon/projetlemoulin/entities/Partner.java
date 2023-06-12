package fr.simplon.projetlemoulin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import java.util.List;

@Entity
@Table(name="partners")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String activity;
    @NotNull
    @Column(length = 600)
    @Pattern(regexp = "^[\\p{L}\\d\\s’'-.,?!:()…]+$", message = "La description contient des caractères non autorisés")
    private String description;
    @Pattern(regexp = "^[\\p{L}\\d\\s’'-.,?!:()…]+$", message = "L'adresse contient des caractères non autorisés")
    private String address;
    @Pattern(regexp = "^[\\p{L}\\d\\s’'-.,?!:()…]+$", message = "Le nom de la ville contient des caractères non autorisés")
    private String town;
    @Size(min = 5, max = 5, message = "Le code postal doit comporter 5 caractères")
    private String zip_code;
    @URL(message = "Veuillez entrer une URL valide commençant par 'https://'")
    private String url;
    private Double latitude;
    private Double longitude;


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "event_partner",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "partner_id"))
    private List<Event> events;

    public Partner() {
    }

    public Partner(String name, String activity, String description, String address, String town, String zip_code, String url, Double latitude, Double longitude) {
        this.name = name;
        this.activity = activity;
        this.description = description;
        this.address = address;
        this.town = town;
        this.zip_code = zip_code;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
