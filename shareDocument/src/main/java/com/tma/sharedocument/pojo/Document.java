/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.pojo;

/**
 *
 * @author Minh Anh
 */
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "document")
public class Document {

    public Document() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String fileUrl;

    @Column(name = "file_type", length = 10)
    private String fileType;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "total_view")
    private Long totalView = 0L;

    @Column(name = "total_like")
    private Long totalLike = 0L;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "document_tags",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();
    
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private Set<Like> likes = new HashSet<>();
    
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<View> views = new HashSet<>();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document document = (Document) o;
        return id != null && id.equals(document.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the fileUrl
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * @param fileUrl the fileUrl to set
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the tags
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    /**
     * @return the comments
     */
    public Set<Comment> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    /**
     * @return the totalView
     */
    public Long getTotalView() {
        return totalView;
    }

    /**
     * @param totalView the totalView to set
     */
    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }

    /**
     * @return the totalLike
     */
    public Long getTotalLike() {
        return totalLike;
    }

    /**
     * @param totalLike the totalLike to set
     */
    public void setTotalLike(Long totalLike) {
        this.totalLike = totalLike;
    }

    /**
     * @return the likes
     */
    public Set<Like> getLikes() {
        return likes;
    }

    /**
     * @param likes the likes to set
     */
    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    /**
     * @return the views
     */
    public Set<View> getViews() {
        return views;
    }

    /**
     * @param views the views to set
     */
    public void setViews(Set<View> views) {
        this.views = views;
    }






}
