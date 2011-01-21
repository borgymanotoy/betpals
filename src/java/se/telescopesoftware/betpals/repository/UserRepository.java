package se.telescopesoftware.betpals.repository;


import java.util.Collection;
import java.util.Date;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;

public interface UserRepository {

    User loadUserByUsername(String username);

    User loadUserByEmail(String email);

    User loadUserByUserId(Long id);

    void storeUser(User user);

    Long registerUser(User user);

    void deleteUserById(Long userId);

    void updateUserProfile(UserProfile userProfile);

    Collection<User> loadAllUsers(User user, Integer lastLog, Integer lastReg);

    boolean isUsernameExists(String username);

    UserProfile loadUserProfileByUserId(Long id);

    Integer getRegistrationCountByPeriod(Integer period);

    Integer getRegistrationCountForDate(Date date);

    Integer getLoginCountByPeriod(Integer period);

    Integer getLoginCountForDate(Date date);

    Integer getTotalCount();

    Collection<User> findUsersByNameOrSurname(String query);

    Collection<User> findUsersByEmail(String query);
    
    Collection<User> findUsersByRole(String userRole);
    
    Collection<UserProfile> findUserProfiles(String query);
    
    void storeUserRequest(UserRequest userRequest);

    void deleteUserRequest(UserRequest userRequest);
    
    UserRequest loadUserRequestById(Long id);

    Collection<UserRequest> loadUserRequestForUser(Long userId);

    Collection<UserRequest> loadUserRequestByUser(Long userId);
    
    Integer getUserRequestForUserCount(Long userId);

}
