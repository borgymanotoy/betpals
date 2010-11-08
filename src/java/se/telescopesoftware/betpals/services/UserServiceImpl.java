package se.telescopesoftware.betpals.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserSearchForm;
import se.telescopesoftware.betpals.repository.UserRepository;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);


    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        return userRepository.loadUserByUsername(username);
    }

    public Long registerUser(User user) {
        user.addRole("ROLE_USER");
        return userRepository.registerUser(user);
    }

    public void updateUserProfile(UserProfile userProfile) {
        userRepository.updateUserProfile(userProfile);
    }

    public Collection<User> getAllUsers(User user, Integer lastLog, Integer lastReg) {
        return userRepository.loadAllUsers(user, lastLog, lastReg);
    }

    public void updateUser(User user) {
        userRepository.storeUser(user);
    }

    public boolean isUsernameExists(String username) {
        return userRepository.isUsernameExists(username);
    }

    public User getUserByUserId(Long userId) {
        return userRepository.loadUserByUserId(userId);
    }

    public UserProfile getUserProfileByUserId(Long id) {
        return userRepository.loadUserProfileByUserId(id);
    }

    public void deleteUserByUserId(Long userId) {
        userRepository.deleteUserById(userId);
    }

    public void blockUser(Long userId) {
        User user = userRepository.loadUserByUserId(userId);
        user.setEnabled(!user.isEnabled());
        userRepository.storeUser(user);
    }


    public Collection<User> searchUsers(UserSearchForm searchForm) {
        List<User> result = new ArrayList<User>();
        Long userId = searchForm.getUserId();
        String username = searchForm.getUsername();
        String nameOrSurname = searchForm.getNameOrSurname();
        String email = searchForm.getEmail();

        if (userId != null) {
            User user = userRepository.loadUserByUserId(userId);
            if (user != null) {
                result.add(user);
            }
        } else if (username != null && username.length() > 0) {
            User user = null;
            try {
                user = userRepository.loadUserByUsername(username);
            } catch (Exception e) {
                // user not found
            }
            if (user != null) {
                result.add(user);
            }
        } else {
            if (nameOrSurname != null && nameOrSurname.length() > 0) {
                result.addAll(userRepository.findUsersByNameOrSurname(nameOrSurname));
            }
            if (email != null && email.length() > 0) {
                result.addAll(userRepository.findUsersByEmail(email));
            }
        }

        return result;
    }

    public User getUserByUsername(String username) {
        return userRepository.loadUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.loadUserByEmail(email);
    }

	public Collection<User> getUsersByRole(String userRole) {
		return userRepository.findUsersByRole(userRole);
	}

	public Collection<UserProfile> searchUserProfiles(String query, Long userId) {
		Collection<UserProfile> result = userRepository.findUserProfiles(query);
		Collection<UserProfile> filteredResult = new HashSet<UserProfile>();
		
		UserProfile userProfile = getUserProfileByUserId(userId);
		Set<Long> idSet = userProfile.getFriendsIdSet();
		idSet.add(userId);
		
		for(UserProfile friend : result) {
			if(!idSet.contains(friend.getUserId())) {
				filteredResult.add(friend);
			}
		}

		return filteredResult;
	}

	public Collection<UserProfile> getUserFriends(Long userId) {
		List<UserProfile> result = new ArrayList<UserProfile>();
		UserProfile userProfile = getUserProfileByUserId(userId);
		if (userProfile == null) {
			logger.error("Could not load user profile for userId " + userId);
			return result;
		}
		for(Long friendId : userProfile.getFriendsIdSet()) {
			result.add(getUserProfileByUserId(friendId));
		}
		
		return result;
	}
    
    
}
