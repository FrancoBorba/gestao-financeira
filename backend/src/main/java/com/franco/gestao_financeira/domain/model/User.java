package com.franco.gestao_financeira.domain.model;


import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "app_user")
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    private String email;

    @Column(name = "nickname" , nullable = false , length = 100)
    private String nickname;

 

    @Column(name = "cycle_start_day")
    private Integer cycleStartDay = 1; // Has day 1 with default

    
  

    public User(Long id, String name, @Email String email, String nickname, Integer cycleStartDay) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.cycleStartDay = cycleStartDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCycleStartDay() {
        return cycleStartDay;
    }

    public void setCycleStartDay(Integer cycleStartDay) {
        this.cycleStartDay = cycleStartDay;
    }

   public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

  
}
