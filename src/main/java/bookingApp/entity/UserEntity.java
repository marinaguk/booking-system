package bookingApp.entity;


import bookingApp.model.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    public UserEntity(String name, String password) {
        this.name = name;
        this.password = password;
        this.role = Role.USER;
    }

    public UserEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PropertyEntity> properties = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (!(obj instanceof UserEntity)) return false;

        return id == ((UserEntity) obj).getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
