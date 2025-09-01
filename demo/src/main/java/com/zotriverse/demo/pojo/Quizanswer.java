/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zotriverse.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
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
@Table(name = "quizanswer")

@NamedQueries({
    @NamedQuery(name = "Quizanswer.findAll", query = "SELECT q FROM Quizanswer q"),
    @NamedQuery(name = "Quizanswer.findById", query = "SELECT q FROM Quizanswer q WHERE q.id = :id"),
    @NamedQuery(name = "Quizanswer.findByPosition", query = "SELECT q FROM Quizanswer q WHERE q.position = :position"),
    @NamedQuery(name = "Quizanswer.findByCreatedAt", query = "SELECT q FROM Quizanswer q WHERE q.createdAt = :createdAt"),
    @NamedQuery(name = "Quizanswer.findByUpdateAt", query = "SELECT q FROM Quizanswer q WHERE q.updateAt = :updateAt")})
@AllArgsConstructor
@Builder
public class Quizanswer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Column(name = "definition", columnDefinition = "LONGTEXT")
    private String definition;
    @Basic(optional = false)
    @NotNull
    @Column(name = "position")
    private int position;
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;
    @JoinColumn(name = "history_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Quizhistory historyId;

    public Quizanswer() {
    }

    public Quizanswer(Integer id) {
        this.id = id;
    }

    public Quizanswer(Integer id, int position) {
        this.id = id;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public Quizhistory getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Quizhistory historyId) {
        this.historyId = historyId;
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
        if (!(object instanceof Quizanswer)) {
            return false;
        }
        Quizanswer other = (Quizanswer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.Quizanswer[ id=" + id + " ]";
    }
    
}
