package kr.sciencealive.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * A PR Award press-release submission. {@code division} is basic|applied,
 * {@code award} is gold|silver|bronze or null (non-winning).
 */
@JmixEntity
@Entity(name = "sciencealive_Submission")
@Table(name = "SA_SUBMISSION")
public class Submission {

    @Id
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    private UUID id;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @Column(name = "CODE", length = 32)
    private String code;

    @InstanceName
    @Column(name = "TITLE")
    private String title;

    @Column(name = "ORG")
    private String org;

    @Column(name = "DIVISION", length = 32)
    private String division;

    @Column(name = "EDITION_NO")
    private Integer editionNo;

    @Column(name = "AWARD", length = 32)
    private String award;

    @Column(name = "VIEWS")
    private Integer views;

    @Column(name = "IMAGE_GRADIENT")
    private String imageGradient;

    /** Optional CSS class providing a bundled background image (e.g. sa-media--mrna); takes precedence over imageGradient. */
    @Column(name = "MEDIA_CLASS", length = 64)
    private String mediaClass;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getOrg() { return org; }
    public void setOrg(String org) { this.org = org; }
    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }
    public Integer getEditionNo() { return editionNo; }
    public void setEditionNo(Integer editionNo) { this.editionNo = editionNo; }
    public String getAward() { return award; }
    public void setAward(String award) { this.award = award; }
    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }
    public String getImageGradient() { return imageGradient; }
    public void setImageGradient(String imageGradient) { this.imageGradient = imageGradient; }
    public String getMediaClass() { return mediaClass; }
    public void setMediaClass(String mediaClass) { this.mediaClass = mediaClass; }
}
