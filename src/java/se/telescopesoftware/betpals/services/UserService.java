package se.telescopesoftware.betpals.services;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetailsService;

import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.FacebookUser;
import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.domain.PasswordRecoveryRequest;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.domain.UserRequestType;
import se.telescopesoftware.betpals.domain.UserSearchForm;


public interface UserService extends UserDetailsService {

    Long registerUser(User user);
    
    Long registerFacebookUser(FacebookUser facebookUser);
    
    void updateUserProfile(UserProfile userProfile);

    Collection<User> getAllUsers(User user, Integer lastLog, Integer lastReg);

    Collection<UserProfile> getAllUserProfiles(User user, Integer lastLog, Integer lastReg);

    void updateUser(User user);

    void deleteUserByUserId(Long userId);

    void blockUser(Long userId);

    boolean isUsernameExists(String username);

    User getUserByUserId(Long userId);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    UserProfile getUserProfileByUserId(Long userId);

    UserProfile getUserProfile(Long id);
    
    Collection<UserProfile> getUserFriends(Long userId);

    Collection<UserProfile> searchUserProfiles(String query, Long userId);
    
    Collection<User> searchUsers(UserSearchForm searchForm);
    
    Collection<User> getUsersByRole(String userRole);
    
    void sendUserRequest(UserRequest userRequest);

    void deleteUserRequest(Long requestId);

    UserRequest getUserRequestById(Long requestId);
    
    Collection<UserRequest> getAllUserRequestsForUser(Long userId);

    Collection<UserRequest> getUserRequestsForUserByType(Long userId, UserRequestType requestType);
    
    Collection<UserRequest> getAllUserRequestsByUser(Long userId);

    Collection<UserRequest> getUserRequestsByUserByType(Long userId, UserRequestType requestType);

    Integer getUserRequestForUserCount(Long userId);
    
    void saveGroup(Group group);
    
    Group getGroupById(Long groupId);
    
    Collection<Group> getUserGroups(Long userId);
    
    void deleteGroup(Long groupId, Long userId);
    
    Community saveCommunity(Community community);
    
    Community getCommunityById(Long communityId);
    
    Collection<Community> getUserCommunities(Long userId);

    Collection<Community> searchCommunities(String query);
    
    void deleteCommunity(Long communityId, Long userId);
    
    void registerPasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest);
    
    PasswordRecoveryRequest findPasswordRecoveryRequest(String requestHash);

    void deletePasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest);
}
