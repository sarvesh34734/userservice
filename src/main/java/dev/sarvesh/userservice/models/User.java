package dev.sarvesh.userservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@JsonDeserialize(as=User.class)
public class User extends BaseModel{

    private String name;

    private String email;

    private String encpass;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Session> sessions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();


}
