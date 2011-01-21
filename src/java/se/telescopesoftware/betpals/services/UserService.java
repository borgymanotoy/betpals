package se.telescopesoftware.betpals.services;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetailsService;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.domain.UserSearchForm;


public interface UserService extends UserDetailsService {

    Long registerUser(User user);

    void updateUserProfile(UserProfile userProfile);

    Collection<User> getAllUsers(User user, Integer lastLog, Integer lastReg);

    void updateUser(User user);

    void deleteUserByUserId(Long userId);

    void blockUser(Long userId);

    boolean isUsernameExists(String username);

    User getUserByUserId(Long userId);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    UserProfile getUserProfileByUserId(Long id);
    
    Collection<UserProfile> getUserFriends(Long userId);

    Collection<UserProfile> searchUserProfiles(String query, Long userId);
    
    Collection<User> searchUsers(UserSearchForm searchForm);
    
    Collection<User> getUsersByRole(String userRole);
    
    void sendUserRequest(UserRequest userRequest);

    void deleteUserRequest(Long requestId);
    
    Collection<UserRequest> getUserRequestForUser(Long userId);

    Collection<UserRequest> getUserRequestByUser(Long userId);
    
    Integer getUserRequestForUserCount(Long userId);
}
