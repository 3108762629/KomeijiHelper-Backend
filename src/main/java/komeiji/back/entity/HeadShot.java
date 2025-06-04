package komeiji.back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class HeadShot {

    @Id
    private String userName;
    private String headShotUrl;

    public HeadShot(){}
    public HeadShot(String userName, String headShotUrl) {
        this.userName = userName;
        this.headShotUrl = headShotUrl;
    }

}
