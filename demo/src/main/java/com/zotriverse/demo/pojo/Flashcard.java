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
@Table(name = "flashcard")

@NamedQueries({
    @NamedQuery(name = "Flashcard.findAll", query = "SELECT f FROM Flashcard f"),
    @NamedQuery(name = "Flashcard.findById", query = "SELECT f FROM Flashcard f WHERE f.id = :id"),
    @NamedQuery(name = "Flashcard.findByImage", query = "SELECT f FROM Flashcard f WHERE f.image = :image"),
    @NamedQuery(name = "Flashcard.findByCreatedAt", query = "SELECT f FROM Flashcard f WHERE f.createdAt = :createdAt"),
    @NamedQuery(name = "Flashcard.findByUpdateAt", query = "SELECT f FROM Flashcard f WHERE f.updateAt = :updateAt")})
@AllArgsConstructor
@Builder
public class Flashcard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "word")
    private String word;
    @Basic(optional = false)
    @NotNull
//    @Lob
//    @Size(min = 1, max = 65535)
//    @Column(name = "definition")
    @Lob
    @Column(name = "definition", columnDefinition = "LONGTEXT")
    private String definition;
    @Size(max = 500)
    @Column(name = "image")
    private String image;
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flashcard")
    @JsonIgnore
    private Set<FlashcardStudy> flashcardStudySet;
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Lesson lessonId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flashcardId")
    @JsonIgnore
    private Set<Quizhistory> quizhistorySet;

    public Flashcard() {
    }

    public Flashcard(Integer id) {
        this.id = id;
    }

    public Flashcard(Integer id, String word, String definition) {
        this.id = id;
        this.word = word;
        this.definition = definition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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


    public Set<FlashcardStudy> getFlashcardStudySet() {
        return flashcardStudySet;
    }

    public void setFlashcardStudySet(Set<FlashcardStudy> flashcardStudySet) {
        this.flashcardStudySet = flashcardStudySet;
    }

    public Lesson getLessonId() {
        return lessonId;
    }

    public void setLessonId(Lesson lessonId) {
        this.lessonId = lessonId;
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
        if (!(object instanceof Flashcard)) {
            return false;
        }
        Flashcard other = (Flashcard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zotriverse.demo.pojo.Flashcard[ id=" + id + " ]";
    }
    
}
