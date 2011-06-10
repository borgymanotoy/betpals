package se.telescopesoftware.betpals.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.FacebookUser;
import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.domain.PasswordRecoveryRequest;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.domain.UserRequestType;
import se.telescopesoftware.betpals.domain.UserSearchForm;
import se.telescopesoftware.betpals.repository.UserRepository;

@Service("userDetailsService")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        return userRepository.loadUserByUsername(username);
    }

	@Transactional(readOnly = false)
    public Long registerUser(User user) {
        user.addRole("ROLE_USER");
        logger.info("Registering " + user);
        return userRepository.registerUser(user);
    }

	@Transactional(readOnly = false)
    public void updateUserProfile(UserProfile userProfile) {
    	logger.info("Saving " + userProfile);
        userRepository.updateUserProfile(userProfile);
    }

    public Collection<User> getAllUsers(User user, Integer lastLog, Integer lastReg) {
        return userRepository.loadAllUsers(user, lastLog, lastReg);
    }

	@Transactional(readOnly = false)
    public void updateUser(User user) {
    	logger.info("Saving " + user);
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

	@Transactional(readOnly = false)
    public void deleteUserByUserId(Long userId) {
        userRepository.deleteUserById(userId);
    }

	@Transactional(readOnly = false)
    public void blockUser(Long userId) {
        User user = userRepository.loadUserByUserId(userId);
        user.setEnabled(!user.isEnabled());
        logger.info("Blocking " + user);
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
    	User user = null;
    	try {
    		user = userRepository.loadUserByUsername(username);
    	} catch (Exception ex) {
    		logger.error(ex);
    	}
        return user;
    }

    public User getUserByEmail(String email) {
    	User user = null;
    	try {
    		user = userRepository.loadUserByEmail(email);
    	} catch (Exception ex) {
    		logger.error(ex);
    	}
        return user;
    }

	public Collection<User> getUsersByRole(String userRole) {
		return userRepository.findUsersByRole(userRole);
	}

	public Collection<UserProfile> searchUserProfiles(String query, Long userId) {
		return userRepository.findUserProfiles(query);
	}

	public Collection<UserProfile> getUserFriends(Long userId) {
		UserProfile userProfile = getUserProfileByUserId(userId);
		if (userProfile == null) {
			logger.error("Could not load user profile for userId " + userId);
			return new ArrayList<UserProfile>();
		}
		
		return userProfile.getFriends();
	}

	@Transactional(readOnly = false)
	public void sendUserRequest(UserRequest userRequest) {
		for (UserRequest request : getAllUserRequestsByUser(userRequest.getOwnerId())) {
			if (request.getInviteeId().compareTo(userRequest.getInviteeId()) == 0 
					&& request.getRequestType() == userRequest.getRequestType()) {
				return; // Do not send duplicate requests
			}
		}
		logger.info("Sending " + userRequest);
		userRepository.storeUserRequest(userRequest);
	}

	public Collection<UserRequest> getAllUserRequestsForUser(Long userId) {
		return userRepository.loadUserRequestsForUser(userId, null);
	}

	public Collection<UserRequest> getAllUserRequestsByUser(Long userId) {
		return userRepository.loadUserRequestsByUser(userId, null);
	}

	public Integer getUserRequestForUserCount(Long userId) {
		return userRepository.getUserRequestForUserCount(userId);
	}

	public Collection<UserRequest> getUserRequestsForUserByType(Long userId, UserRequestType requestType) {
		return userRepository.loadUserRequestsForUser(userId, requestType);
	}

	public Collection<UserRequest> getUserRequestsByUserByType(Long userId, UserRequestType requestType) {
		return userRepository.loadUserRequestsByUser(userId, requestType);
	}
    
	@Transactional(readOnly = false)
	public void deleteUserRequest(Long requestId) {
		UserRequest userRequest = userRepository.loadUserRequestById(requestId);
		if (userRequest != null) {
			logger.info("Deleting " + userRequest);
			userRepository.deleteUserRequest(userRequest);
		}
	}

	@Transactional(readOnly = false)
	public Long registerFacebookUser(FacebookUser facebookUser) {
		User user = new User(facebookUser.getMybetpalsUsername());
		user.encodeAndSetPassword(facebookUser.getMybetpalsPassword());
		UserProfile userProfile = new UserProfile();
		userProfile.setName(facebookUser.getFirstName());
		userProfile.setSurname(facebookUser.getLastName());
		userProfile.setEmail(facebookUser.getEmail());
		userProfile.setRegistrationDate(new Date());
		userProfile.setUser(user);
		
		user.setUserProfile(userProfile);
		
		return registerUser(user);
	}

	@Transactional(readOnly = false)
	public void saveGroup(Group group) {
		for (Long memberId : group.getMembersIdSet()) {
			group.addMember(getUserProfileByUserId(memberId));
		}
		logger.info("Saving " + group);
		userRepository.storeGroup(group);
	}

	public Group getGroupById(Long groupId) {
		return userRepository.loadGroupById(groupId);
	}

	public Collection<Group> getUserGroups(Long userId) {
		return userRepository.loadUserGroups(userId);
	}

	@Transactional(readOnly = false)
	public void deleteGroup(Long groupId, Long userId) {
		Group group = userRepository.loadGroupById(groupId);
		if (group.checkOwnership(userId)) {
			logger.info("Deleting " + group);
			userRepository.deleteGroup(group);
		}
	}

	@Transactional(readOnly = false)
	public Community saveCommunity(Community community) {
		logger.info("Saving " + community);
		return userRepository.storeCommunity(community);
	}

	public Community getCommunityById(Long communityId) {
		return userRepository.loadCommunityById(communityId);
	}

	public Collection<Community> getUserCommunities(Long userId) {
		return userRepository.loadUserCommunities(userId);
	}

	@Transactional(readOnly = false)
	public void deleteCommunity(Long communityId, Long userId) {
		Community community = userRepository.loadCommunityById(communityId);
		if (community.checkOwnership(userId) && community.getMembers().size() == 1) {
			logger.info("Deleting " + community);
			userRepository.deleteCommunity(community);
		}
	}

	public Collection<Community> searchCommunities(String query) {
		return userRepository.findCommunities(query);
	}

	public UserRequest getUserRequestById(Long requestId) {
		return userRepository.loadUserRequestById(requestId);
	}

	@Transactional(readOnly = false)
	public void registerPasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest) {
		logger.info("Saving " + passwordRecoveryRequest);
		userRepository.storePasswordRecoveryRequest(passwordRecoveryRequest);
	}

	public PasswordRecoveryRequest findPasswordRecoveryRequest(String requestHash) {
		return userRepository.findPasswordRecoveryRequest(requestHash);
	}

	@Transactional(readOnly = false)
	public void deletePasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest) {
		logger.info("Deleting " + passwordRecoveryRequest);
		userRepository.deletePasswordRecoveryRequest(passwordRecoveryRequest);
	}

   
}
