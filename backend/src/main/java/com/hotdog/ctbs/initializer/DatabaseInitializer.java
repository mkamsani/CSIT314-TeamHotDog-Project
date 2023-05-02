package com.hotdog.ctbs.initializer;

import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    final UserProfileRepository userProfileRepository;
    final UserAccountRepository userAccountRepository;
    final Faker faker = new Faker();

    public DatabaseInitializer(UserProfileRepository userProfileRepository, UserAccountRepository userAccountRepository)
    {
        this.userProfileRepository = userProfileRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        if (userProfileRepository.count() == 0) {
            Arrays.stream(new String[][]{
                    {"customer", "customer"},
                    {"manager", "junior manager"},
                    {"manager", "senior manager"},
                    {"owner", "chief financial officer"},
                    {"owner", "chief executive officer"},
                    {"admin", "junior admin"},
                    {"admin", "senior admin"},
                    {"admin", "chief information officer"}
            }).forEach(s -> userProfileRepository.save(
                    UserProfile.builder()
                               .privilege(s[0])
                               .title(s[1])
                               .isActive(true)
                               .build()
            ));
        }
        if (userAccountRepository.count() == 0) {
            Arrays.stream(new String[][]{
                    {"password_Mgr_is_mgrJ", "jim", "Jim", "Halpert", "1979-10-20", "junior manager"},
                    {"password_Mgr_is_mgrS", "mscott", "Michael", "Scott", "1962-08-16", "senior manager"},
                    {"password_Owr_is_%CFO", "dwallace", "David", "Wallace", "1965-02-13", "chief financial officer"},
                    {"password_Owr_is_%CEO", "jbennett", "Joleen", "Bennett", "1948-06-28", "chief executive officer"},
                    {"password_Adm_is_admJ", "marcus", "Marcus", "Hutchins", "1994-01-01", "junior admin"},
                    {"password_Adm_is_admS", "samy", "Samy", "Kamkar", "1985-12-10", "senior admin"},
                    {"password_Adm_is_%CIO", "stonebraker", "Michael", "Stonebraker", "1943-10-11", "chief information officer"}
            }).forEach(userAccount -> userAccountRepository.save(
                    UserAccount.builder()
                               .passwordHash(userAccount[0])
                               .username(userAccount[1])
                               .email(userAccount[1] + "@ctbs.com")
                               .firstName(userAccount[2])
                               .lastName(userAccount[3])
                               .address(faker.address().fullAddress())
                               .dateOfBirth(LocalDate.parse(userAccount[4]))
                               .userProfile(userProfileRepository.findUserProfileByTitle(userAccount[5]))
                               .timeCreated(OffsetDateTime.now())
                               .timeLastLogin(OffsetDateTime.now())
                               .isActive(true)
                               .build()
            ));
            for (int i = 0; i < 100; i++) {
                String firstName = faker.name().firstName();
                UserAccount userAccount = UserAccount.builder()
                        .id(UUID.randomUUID())
                                                     .passwordHash("password_" + i)
                                                     .username("user_" + i)
                                                     .email(faker.internet().emailAddress())
                                                     .firstName(firstName)
                                                     .lastName(faker.name().lastName())
                                                     .address(faker.address().fullAddress())
                                                     .dateOfBirth(faker.date()
                                                                       .birthday()
                                                                       .toLocalDateTime()
                                                                       .toLocalDate())
                                                     .userProfile(userProfileRepository.findUserProfileByTitle(
                                                             "customer"))
                                                     .timeCreated(OffsetDateTime.now())
                                                     .timeLastLogin(OffsetDateTime.now())
                                                     .isActive(true)
                                                     .build();
                if (userAccountRepository.findAll().contains(userAccount)) {
                    i--;
                    continue;
                }
                userAccountRepository.save(
                        userAccount
                );
            }
        }
    }
}
