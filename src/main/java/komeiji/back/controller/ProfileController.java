package komeiji.back.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import komeiji.back.entity.ConProfile;
import komeiji.back.entity.User;
import komeiji.back.entity.UserClass;
import komeiji.back.repository.UserDao;
import komeiji.back.service.ProfileService;
import komeiji.back.utils.Result;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Resource
    private ProfileService profileService;
    @Resource
    private UserDao userDao;

    @GetMapping("/queryConProfile")
    public ConProfile queryConProfile(@RequestParam String userName,HttpSession session) {
        return profileService.getProfile(userName);
    }

    @GetMapping("/consultantProfile")
    public ConProfile getProfile(HttpSession session){
        String userName = (String) session.getAttribute("LoginUser");
        User user =  userDao.findByUserName(userName);

        if(user.getUserClass() != UserClass.Assistant && user.getUserClass() != UserClass.Supervisor)
            return null;
        return profileService.getProfile(userName);
    }

    @PostMapping("/updateProfile")
    public Result<String> updateProfile(@RequestBody ConProfile profile, HttpSession session){
        if(profile!= null && !profile.getUserName().equals(session.getAttribute("LoginUser")))
            return Result.error("-1","用户不匹配");

        profileService.updateProfile(profile);

        return Result.success();

    }

}

