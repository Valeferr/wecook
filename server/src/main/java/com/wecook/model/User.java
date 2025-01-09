package com.wecook.model;

import jakarta.persistence.*;

@Entity
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name = "User_GetAll",
                query = "FROM User"
        )
})
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="username")
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
