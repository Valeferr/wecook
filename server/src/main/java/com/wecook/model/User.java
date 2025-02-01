package com.wecook.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name = User.GET_ALL,
                query = "FROM User"
        ),
        @org.hibernate.annotations.NamedQuery(
                name = User.GET_BY_EMAIL,
                query = "FROM User AS u WHERE u.email = :email"
        )
})
public class User implements Serializable {
    public enum Roles {
        STANDARD,
        MODERATOR
    }

    public static final String GET_ALL = "User_GetAll";
    public static final String GET_BY_EMAIL = "User_GetByEmail";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int id;

    @Column(name = "email", nullable = false, unique = true)
    @Expose
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    @Expose
    private String username;

    @Column(name = "password", nullable = false, length = 256)
    @Expose(serialize = false, deserialize = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Expose
    private Roles role;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "MEDIUMBLOB")
    private byte[] profilePicture;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Report> reports = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> users) {
        this.reports = users;
    }
}
