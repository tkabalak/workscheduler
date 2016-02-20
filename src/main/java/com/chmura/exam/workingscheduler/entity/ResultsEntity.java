package com.chmura.exam.workingscheduler.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "rlzt")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResultsEntity.findAll", query = "SELECT r FROM ResultsEntity r"),
    @NamedQuery(name = "ResultsEntity.findByIdRealzation", query = "SELECT r FROM ResultsEntity r WHERE r.idRealzation = :idRealzation"),
    @NamedQuery(name = "ResultsEntity.findByPercentage", query = "SELECT r FROM ResultsEntity r WHERE r.percentage = :percentage")})
public class ResultsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_realzation")
    private Integer idRealzation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "percentage")
    private BigDecimal percentage;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "task_id", referencedColumnName = "id_task")
    @ManyToOne(fetch = FetchType.LAZY)
    private Task taskId;

    public ResultsEntity() {
    }

    public ResultsEntity(Integer idRealzation) {
        this.idRealzation = idRealzation;
    }

    public ResultsEntity(Integer idRealzation, BigDecimal percentage, String description) {
        this.idRealzation = idRealzation;
        this.percentage = percentage;
        this.description = description;
    }

    public Integer getIdRealzation() {
        return idRealzation;
    }

    public void setIdRealzation(Integer idRealzation) {
        this.idRealzation = idRealzation;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task getTaskId() {
        return taskId;
    }

    public void setTaskId(Task taskId) {
        this.taskId = taskId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRealzation != null ? idRealzation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResultsEntity)) {
            return false;
        }
        ResultsEntity other = (ResultsEntity) object;
        if ((this.idRealzation == null && other.idRealzation != null) || (this.idRealzation != null && !this.idRealzation.equals(other.idRealzation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.chmura.exam.workingscheduler.entity.ResultsEntity[ idRealzation=" + idRealzation + " ]";
    }

}
