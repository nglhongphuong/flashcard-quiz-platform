/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zotriverse.demo.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

/**
 *
 * @author ASUS
 */
@Embeddable
@Builder
public class FlashcardStudyPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "flashcard_id")
    private int flashcardId;

    public FlashcardStudyPK() {
    }

    public FlashcardStudyPK(int userId, int flashcardId) {
        this.userId = userId;
        this.flashcardId = flashcardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFlashcardId() {
        return flashcardId;
    }

    public void setFlashcardId(int flashcardId) {
        this.flashcardId = flashcardId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) flashcardId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlashcardStudyPK)) {
            return false;
        }
        FlashcardStudyPK other = (FlashcardStudyPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.flashcardId != other.flashcardId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.FlashcardStudyPK[ userId=" + userId + ", flashcardId=" + flashcardId + " ]";
    }
    
}
