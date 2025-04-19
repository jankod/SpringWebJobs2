package hr.bioinfo.swj.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_username", columnList = "username", unique = true),
        @Index(name = "idx_user_email", columnList = "email", unique = true)
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AbstractPersistable<Long> {

    @Column(nullable = false, unique = true, length = 50)
    String username;

    @Column(nullable = false, length = 100)
    String password;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Column(length = 100)
    String firstName;

    @Column(length = 100)
    String lastName;

    @Column(nullable = false)
    boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }
}