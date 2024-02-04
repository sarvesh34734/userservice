package dev.sarvesh.userservice.models;

import dev.sarvesh.userservice.dtos.SessionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Session extends BaseModel{

    private String token;

    private SessionStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
