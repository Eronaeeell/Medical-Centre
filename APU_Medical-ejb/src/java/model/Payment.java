/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
public class Payment implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;
    private double amount;
    private String type;
    private String paymentDate;
    private String paymentTime;
    private String status;
    private String method;
    
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Appointment appointment;
    //@ManyToOne
    //private Staff staff;

    public Payment(double amount, String type, String paymentDate, String paymentTime, String status, String method, Customer customer, Appointment appointment) {
        this.amount = amount;
        this.type = type;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.status = status;
        this.method = method;
        this.customer = customer;
        this.appointment = appointment;
        //this.staff = staff;
    }

    

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    

    public Payment() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
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
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Payment[ id=" + id + " ]";
    }

    private CustomerFacade lookupCustomerFacadeBean() {
        try {
            Context c = new InitialContext();
            return (CustomerFacade) c.lookup("java:global/APU_Medical/APU_Medical-ejb/CustomerFacade!model.CustomerFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CustomerFacade lookupCustomerFacadeBean1() {
        try {
            Context c = new InitialContext();
            return (CustomerFacade) c.lookup("java:global/APU_Medical/APU_Medical-ejb/CustomerFacade!model.CustomerFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
