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
@Table(name = "quizhistory")

@NamedQueries({
    @NamedQuery(name = "Quizhistory.findAll", query = "SELECT q FROM Quizhistory q"),
    @NamedQuery(name = "Quizhistory.findById", query = "SELECT q FROM Quizhistory q WHERE q.id = :id"),
    @NamedQuery(name = "Quizhistory.findByQuestionType", query = "SELECT q FROM Quizhistory q WHERE q.questionType = :questionType"),
    @NamedQuery(name = "Quizhistory.findByAnswerType", query = "SELECT q FROM Quizhistory q WHERE q.answerType = :answerType"),
    @NamedQuery(name = "Quizhistory.findByResult", query = "SELECT q FROM Quizhistory q WHERE q.result = :result"),
    @NamedQuery(name = "Quizhistory.findByCreatedAt", query = "SELECT q FROM Quizhistory q WHERE q.createdAt = :createdAt"),
    @NamedQuery(name = "Quizhistory.findByUpdateAt", query = "SELECT q FROM Quizhistory q WHERE q.updateAt = :updateAt")})
@AllArgsConstructor
@Builder
public class Quizhistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "explanation")
    private String explanation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "question_type")
    private String questionType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "answer_type")
    private String answerType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "result")
    private String result;
    @Lob
    @Size(max = 65535)
    @Column(name = "user_answer")
    private String userAnswer;
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;
    @JoinColumn(name = "flashcard_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Flashcard flashcardId;
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Quizstudy quizId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historyId")
    @JsonIgnore
    private Set<Quizanswer> quizanswerSet;

    public Quizhistory() {
    }

    public Quizhistory(Integer id) {
        this.id = id;
    }

    public Quizhistory(Integer id, String questionType, String answerType, String result) {
        this.id = id;
        this.questionType = questionType;
        this.answerType = answerType;
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
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

    public Flashcard getFlashcardId() {
        return flashcardId;
    }

    public void setFlashcardId(Flashcard flashcardId) {
        this.flashcardId = flashcardId;
    }

    public Quizstudy getQuizId() {
        return quizId;
    }

    public void setQuizId(Quizstudy quizId) {
        this.quizId = quizId;
    }


    public Set<Quizanswer> getQuizanswerSet() {
        return quizanswerSet;
    }

    public void setQuizanswerSet(Set<Quizanswer> quizanswerSet) {
        this.quizanswerSet = quizanswerSet;
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
        if (!(object instanceof Quizhistory)) {
            return false;
        }
        Quizhistory other = (Quizhistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.Quizhistory[ id=" + id + " ]";
    }
    
}
