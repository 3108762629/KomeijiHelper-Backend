package komeiji.back.repository;


import komeiji.back.entity.ConProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ConProfileDao extends JpaRepository<ConProfile,Long> {
    ConProfile findByUserName(String userName);

    @Modifying
    @Transactional
    @Query("update ConProfile p set p.honor =?2, p.education =?3 where p.userName =?1")
    void updateHonor(String userName,String honor, String education);
}
