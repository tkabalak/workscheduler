package com.chmura.exam.workingscheduler.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
    @NamedQuery(name = "Task.findByIdTask", query = "SELECT t FROM Task t WHERE t.idTask = :idTask"),
    @NamedQuery(name = "Task.findByTitle", query = "SELECT t FROM Task t WHERE t.title = :title"),
    @NamedQuery(name = "Task.findByADate", query = "SELECT t FROM Task t WHERE t.aDate = :aDate"),
    @NamedQuery(name = "Task.findByEDate", query = "SELECT t FROM Task t WHERE t.eDate = :eDate")})
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_task")
    private Integer idTask;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "a_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "e_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eDate;
    @OneToMany(mappedBy = "taskId", fetch = FetchType.LAZY)
    private Collection<ResultsEntity> resultsEntityCollection;
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Worker workerId;

    public Task() {
    }

    public Task(Integer idTask) {
        this.idTask = idTask;
    }

    public Task(Integer idTask, String title, String description, Date aDate, Date eDate) {
        this.idTask = idTask;
        this.title = title;
        this.description = description;
        this.aDate = aDate;
        this.eDate = eDate;
    }

    public Integer getIdTask() {
        return idTask;
    }

    public void setIdTask(Integer idTask) {
        this.idTask = idTask;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getADate() {
        return aDate;
    }

    public void setADate(Date aDate) {
        this.aDate = aDate;
    }

    public Date getEDate() {
        return eDate;
    }

    public void setEDate(Date eDate) {
        this.eDate = eDate;
    }

    @XmlTransient
    public Collection<ResultsEntity> getResultsEntityCollection() {
        return resultsEntityCollection;
    }

    public void setResultsEntityCollection(Collection<ResultsEntity> resultsEntityCollection) {
        this.resultsEntityCollection = resultsEntityCollection;
    }

    public Worker getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Worker workerId) {
        this.workerId = workerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTask != null ? idTask.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.idTask == null && other.idTask != null) || (this.idTask != null && !this.idTask.equals(other.idTask))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.chmura.exam.workingscheduler.entity.Task[ idTask=" + idTask + " ]";
    }

}
