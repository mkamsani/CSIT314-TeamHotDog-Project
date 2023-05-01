As a user admin, I want to be able to create user profile so that I allocate user profile to user account.

Taiga ID: [#11](https://tree.taiga.io/project/isaacsimstudy-sim2023q2-hotdogbun-1/us/11)

## BCE Diagram

```mermaid
classDiagram
class CreateUserProfilePage {
    <<Boundary>>
    onClickCreate(String json)
}
class CreateUserProfileController {
    <<Controller>>
    parse(String json)
    createUserProfile(String privilege, String title)
}
class UserProfileService {
    <<Entity>>
    createUserProfile(String privilege, String title)
}
class UserProfile {
    <<Entity>>
    - UUID id;
    - String privilege;
    - String title;
    + createUserProfile(String privilege, String title)
}
CreateUserProfilePage -- CreateUserProfileController
CreateUserProfileController -- UserProfileService
UserProfileService -- UserProfile
```

## Sequence Diagram

### Default flow

```mermaid
sequenceDiagram
actor Admin
Admin ->> CreateUserProfilePage : create user profile
activate CreateUserProfilePage
CreateUserProfilePage ->> CreateUserAccountController : onClickCreate(String json)
activate CreateUserAccountController
CreateUserAccountController ->> UserAccountEntity : create(String json)
```

### Alternate flows: Invalid information

```mermaid
sequenceDiagram
actor Admin
```
