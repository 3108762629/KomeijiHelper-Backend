package komeiji.back.service.Impl;

import jakarta.annotation.Resource;
import komeiji.back.entity.ConProfile;
import komeiji.back.repository.ConProfileDao;
import komeiji.back.service.ProfileService;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Resource
    private ConProfileDao conProfileDao;




    @Override
    public ConProfile getProfile(String userName) {
        return conProfileDao.findByUserName(userName);
    }

    @Override
    public void updateProfile(ConProfile profile) {
        if(conProfileDao.findByUserName(profile.getUserName()) == null) {
            conProfileDao.save(profile);
            return;
        }

        conProfileDao.updateHonor(profile.getUserName(), profile.getHonor(), profile.getEducation());
    }
}
