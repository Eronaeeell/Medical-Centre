package model;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class ManagerFacade extends AbstractFacade<Manager> {

    @PersistenceContext(unitName = "APU_Medical-ejbPU")
    private EntityManager em;

    public ManagerFacade() { super(Manager.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    /** Case-insensitive email lookup; returns null if not found */
    public Manager findByEmail(String email) {
        if (email == null) return null;
        List<Manager> list = em.createQuery(
                "SELECT m FROM Manager m WHERE LOWER(m.email) = :email", Manager.class)
            .setParameter("email", email.toLowerCase())
            .setMaxResults(1)
            .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    /** Plain check for now; replace with BCrypt later */
    public boolean verifyPassword(Manager m, String raw) {
        return m != null && raw != null && raw.equals(m.getPassword());
    }

    /** Treat null status as active during development, or enforce ACTIVE explicitly */
    public boolean isActive(Manager m) {
        return m != null && (m.getStatus() == null || "ACTIVE".equalsIgnoreCase(m.getStatus()));
    }
}
