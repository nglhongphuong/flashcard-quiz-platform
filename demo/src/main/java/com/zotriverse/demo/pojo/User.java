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
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByAccountId", query = "SELECT u FROM User u WHERE u.accountId = :accountId")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "account_id")
    private Integer accountId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Set<FlashcardStudy> flashcardStudySet;
    @OneToMany(mappedBy = "userId")
    @JsonIgnore
    private Set<Lesson> lessonSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Set<Rating> ratingSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Set<Bookmark> bookmarkSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    @JsonIgnore
    private Set<Quizstudy> quizstudySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    @JsonIgnore
    private Set<Comment> commentSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Set<Viewhistory> viewhistorySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    @JsonIgnore
    private Set<Lessonschedule> lessonscheduleSet;
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    @JsonIgnore
    private Account account;

    public User() {
    }

    public User(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }


    public Set<FlashcardStudy> getFlashcardStudySet() {
        return flashcardStudySet;
    }

    public void setFlashcardStudySet(Set<FlashcardStudy> flashcardStudySet) {
        this.flashcardStudySet = flashcardStudySet;
    }


    public Set<Lesson> getLessonSet() {
        return lessonSet;
    }

    public void setLessonSet(Set<Lesson> lessonSet) {
        this.lessonSet = lessonSet;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountId != null ? accountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.accountId == null && other.accountId != null) || (this.accountId != null && !this.accountId.equals(other.accountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.User[ accountId=" + accountId + " ]";
    }
    
}
