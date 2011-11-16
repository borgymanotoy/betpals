package se.telescopesoftware.betpals.repository;


import java.util.Collection;
import java.util.Date;

import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.domain.PasswordRecoveryRequest;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserLogEntry;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.domain.UserRequestType;

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

    UserProfile loadUserProfileByUserId(Long userId);

    UserProfile loadUserProfile(Long id);

    Integer getRegistrationCountByPeriod(Integer period);

    Integer getRegistrationCountForDate(Date date);

    Integer getLoginCountByPeriod(Integer period);

    Integer getLoginCountForDate(Date date);

    Integer getTotalCount();

    Collection<User> findUsersByNameOrSurname(String query);

    Collection<User> findUsersByEmail(String query);
    
    Collection<User> findUsersByRole(String userRole);
    
    Collection<UserProfile> findUserProfiles(String query);
    
    UserProfile findUserProfilesById(Long userId);
    
    void storeUserRequest(UserRequest userRequest);

    void deleteUserRequest(UserRequest userRequest);
    
    UserRequest loadUserRequestById(Long id);

    Collection<UserRequest> loadUserRequestsForUser(Long userId, UserRequestType requestType);

    Collection<UserRequest> loadUserRequestsByUser(Long userId, UserRequestType requestType);
    
    Integer getUserRequestForUserCount(Long userId);

    void storeGroup(Group group);
    
    Group loadGroupById(Long groupId);
    
    Collection<Group> loadUserGroups(Long userId);
    
    void deleteGroup(Group group);

    Community storeCommunity(Community community);
    
    Community loadCommunityById(Long communityId);
    
    Collection<Community> loadUserCommunities(Long userId);

    Collection<Community> findCommunities(String query);
    
    void deleteCommunity(Community community);

    void storePasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest);
    
    PasswordRecoveryRequest findPasswordRecoveryRequest(String requestHash);

    void deletePasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest);

    void storeUserLogEntry(UserLogEntry userLogEntry);
    
    Collection<UserLogEntry> loadUserLogEntries(Long userId);

}
