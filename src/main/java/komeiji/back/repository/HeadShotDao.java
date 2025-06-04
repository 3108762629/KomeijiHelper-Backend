package komeiji.back.repository;

import jakarta.transaction.Transactional;
import komeiji.back.entity.HeadShot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadShotDao extends JpaRepository<HeadShot, Long> {
    HeadShot findByUserName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE HeadShot h SET h.headShotUrl = :url WHERE h.userName = :name")
    int updateHeadShotUrl(String name, String url);

}

