package com.wecook.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name = User.GET_ALL,
                query = "FROM User"
        ),
        @org.hibernate.annotations.NamedQuery(
                name = User.GET_BY_EMAIL,
                query = "FROM User AS u WHERE u.email = :email"
        ),
        @org.hibernate.annotations.NamedQuery(
                name = User.GET_BY_USERNAME,
                query = "FROM User AS u WHERE u.username = :username"
        )
})
public abstract class User {
    public static final String GET_ALL = "User_GetAll";
    public static final String GET_BY_EMAIL = "User_GetByEmail";
    public static final String GET_BY_USERNAME = "User_GetByUsername";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @Column(name="password", nullable = false, length = 256)
    private String password;

    // TODO Add profile picture

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
