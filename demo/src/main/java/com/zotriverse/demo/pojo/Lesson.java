/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zotriverse.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "lesson")

@NamedQueries({
    @NamedQuery(name = "Lesson.findAll", query = "SELECT l FROM Lesson l"),
    @NamedQuery(name = "Lesson.findById", query = "SELECT l FROM Lesson l WHERE l.id = :id"),
    @NamedQuery(name = "Lesson.findByTitle", query = "SELECT l FROM Lesson l WHERE l.title = :title"),
    @NamedQuery(name = "Lesson.findByImage", query = "SELECT l FROM Lesson l WHERE l.image = :image"),
    @NamedQuery(name = "Lesson.findByVisibility", query = "SELECT l FROM Lesson l WHERE l.visibility = :visibility"),
    @NamedQuery(name = "Lesson.findByIsCommentLocked", query = "SELECT l FROM Lesson l WHERE l.isCommentLocked = :isCommentLocked"),
    @NamedQuery(name = "Lesson.findByCreatedAt", query = "SELECT l FROM Lesson l WHERE l.createdAt = :createdAt"),
    @NamedQuery(name = "Lesson.findByUpdateAt", query = "SELECT l FROM Lesson l WHERE l.updateAt = :updateAt")})
@AllArgsConstructor
@Builder
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "title")
    private String title;
//    @Lob
//    @Size(max = 65535)
//    @Column(name = "description")
//    private String description;

    @Lob
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;
    @Size(max = 500)
    @Column(name = "image")
    private String image;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "visibility")
    private String visibility;
    @Column(name = "is_comment_locked")
    private Boolean isCommentLocked;
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lessonId")
    @JsonIgnore
    private Set<Flashcard> flashcardSet;
    @JoinColumn(name = "user_id", referencedColumnName = "account_id")
    @ManyToOne
    @JsonIgnore
    private User userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lesson")
    @JsonIgnore
    private Set<Rating> ratingSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lesson")
    @JsonIgnore
    private Set<Bookmark> bookmarkSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lessonId")
    @JsonIgnore
    private Set<Quizstudy> quizstudySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lessonId")
    @JsonIgnore
    private Set<Comment> commentSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lesson")
    @JsonIgnore
    private Set<Viewhistory> viewhistorySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lessonId")
    @JsonIgnore
    private Set<Lessonschedule> lessonscheduleSet;

    public Lesson() {
    }

    public Lesson(Integer id) {
        this.id = id;
    }

    public Lesson(Integer id, String title, String visibility) {
        this.id = id;
        this.title = title;
        this.visibility = visibility;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Boolean getIsCommentLocked() {
        return isCommentLocked;
    }

    public void setIsCommentLocked(Boolean isCommentLocked) {
        this.isCommentLocked = isCommentLocked;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }


    public Set<Flashcard> getFlashcardSet() {
        return flashcardSet;
    }

    public void setFlashcardSet(Set<Flashcard> flashcardSet) {
        this.flashcardSet = flashcardSet;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }


    public Set<Rating> getRatingSet() {
        return ratingSet;
    }

    public void setRatingSet(Set<Rating> ratingSet) {
        this.ratingSet = ratingSet;
    }


    public Set<Bookmark> getBookmarkSet() {
        return bookmarkSet;
    }

    public void setBookmarkSet(Set<Bookmark> bookmarkSet) {
        this.bookmarkSet = bookmarkSet;
    }


    public Set<Quizstudy> getQuizstudySet() {
        return quizstudySet;
    }

    public void setQuizstudySet(Set<Quizstudy> quizstudySet) {
        this.quizstudySet = quizstudySet;
    }


    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }


    public Set<Viewhistory> getViewhistorySet() {
        return viewhistorySet;
    }

    public void setViewhistorySet(Set<Viewhistory> viewhistorySet) {
        this.viewhistorySet = viewhistorySet;
    }


    public Set<Lessonschedule> getLessonscheduleSet() {
        return lessonscheduleSet;
    }

    public void setLessonscheduleSet(Set<Lessonschedule> lessonscheduleSet) {
        this.lessonscheduleSet = lessonscheduleSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lesson)) {
            return false;
        }
        Lesson other = (Lesson) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.Lesson[ id=" + id + " ]";
    }
    
}
