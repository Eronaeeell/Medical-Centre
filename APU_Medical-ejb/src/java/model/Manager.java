package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Manager entity mapped to APP.MANAGER
 */
@Entity
@Table(name = "MANAGER", schema = "APP")
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "EMAIL", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", length = 255, nullable = false)
    private String password;

    @Column(name = "PHONE", length = 30)
    private String phone;

    @Column(name = "ROLE", length = 50)
    private String role;

    @Column(name = "DEPARTMENT", length = 100)
    private String department;

    @Column(name = "STATUS", length = 20)
    private String status;

    // You stored this as a VARCHAR in DB; keep as String for now to avoid migration work.
    @Column(name = "JOINDATE", length = 20)
    private String joinDate;

    public Manager() { }

    public Manager(String name, String email, String password, String phone, String role,
                   String department, String status, String joinDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.department = department;
        this.status = status;
        this.joinDate = joinDate;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getJoinDate() { return joinDate; }
    public void setJoinDate(String joinDate) { this.joinDate = joinDate; }

    @Override
    public int hashCode() { return (id != null ? id.hashCode() : 0); }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Manager)) return false;
        Manager other = (Manager) object;
        if ((this.id == null && other.id != null) ||
            (this.id != null && !this.id.equals(other.id))) return false;
        return true;
    }

    @Override
    public String toString() { return "model.Manager[ id=" + id + " ]"; }
}
