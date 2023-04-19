package com.hotdog.ctbs.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserProfileRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class CreateUserProfile {

    @Autowired
    private UserProfileRepository userProfileRepository;

    // TODO: remove when services are implemented.
    Faker faker = new Faker();

    @GetMapping
    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    public String jsonPretty(String string) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return "<pre>" +
                objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(string)
                + "</pre>";
    }

    public String jsonRegular(String string) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(string);
    }

    @GetMapping("/test/retrieve/fake")
    public String fakeUserProfile() {
        return new UserProfile(faker).toString();
    }

    @GetMapping("/test/create/fake")
    public String createUserProfile() throws JsonProcessingException {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        UserProfile fake = new UserProfile(faker);
        if (userProfiles.contains(fake)) {
            return "User profile already exists.";
        }
        userProfileRepository.save(fake);
        ObjectMapper om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(userProfiles);
        return "<pre>" + json + "</pre>";
    }

/*
# Generate the string "admin", "owner", or "manager" using posix shell:
uuid=$(uuidgen)
val=$(($RANDOM % 3))
privilege=$(if [ $val -eq 0 ]; then echo "admin"; elif [ $val -eq 1 ]; then echo "admin"; else echo "manager"; fi)
title=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 8 | head -n 1)
json="{\"id\":\"$uuid\",\"privilege\":\"$privilege\",\"title\":\"$title\",\"userAccounts\":[]}"
clear
printf %s\\n $json
curl -X POST -H "Content-Type: application/json" -d $(printf '%s' $json) http://localhost:8080/api/admin/create
unset uuid val priv rand json
printf \\n\\n
*/

    // php axios.post('http://localhost:8080/api/admin/create', {id: '8474c1b2-23e8-4668-aaa8-f90003dbe241', privilege: 'owner', title: 'Chief Branding Architect', userAccounts: []})

    /**
     * @return the user profile.
     * @code curl -X POST -H "Content-Type: application/json" -d '{"id":"8474c1b2-23e8-4668-aaa8-f90003dbe241","privilege":"owner","title":"Chief Branding Architect","userAccounts":[]}' http://localhost:8080/api/admin/create
     */
    @PostMapping("/create")
    public String createUserProfile(@RequestBody String json) {
        UserProfile userProfile = new UserProfile(json);
        if (userProfileRepository.findAll().contains(userProfile)) {
            return "User profile already exists.";
        }
        userProfileRepository.save(userProfile);
        if (userProfileRepository.findAll().contains(userProfile)) {
            return "User profile created.";
        }
        return "User profile not created.";
    }

    @GetMapping("/retrieve/all")
    public String retrieveUserProfilesAll() {
        return userProfileRepository.findAll().toString();
    }

    @GetMapping("/delete/all")
    public boolean dropUserProfile() {
        userProfileRepository.deleteAll();
        return userProfileRepository.findAll().isEmpty();
    }
}