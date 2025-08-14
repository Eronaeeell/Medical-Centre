/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Jee
 */
@Entity
public class GeneratedReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;
    private String reportType;
    private String generatedDate;
    private String reportContent;
    private String status;
    
    @ManyToOne
    private Staff generatedBy;
    
    public GeneratedReport() {
    }
    
    public GeneratedReport(String reportType, String generatedDate, String reportContent, String status, Staff generatedBy) {
        this.reportType = reportType;
        this.generatedDate = generatedDate;
        this.reportContent = reportContent;
        this.status = status;
        this.generatedBy = generatedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Staff getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(Staff generatedBy) {
        this.generatedBy = generatedBy;
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
        if (!(object instanceof GeneratedReport)) {
            return false;
        }
        GeneratedReport other = (GeneratedReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.GeneratedReport[ id=" + id + " ]";
    }
    
}