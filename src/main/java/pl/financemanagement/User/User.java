package pl.financemanagement.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String name;
    private Instant createdOn;
    private Instant modifyOn;
    private long version;

    public User() {
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Instant getModifyOn() {
        return modifyOn;
    }

    public long getVersion() {
        return version;
    }
}
