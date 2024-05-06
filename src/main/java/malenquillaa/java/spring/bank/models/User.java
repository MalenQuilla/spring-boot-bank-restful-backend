package malenquillaa.java.spring.bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Data
@Entity
@Table(name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
    })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private Long id;

    @NotNull
    @Size(max = 20)
    private String username;

    @NotNull
    @Size(max = 50)
    @Email
    private String email;

    @NotNull
    @JsonIgnore
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @NotNull
    @JsonIgnore
    private EStatus status;

    public User(String username, String email, String password, EStatus status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    @JsonIgnore
    public boolean isAdmin() {
        Set<Role> roles = this.getRoles();

        for (Role role : roles) {
            if (role.getName().equals(ERole.ROLE_ADMIN)) {
                return true;
            }
        }
        return false;
    }
}
