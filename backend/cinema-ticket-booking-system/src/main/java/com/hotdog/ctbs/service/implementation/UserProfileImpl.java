package com.hotdog.ctbs.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.aspectj.bridge.IMessage;
import org.springframework.stereotype.Service;
import com.hotdog.ctbs.service.UserProfileSvc;

import java.util.List;
import java.util.UUID;

/**
 * CRUD for user profiles.
 *
 * @see com.hotdog.ctbs.entity.UserProfile
 */
@Service
public class UserProfileImpl implements UserProfileSvc {

    private final UserProfileRepository userProfileRepository;

    /**
     * @return A title that does not have extra spaces or weird CaSe teXt.
     */
    public String cleanTitleInputAndCapitalize(String dirtyTitle) {
        dirtyTitle = dirtyTitle.strip();
        dirtyTitle.replaceAll("//s+", " ");
        for (String word: dirtyTitle.split(" ")); {
            // capitalize each text.
        }

        return null;
    }

    public UserProfileImpl(UserProfileRepository userProfileRepository)
    {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<String> getAllTitles()
    {
        return userProfileRepository.findAll().stream()
                                    .map(UserProfile::getTitle)
                                    .toList();
    }

    /** @return [ "admin", "owner", "manager", "customer" ] */
    @Override
    public List<String> getAllPrivileges()
    {
        return userProfileRepository.findAll().stream()
                                    .map(UserProfile::getPrivilege)
                                    .distinct()
                                    .toList();
    }

    /** @return [ "admin", "owner", "manager"] */
    @Override
    public List<String> getValidPrivileges()
    {
        return userProfileRepository.findAll().stream()
                                    .map(UserProfile::getPrivilege)
                                    .distinct()
                                    .filter(privilege -> !privilege.equals("customer"))
                                    .toList();
    }

    @Override
    public List<UserProfile> getUserProfilesByPrivilege(final String privilege)
    {
        return userProfileRepository.findAll().stream()
                                    .filter(userProfile -> userProfile.getPrivilege().equals(privilege))
                                    .toList();
    }

    @Override
    public UserProfile getUserProfileById(final UUID id)
    {
        return userProfileRepository.findById(id)
                                    .orElse(null);
    }

    @Override
    public UserProfile getUserProfileByTitle(final String title)
    {
        for (UserProfile userProfile : userProfileRepository.findAll())
            if (userProfile.getTitle().equals(title))
                return userProfile;
        return null;
    }

    @Override
    public UUID getIdByTitle(final String title)
    {
        for (UserProfile userProfile : userProfileRepository.findAll())
            if (userProfile.getTitle().equals(title))
                return userProfile.getId();
        return null;
    }

    @Override
    public void createUserProfile(String privilege, String title)
    {
        for (String s : getAllTitles())
            if (s.equalsIgnoreCase(title))
                throw new IllegalArgumentException("Title already exists.");
        if (privilege.equals("customer"))
            throw new IllegalArgumentException("Reserved privilege.");
        if (!getValidPrivileges().contains(privilege))
            throw new IllegalArgumentException("Invalid privilege.");
        if (!title.matches("^[a-zA-Z ]+$"))
            throw new IllegalArgumentException("Title must contain only letters and spaces.");

        // Remove internal spaces, newlines, and tabs with a single space.
        // For future consideration, this method can Capitalize the string, or lowercase it.
        title = title.strip().replaceAll("\\s+", " ");

        // "   Senior     Admin    "
        // title.strip()
        // "Senior     Admin"
        // .replaceAll( , )
        // "Senior Admin"

        userProfileRepository.save(
                UserProfile.builder()
                           .id(UUID.randomUUID())
                           .privilege(privilege)
                           .title(title)
                           .build()
        );
    }

    @Override
    public void updateOneTitle(String targetTitle, String newTitle)
    {
        if (targetTitle.equals("Customer"))
            throw new IllegalArgumentException("Cannot modify Customer.");
        for (UserProfile userProfile : userProfileRepository.findAll()) {
            if (!userProfile.getTitle().equals(targetTitle))
                continue;

            if (!newTitle.matches("^[a-zA-Z ]+$"))
                throw new IllegalArgumentException("Title must contain only letters and spaces.");

            newTitle = newTitle.strip().replaceAll("\\s+", " ");

            userProfile.setTitle(newTitle);
            userProfileRepository.save(userProfile);
            break;
        }
    }

    // Chief Information Officer (CIO): Owner
    // Chief Information Officer (CIO): Admin

    /**
     * @param targetTitle  the title of the user profile to be modified.
     * @param newPrivilege the new privilege.
     */
    @Override
    public void updateOnePrivilege(String targetTitle, String newPrivilege)
    {
        if (targetTitle.equals("Customer"))
            throw new IllegalArgumentException("Cannot modify Customer.");
        for (UserProfile userProfile : userProfileRepository.findAll()) {
            if (!userProfile.getTitle().equals(targetTitle))
                continue;

            newPrivilege = newPrivilege.strip().replaceAll("\\s+", " ");

            if (!newPrivilege.matches("^[a-zA-Z ]+$"))
                throw new IllegalArgumentException("Privilege must contain only letters and spaces.");
            userProfile.setPrivilege(newPrivilege);
            userProfileRepository.save(userProfile);
            break;
        }
    }

    @Override
    public String deleteByTitle(String title)
    {
        // Check if JSON. this logic should be elsewhere.
//        if (title.stripLeading().startsWith("{") && title.stripTrailing().endsWith("}") && title.contains(":")) {
//            try {
//                deleteByTitle(new ObjectMapper().readTree(title).get("title").asText());
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }

        // TODO: check if this any user_account is using this title:

        for (UserProfile userProfile : userProfileRepository.findAll())
            if (userProfile.getTitle().equals(title)) {
                String message;
                int size = userProfile.getUserAccounts().size();
                if (size > 0) {
                     message = "Deleted user profile belonging to " + size + " accounts.";
                }
                else {
                    message = "Deleted user profile.";
                }
                userProfileRepository.delete(userProfile);
                return message;
            }
        return "Not found";
    }

