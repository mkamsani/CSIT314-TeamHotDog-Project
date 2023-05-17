package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.UserProfileRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_profile")
@JsonIgnoreProperties({"userAccounts"})
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    protected UUID id;

    @Column(name = "privilege", nullable = false)
    protected String privilege;

    @Column(name = "title", nullable = false)
    protected String title;

    @OneToMany(mappedBy = "userProfile")
    protected Set<UserAccount> userAccounts = new LinkedHashSet<>();

    @Column(name = "is_active", nullable = false)
    protected Boolean isActive;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static void createUserProfile(UserProfileRepository userProfileRepo, String privilege, String title)
    {
        List<UserProfile> userProfiles = userProfileRepo.findAll();
        for (String titles : userProfiles.stream().map(UserProfile::getTitle).toList())
            if (titles.equalsIgnoreCase(title))
                throw new IllegalArgumentException("Title already exists.");
        for (String titles : new String[]{"admin", "owner", "manager", "customer", "titles", "privileges", "active", "all"})
            if (titles.equalsIgnoreCase(title))
                throw new IllegalArgumentException("Reserved title.");

        switch (privilege) {
            case "admin", "owner", "manager":
                break;
            case "customer":
                throw new IllegalArgumentException("Reserved privilege.");
            default:
                throw new IllegalArgumentException("Invalid privilege.");
        }

        if (!title.matches("^[a-zA-Z ]+$"))
            throw new IllegalArgumentException("Title must contain only letters and spaces.");

        // Remove extra spaces.
        title = title.strip().replaceAll("\\s+", " ");

        UserProfile userProfile = new UserProfile();
        userProfile.id = UUID.randomUUID();
        userProfile.privilege = privilege;
        userProfile.title = title;
        userProfile.isActive = true;
        userProfileRepo.save(userProfile);
    }

    public static String readUserProfile(UserProfileRepository userProfileRepo, String param)
    {
        if (param.equals("titles"))
            return userProfileRepo.findAllTitles().toString();
        if (param.equals("privileges"))
            return userProfileRepo.findAllPrivileges().toString();
        List<UserProfile> userProfileList = switch (param) {
            case "admin", "owner", "manager", "customer"
                    -> userProfileRepo.findUserProfilesByPrivilege(param);
            case "active"
                    -> userProfileRepo.findUserProfilesByIsActiveTrue();
            case "all"
                    -> userProfileRepo.findAll();
            default -> {
                UserProfile userProfile = userProfileRepo.findUserProfileByTitle(param).orElse(null);
                if (userProfile == null)
                    throw new IllegalArgumentException("User profile not found.");
                yield List.of(userProfile);
            }
        };
        JsonNode[] jsonNodes = new JsonNode[userProfileList.size()];
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (int i = userProfileList.size() - 1; i >= 0; i--) {
            UserProfile userAccount = userProfileList.get(i);
            jsonNodes[i] = objectMapper.valueToTree(userAccount);
            ((ObjectNode) jsonNodes[i]).remove("id");
            arrayNode.add(jsonNodes[i]);
        }
        return arrayNode.toString();
    }

    public static void updateUserProfile(UserProfileRepository userProfileRepo, String targetTitle, String privilege,
                                         String title)
    {
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(targetTitle).orElse(null);
        if (userProfile == null)
            throw new IllegalArgumentException("User profile not found.");
        if (title.equalsIgnoreCase("customer") ||
            targetTitle.equalsIgnoreCase("customer") ||
            userProfile.privilege.equalsIgnoreCase("customer"))
            throw new IllegalArgumentException("Cannot modify Customer.");

        switch (privilege) {
            case "admin", "owner", "manager":
                break;
            case "customer":
                throw new IllegalArgumentException("Reserved privilege.");
            default:
                throw new IllegalArgumentException("Invalid privilege.");
        }

        if (!title.matches("^[a-zA-Z ]+$"))
            throw new IllegalArgumentException("Title must contain only letters and spaces.");

        title = title.strip().replaceAll("\\s+", " ");

        userProfile.privilege = privilege;
        userProfile.title = title;
        userProfileRepo.save(userProfile);
    }

    public static void suspendUserProfile(UserProfileRepository userProfileRepo, String targetTitle)
    {
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(targetTitle).orElse(null);
        if (targetTitle.equalsIgnoreCase("customer"))
            throw new IllegalArgumentException("Cannot suspend Customer.");
        if (userProfile == null)
            throw new IllegalArgumentException("User profile not found.");
        if (!userProfile.getIsActive())
            throw new IllegalArgumentException("User profile is already suspended.");

        userProfile.setIsActive(false);
        userProfileRepo.save(userProfile);
    }
}