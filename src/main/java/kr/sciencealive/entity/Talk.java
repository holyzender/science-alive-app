package kr.sciencealive.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

/** A talk / session within an edition's program. */
@JmixEntity
@Entity(name = "sciencealive_Talk")
@Table(name = "SA_TALK")
public class Talk {

    @Id
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    private UUID id;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @Column(name = "EDITION_NO")
    private Integer editionNo;

    @InstanceName
    @Column(name = "TITLE")
    private String title;

    @Column(name = "SPEAKER_NAME")
    private String speakerName;

    @Column(name = "SPEAKER_ROLE")
    private String speakerRole;

    @Column(name = "TIME_SLOT", length = 64)
    private String timeSlot;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Integer getEditionNo() { return editionNo; }
    public void setEditionNo(Integer editionNo) { this.editionNo = editionNo; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSpeakerName() { return speakerName; }
    public void setSpeakerName(String speakerName) { this.speakerName = speakerName; }
    public String getSpeakerRole() { return speakerRole; }
    public void setSpeakerRole(String speakerRole) { this.speakerRole = speakerRole; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
