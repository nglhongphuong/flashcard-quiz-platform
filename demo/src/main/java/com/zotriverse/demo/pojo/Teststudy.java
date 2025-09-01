/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zotriverse.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "teststudy")

@NamedQueries({
    @NamedQuery(name = "Teststudy.findAll", query = "SELECT t FROM Teststudy t"),
    @NamedQuery(name = "Teststudy.findByQuizId", query = "SELECT t FROM Teststudy t WHERE t.quizId = :quizId"),
    @NamedQuery(name = "Teststudy.findByMin", query = "SELECT t FROM Teststudy t WHERE t.min = :min")})
@AllArgsConstructor
@Builder
public class Teststudy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "quiz_id")
    private Integer quizId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "min")
    private int min;
    @JoinColumn(name = "quiz_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    @JsonIgnore
    @MapsId
    private Quizstudy quizstudy;

    public Teststudy() {
    }

    public Teststudy(Integer quizId) {
        this.quizId = quizId;
    }

    public Teststudy(Integer quizId, int min) {
        this.quizId = quizId;
        this.min = min;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public Quizstudy getQuizstudy() {
        return quizstudy;
    }

    public void setQuizstudy(Quizstudy quizstudy) {
        this.quizstudy = quizstudy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (quizId != null ? quizId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Teststudy)) {
            return false;
        }
        Teststudy other = (Teststudy) object;
        if ((this.quizId == null && other.quizId != null) || (this.quizId != null && !this.quizId.equals(other.quizId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.Teststudy[ quizId=" + quizId + " ]";
    }
    
}
