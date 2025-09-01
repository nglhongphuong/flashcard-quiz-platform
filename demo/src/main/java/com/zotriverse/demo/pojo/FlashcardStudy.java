/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zotriverse.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "flashcard_study")
@AllArgsConstructor
@Builder
@NamedQueries({
    @NamedQuery(name = "FlashcardStudy.findAll", query = "SELECT f FROM FlashcardStudy f"),
    @NamedQuery(name = "FlashcardStudy.findByUserId", query = "SELECT f FROM FlashcardStudy f WHERE f.flashcardStudyPK.userId = :userId"),
    @NamedQuery(name = "FlashcardStudy.findByFlashcardId", query = "SELECT f FROM FlashcardStudy f WHERE f.flashcardStudyPK.flashcardId = :flashcardId"),
    @NamedQuery(name = "FlashcardStudy.findByStatus", query = "SELECT f FROM FlashcardStudy f WHERE f.status = :status"),
    @NamedQuery(name = "FlashcardStudy.findByCreatedAt", query = "SELECT f FROM FlashcardStudy f WHERE f.createdAt = :createdAt"),
    @NamedQuery(name = "FlashcardStudy.findByUpdateAt", query = "SELECT f FROM FlashcardStudy f WHERE f.updateAt = :updateAt")})
public class FlashcardStudy implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FlashcardStudyPK flashcardStudyPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 14)
    @Column(name = "status")
    private String status;
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;
    @JoinColumn(name = "flashcard_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Flashcard flashcard;
    @JoinColumn(name = "user_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private User user;

    public FlashcardStudy() {
    }

    public FlashcardStudy(FlashcardStudyPK flashcardStudyPK) {
        this.flashcardStudyPK = flashcardStudyPK;
    }

    public FlashcardStudy(FlashcardStudyPK flashcardStudyPK, String status) {
        this.flashcardStudyPK = flashcardStudyPK;
        this.status = status;
    }

    public FlashcardStudy(int userId, int flashcardId) {
        this.flashcardStudyPK = new FlashcardStudyPK(userId, flashcardId);
    }

    public FlashcardStudyPK getFlashcardStudyPK() {
        return flashcardStudyPK;
    }

    public void setFlashcardStudyPK(FlashcardStudyPK flashcardStudyPK) {
        this.flashcardStudyPK = flashcardStudyPK;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Flashcard getFlashcard() {
        return flashcard;
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flashcardStudyPK != null ? flashcardStudyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlashcardStudy)) {
            return false;
        }
        FlashcardStudy other = (FlashcardStudy) object;
        if ((this.flashcardStudyPK == null && other.flashcardStudyPK != null) || (this.flashcardStudyPK != null && !this.flashcardStudyPK.equals(other.flashcardStudyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.FlashcardStudy[ flashcardStudyPK=" + flashcardStudyPK + " ]";
    }
    
}