    @Override
    public UserProfile userProfileFromJSON(String json)
    {
        try {
            return new ObjectMapper().readValue(json, UserProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserProfile> userProfilesFromJSON(String json) throws JsonProcessingException
    {
        return List.of(new ObjectMapper().readValue(json, UserProfile[].class));
    }

    @Override
    public String userProfileToJSON(UserProfile userProfile) throws JsonProcessingException
    {
        return new ObjectMapper().writeValueAsString(userProfile);
    }

    @Override
    public String userProfilesToJSON(List<UserProfile> userProfiles) throws JsonProcessingException
    {
        return new ObjectMapper().writeValueAsString(userProfiles);
    }

    @Override
    public String userProfileOmitIdToJSON(UserProfile userProfile) throws JsonProcessingException
    {
        ObjectMapper om = new ObjectMapper();
        JsonNode jn = om.readTree(om.writeValueAsString(userProfile));
        ((ObjectNode) jn).remove("id");
        return jn.toString();
    }

    @Override
    public String userProfilesOmitIdToJSON(List<UserProfile> userProfiles) throws JsonProcessingException
    {
        StringBuilder sb = new StringBuilder("[");
        for (UserProfile userProfile : userProfiles)
            sb.append(userProfileOmitIdToJSON(userProfile))
              .append(",");

        return sb.deleteCharAt(sb.length() - 1)
                 .append("]")
                 .toString();
    }

    @Override
    public String userProfilesOmitIdToJSON() throws JsonProcessingException
    {
        return userProfilesOmitIdToJSON(userProfileRepository.findAll());
    }

    @Override
    public UserProfile userProfileOmitIdFromJSON(String json) throws JsonProcessingException
    {
        return getUserProfileByTitle(
                new ObjectMapper().readValue(json, UserProfile.class).getTitle()
        );
    }

    @Override
    public List<UserProfile> userProfilesOmitIdFromJSON(String json) throws JsonProcessingException
    {
        return userProfilesFromJSON(json).stream()
                                         .map(userProfile -> getUserProfileByTitle(userProfile.getTitle()))
                                         .toList();
    }
}
