package com.example.NatakaLK.service;

import com.example.NatakaLK.config.CustomOAuth2User;
import com.example.NatakaLK.model.User;
import com.example.NatakaLK.model.enums.RoleType;
import com.example.NatakaLK.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepo.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            String[] nameParts = name != null ? name.split(" ") : new String[]{"User"};
            user.setFname(nameParts[0]);
            user.setLname(nameParts.length > 1 ? nameParts[1] : "");

            user.setStatus("approved");
            user.setRole(RoleType.Customer);
            userRepo.save(user);
        } else {
            boolean updated = false;
            String[] nameParts = name != null ? name.split(" ") : new String[]{user.getFname()};
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            // Update name if changed on Google
            if (!firstName.equals(user.getFname())) {
                user.setFname(firstName);
                updated = true;
            }
            if (!lastName.equals(user.getLname())) {
                user.setLname(lastName);
                updated = true;
            }
            if (updated) {
                userRepo.save(user);
            }
        }

        // 3. RETURN THE WRAPPER (Critical!)
        // If you return 'oAuth2User' here, your SuccessHandler will crash.
        return new CustomOAuth2User(oAuth2User, user);
    }
}