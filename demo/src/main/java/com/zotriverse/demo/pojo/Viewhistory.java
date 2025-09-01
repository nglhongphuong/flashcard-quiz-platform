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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "viewhistory")

@NamedQueries({
    @NamedQuery(name = "Viewhistory.findAll", query = "SELECT v FROM Viewhistory v"),
    @NamedQuery(name = "Viewhistory.findByUserId", query = "SELECT v FROM Viewhistory v WHERE v.viewhistoryPK.userId = :userId"),
    @NamedQuery(name = "Viewhistory.findByLessonId", query = "SELECT v FROM Viewhistory v WHERE v.viewhistoryPK.lessonId = :lessonId"),
    @NamedQuery(name = "Viewhistory.findByStudy", query = "SELECT v FROM Viewhistory v WHERE v.study = :study"),
    @NamedQuery(name = "Viewhistory.findByCreatedAt", query = "SELECT v FROM Viewhistory v WHERE v.createdAt = :createdAt"),
    @NamedQuery(name = "Viewhistory.findByUpdateAt", query = "SELECT v FROM Viewhistory v WHERE v.updateAt = :updateAt")})
@AllArgsConstructor
@Builder
public class Viewhistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ViewhistoryPK viewhistoryPK;
    @Column(name = "study")
    private Boolean study;
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

    public Viewhistory() {
    }

    public Viewhistory(ViewhistoryPK viewhistoryPK) {
        this.viewhistoryPK = viewhistoryPK;
    }

    public Viewhistory(int userId, int lessonId) {
        this.viewhistoryPK = new ViewhistoryPK(userId, lessonId);
    }

    public ViewhistoryPK getViewhistoryPK() {
        return viewhistoryPK;
    }

    public void setViewhistoryPK(ViewhistoryPK viewhistoryPK) {
        this.viewhistoryPK = viewhistoryPK;
    }

    public Boolean getStudy() {
        return study;
    }

    public void setStudy(Boolean study) {
        this.study = study;
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
        hash += (viewhistoryPK != null ? viewhistoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Viewhistory)) {
            return false;
        }
        Viewhistory other = (Viewhistory) object;
        if ((this.viewhistoryPK == null && other.viewhistoryPK != null) || (this.viewhistoryPK != null && !this.viewhistoryPK.equals(other.viewhistoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.Viewhistory[ viewhistoryPK=" + viewhistoryPK + " ]";
    }
    
}
