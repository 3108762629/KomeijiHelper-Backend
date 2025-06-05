package komeiji.back.service;

import komeiji.back.entity.ConProfile;

public interface ProfileService {
    ConProfile getProfile(String  userName);
    void updateProfile(ConProfile profile);

}
