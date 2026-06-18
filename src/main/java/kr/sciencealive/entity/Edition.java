package kr.sciencealive.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

/** A 사이언스얼라이브 edition (회차). */
@JmixEntity
@Entity(name = "sciencealive_Edition")
@Table(name = "SA_EDITION")
public class Edition {

    @Id
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    private UUID id;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @Column(name = "EDITION_NO")
    private Integer editionNo;

    @Column(name = "YEAR_")
    private Integer year;

    @InstanceName
    @Column(name = "THEME")
    private String theme;

    @Column(name = "EVENT_DATE")
    private String eventDate;

    @Column(name = "VENUE")
    private String venue;

    @Column(name = "TALKS")
    private Integer talks;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "POSTER_IMAGE")
    private String posterImage;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Integer getEditionNo() { return editionNo; }
    public void setEditionNo(Integer editionNo) { this.editionNo = editionNo; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public Integer getTalks() { return talks; }
    public void setTalks(Integer talks) { this.talks = talks; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPosterImage() { return posterImage; }
    public void setPosterImage(String posterImage) { this.posterImage = posterImage; }
}
