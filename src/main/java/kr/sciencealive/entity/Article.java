package kr.sciencealive.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

/** A news article / press coverage related to an edition. */
@JmixEntity
@Entity(name = "sciencealive_Article")
@Table(name = "SA_ARTICLE")
public class Article {

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

    @Column(name = "PUBLISHER")
    private String publisher;

    @Column(name = "PUBLISHED_DATE", length = 32)
    private String publishedDate;

    @Column(name = "URL", length = 512)
    private String url;

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
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getPublishedDate() { return publishedDate; }
    public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
