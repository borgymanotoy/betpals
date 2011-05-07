package se.telescopesoftware.betpals.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.FacebookUser;
import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.domain.UserRequestType;
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

	public void sendUserRequest(UserRequest userRequest) {
		for (UserRequest request : getAllUserRequestsByUser(userRequest.getOwnerId())) {
			if (request.getInviteeId().compareTo(userRequest.getInviteeId()) == 0 
					&& request.getRequestType() == userRequest.getRequestType()) {
				return; // Do not send duplicate requests
			}
		}
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
    
	public void deleteUserRequest(Long requestId) {
		UserRequest userRequest = userRepository.loadUserRequestById(requestId);
		if (userRequest != null) {
			userRepository.deleteUserRequest(userRequest);
		}
	}

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

	public void saveGroup(Group group) {
		for (Long memberId : group.getMembersIdSet()) {
			group.addMember(getUserProfileByUserId(memberId));
		}
		
		userRepository.storeGroup(group);
	}

	public Group getGroupById(Long groupId) {
		return userRepository.loadGroupById(groupId);
	}

	public Collection<Group> getUserGroups(Long userId) {
		return userRepository.loadUserGroups(userId);
	}

	public void deleteGroup(Long groupId, Long userId) {
		Group group = userRepository.loadGroupById(groupId);
		if (group.checkOwnership(userId)) {
			userRepository.deleteGroup(group);
		}
	}

	public Community saveCommunity(Community community) {
		return userRepository.storeCommunity(community);
	}

	public Community getCommunityById(Long communityId) {
		return userRepository.loadCommunityById(communityId);
	}

	public Collection<Community> getUserCommunities(Long userId) {
		return userRepository.loadUserCommunities(userId);
	}

	public void deleteCommunity(Long communityId, Long userId) {
		Community community = userRepository.loadCommunityById(communityId);
		if (community.checkOwnership(userId) && community.getMembers().size() == 1) {
			userRepository.deleteCommunity(community);
		}
	}

	public Collection<Community> searchCommunities(String query) {
		return userRepository.findCommunities(query);
	}

	public UserRequest getUserRequestById(Long requestId) {
		return userRepository.loadUserRequestById(requestId);
	}

   
}
