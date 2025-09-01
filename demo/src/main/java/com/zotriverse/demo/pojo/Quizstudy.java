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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "quizstudy")

@NamedQueries({
    @NamedQuery(name = "Quizstudy.findAll", query = "SELECT q FROM Quizstudy q"),
    @NamedQuery(name = "Quizstudy.findById", query = "SELECT q FROM Quizstudy q WHERE q.id = :id"),
    @NamedQuery(name = "Quizstudy.findByCreatedAt", query = "SELECT q FROM Quizstudy q WHERE q.createdAt = :createdAt"),
    @NamedQuery(name = "Quizstudy.findByUpdateAt", query = "SELECT q FROM Quizstudy q WHERE q.updateAt = :updateAt")})
@AllArgsConstructor
@Builder
public class Quizstudy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Lesson lessonId;
    @JoinColumn(name = "user_id", referencedColumnName = "account_id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private User userId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "quizstudy")
    @JsonIgnore
    private Teststudy teststudy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizId")
    @JsonIgnore
    private Set<Quizhistory> quizhistorySet;

    public Quizstudy() {
    }

    public Quizstudy(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Lesson getLessonId() {
        return lessonId;
    }

    public void setLessonId(Lesson lessonId) {
        this.lessonId = lessonId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Teststudy getTeststudy() {
        return teststudy;
    }

    public void setTeststudy(Teststudy teststudy) {
        this.teststudy = teststudy;
    }


    public Set<Quizhistory> getQuizhistorySet() {
        return quizhistorySet;
    }

    public void setQuizhistorySet(Set<Quizhistory> quizhistorySet) {
        this.quizhistorySet = quizhistorySet;
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
        if (!(object instanceof Quizstudy)) {
            return false;
        }
        Quizstudy other = (Quizstudy) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.Quizstudy[ id=" + id + " ]";
    }
    
}
