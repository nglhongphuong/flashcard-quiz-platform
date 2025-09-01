/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zotriverse.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "bookmark")

@NamedQueries({
    @NamedQuery(name = "Bookmark.findAll", query = "SELECT b FROM Bookmark b"),
    @NamedQuery(name = "Bookmark.findByUserId", query = "SELECT b FROM Bookmark b WHERE b.bookmarkPK.userId = :userId"),
    @NamedQuery(name = "Bookmark.findByLessonId", query = "SELECT b FROM Bookmark b WHERE b.bookmarkPK.lessonId = :lessonId"),
    @NamedQuery(name = "Bookmark.findByCreatedAt", query = "SELECT b FROM Bookmark b WHERE b.createdAt = :createdAt"),
    @NamedQuery(name = "Bookmark.findByUpdateAt", query = "SELECT b FROM Bookmark b WHERE b.updateAt = :updateAt")})
@AllArgsConstructor
@Builder
public class Bookmark implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BookmarkPK bookmarkPK;
    @Lob
    @Size(max = 65535)
    @Column(name = "notice")
    private String notice;
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;
    @JoinColumn(name = "lesson_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Lesson lesson;
    @JoinColumn(name = "user_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private User user;

    public Bookmark() {
    }

    public Bookmark(BookmarkPK bookmarkPK) {
        this.bookmarkPK = bookmarkPK;
    }

    public Bookmark(int userId, int lessonId) {
        this.bookmarkPK = new BookmarkPK(userId, lessonId);
    }

    public BookmarkPK getBookmarkPK() {
        return bookmarkPK;
    }

    public void setBookmarkPK(BookmarkPK bookmarkPK) {
        this.bookmarkPK = bookmarkPK;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
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
        hash += (bookmarkPK != null ? bookmarkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bookmark)) {
            return false;
        }
        Bookmark other = (Bookmark) object;
        if ((this.bookmarkPK == null && other.bookmarkPK != null) || (this.bookmarkPK != null && !this.bookmarkPK.equals(other.bookmarkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.Bookmark[ bookmarkPK=" + bookmarkPK + " ]";
    }
    
}
