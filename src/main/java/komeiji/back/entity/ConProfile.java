package komeiji.back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ConProfile {
    @Id
    private String userName;

    private String honor;
    private String education;

}
