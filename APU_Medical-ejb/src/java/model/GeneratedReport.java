/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Eronaeeell
 */
@Entity
public class GeneratedReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;
    private String name;
    private String date;
    private String type;
    private String status;
    private String size;
    
    @Column(name = "DATEFROM")  // Map to actual database column name
    private String dateFrom;
    
    @Column(name = "DATETO")    // Map to actual database column name
    private String dateTo;
    
    private String summary1;
    private String summary2;
    private String summary3;
    private String summary4;
    private String summary5;

    // Default constructor
    public GeneratedReport() {
    }

    // Constructor with all parameters
    public GeneratedReport(String name, String date, String type, String status, String size, 
                          String dateFrom, String dateTo, String summary1, String summary2, 
                          String summary3, String summary4, String summary5) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.status = status;
        this.size = size;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.summary1 = summary1;
        this.summary2 = summary2;
        this.summary3 = summary3;
        this.summary4 = summary4;
        this.summary5 = summary5;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getSummary1() {
        return summary1;
    }

    public void setSummary1(String summary1) {
        this.summary1 = summary1;
    }

    public String getSummary2() {
        return summary2;
    }

    public void setSummary2(String summary2) {
        this.summary2 = summary2;
    }

    public String getSummary3() {
        return summary3;
    }

    public void setSummary3(String summary3) {
        this.summary3 = summary3;
    }

    public String getSummary4() {
        return summary4;
    }

    public void setSummary4(String summary4) {
        this.summary4 = summary4;
    }

    public String getSummary5() {
        return summary5;
    }

    public void setSummary5(String summary5) {
        this.summary5 = summary5;
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