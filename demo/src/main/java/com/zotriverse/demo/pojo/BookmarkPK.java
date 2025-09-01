/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zotriverse.demo.pojo;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
@Embeddable
public class BookmarkPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lesson_id")
    private int lessonId;

    public BookmarkPK() {
    }

    public BookmarkPK(int userId, int lessonId) {
        this.userId = userId;
        this.lessonId = lessonId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) lessonId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookmarkPK)) {
            return false;
        }
        BookmarkPK other = (BookmarkPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.lessonId != other.lessonId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.BookmarkPK[ userId=" + userId + ", lessonId=" + lessonId + " ]";
    }
    
}
