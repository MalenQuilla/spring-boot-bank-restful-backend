package malenquillaa.java.spring.bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Date;
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
    @Size(min = 3, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 50)
    private String lastName;

    @NotNull
    private Date DOB;

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

    public User(String firstName, String lastName, Date DOB, String username, String email, String password, EStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.DOB = DOB;
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    @JsonIgnore
    public boolean isAdmin() {
        for (Role role : roles) {
            if (role.getName().equals(ERole.ROLE_ADMIN)) {
                return true;
            }
        }
        return false;
    }
}
