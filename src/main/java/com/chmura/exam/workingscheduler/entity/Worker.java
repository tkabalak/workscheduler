package com.chmura.exam.workingscheduler.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "worker")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Worker.findAll", query = "SELECT w FROM Worker w"),
    @NamedQuery(name = "Worker.findById", query = "SELECT w FROM Worker w WHERE w.id = :id"),
    @NamedQuery(name = "Worker.findByName", query = "SELECT w FROM Worker w WHERE w.name = :name"),
    @NamedQuery(name = "Worker.findBySurname", query = "SELECT w FROM Worker w WHERE w.surname = :surname"),
    @NamedQuery(name = "Worker.findByPesel", query = "SELECT w FROM Worker w WHERE w.pesel = :pesel"),
    @NamedQuery(name = "Worker.findByPosition", query = "SELECT w FROM Worker w WHERE w.position = :position")})
public class Worker implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "pesel")
    private String pesel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "position")
    private int position;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workerId", fetch = FetchType.LAZY)
    private Collection<Task> taskCollection;

    public Worker() {
    }

    public Worker(Integer id) {
        this.id = id;
    }

    public Worker(Integer id, String name, String surname, String pesel, int position) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @XmlTransient
    public Collection<Task> getTaskCollection() {
        return taskCollection;
    }

    public void setTaskCollection(Collection<Task> taskCollection) {
        this.taskCollection = taskCollection;
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
        if (!(object instanceof Worker)) {
            return false;
        }
        Worker other = (Worker) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.chmura.exam.workingscheduler.entity.Worker[ id=" + id + " ]";
    }

}
