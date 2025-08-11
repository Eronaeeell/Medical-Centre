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
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String rateDate;
    private int doctorRating;
    private String overallexperience;
    private int staffRating;
    private String staffFeedback;
    private String doctorFeedback;
    
    @ManyToOne
    private Appointment appointment;
    
    @ManyToOne
    private Doctor doctor;

    public Feedback(String rateDate, int doctorRating, String overallexperience, Appointment appointment, Doctor doctor, int staffRating, String staffFeedback,String doctorFeedback) {
        this.rateDate = rateDate;
        this.doctorRating = doctorRating;
        this.overallexperience = overallexperience;
        this.appointment = appointment;
        this.doctor = doctor;
        this.staffRating = staffRating;
        this.staffFeedback = staffFeedback;
        this.doctorFeedback = doctorFeedback;
    }
    
    public String getRateDate() {
        return rateDate;
    }

    public String getStaffFeedback() {
        return staffFeedback;
    }

    public void setStaffFeedback(String staffFeedback) {
        this.staffFeedback = staffFeedback;
    }

    public String getDoctorFeedback() {
        return doctorFeedback;
    }

    public void setDoctorFeedback(String doctorFeedback) {
        this.doctorFeedback = doctorFeedback;
    }
    

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public int getStaffRating() {
        return staffRating;
    }

    public void setStaffRating(int staffRating) {
        this.staffRating = staffRating;
    }
    
    public int getDoctorRating() {
        return doctorRating;
    }

    public void setDoctorRating(int doctorRating) {
        this.doctorRating = doctorRating;
    }

    public String getOverallexperience() {
        return overallexperience;
    }

    public void setOverallexperience(String overallexperience) {
        this.overallexperience = overallexperience;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public Feedback() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Feedback)) {
            return false;
        }
        Feedback other = (Feedback) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Feedback[ id=" + id + " ]";
    }
    
}
