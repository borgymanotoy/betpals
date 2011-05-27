package se.telescopesoftware.betpals.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import se.telescopesoftware.betpals.domain.Authority;
import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.domain.PasswordRecoveryRequest;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.domain.UserRequestType;

public class HibernateUserRepositoryImpl extends HibernateDaoSupport implements UserRepository {

    public User loadUserByUsername(String username) {
        @SuppressWarnings("unchecked")
		List<User> list = getHibernateTemplate().find("from User usr where usr.username = ?", username);
        if (list == null || list.isEmpty()) {
            throw new UsernameNotFoundException("No user found.");
        }
        return (User) list.get(0);
    }

    public User loadUserByEmail(String email) {
        @SuppressWarnings("unchecked")
		List<UserProfile> list = getHibernateTemplate().find("from UserProfile up where up.email = ?", email);
        if (list == null || list.isEmpty()) {
            throw new UsernameNotFoundException("No user found.");
        }
        UserProfile userProfile = (UserProfile) list.get(0);
        return loadUserByUserId(userProfile.getUserId());
    }

    public User loadUserByUserId(Long id) {
        return getHibernateTemplate().get(User.class, id);
    }

    public boolean isUsernameExists(String username) {
        @SuppressWarnings("unchecked")
		List<User> list = getHibernateTemplate().find("from User usr where usr.username = ?", username);
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }

    private UserProfile loadUserProfile(Long userId) {
        @SuppressWarnings("unchecked")
		List<UserProfile> list = getHibernateTemplate().find("from UserProfile profile where profile.user.id = ?", userId);
        if (list.isEmpty()) {
            UserProfile userProfile = new UserProfile();
            return userProfile;
        }
        return (UserProfile) list.get(0);
    }

    public void storeUser(User user) {
        getHibernateTemplate().saveOrUpdate(user);
    }

    public Long registerUser(User user) {
        User storedUser = (User) getHibernateTemplate().merge(user);
        return storedUser.getId();
    }

    public void updateUserProfile(UserProfile userProfile) {
        getHibernateTemplate().saveOrUpdate(userProfile);
    }

    public Collection<User> loadAllUsers(User user, Integer lastLog, Integer lastReg) {
        @SuppressWarnings("unchecked")
		List<User> list = getHibernateTemplate().find("from User u where u.id != ? and u.id != 0 order by username", user.getId());

        if (lastLog != null) {
            list = filterUsersByLastLoginDate(list, lastLog);
        } else if (lastReg != null) {
            list = filterUsersByRegistrationDate(list, lastReg);
        }

        return list;
    }

    private List<User> filterUsersByRegistrationDate(Collection<User> userList, Integer period) {
        List<User> filteredList = new ArrayList<User>();
        Date date = getFromDateForPeriod(period);
        for (User user : userList) {
            Date registrationDate = user.getUserProfile().getRegistrationDate();
            if (registrationDate == null) {
                continue;
            }
            registrationDate = prepareMinimumTimeDate(registrationDate);
            if (period == 1) {
                if (registrationDate != null && date.compareTo(registrationDate) == 0) {
                    filteredList.add(user);
                }
            } else {
                if (registrationDate != null && date.compareTo(registrationDate) <= 0) {
                    filteredList.add(user);
                }
            }
        }

        return filteredList;
    }

    private List<User> filterUsersByLastLoginDate(Collection<User> userList, Integer period) {
        List<User> filteredList = new ArrayList<User>();
        Date date = getFromDateForPeriod(period);

        for (User user : userList) {
            Date lastLoginDate = user.getUserProfile().getLastLoginDate();
            if (lastLoginDate == null) {
                continue;
            }
            lastLoginDate = prepareMinimumTimeDate(lastLoginDate);
            if (period == 1) {
                if (lastLoginDate != null && date.compareTo(lastLoginDate) == 0) {
                    filteredList.add(user);
                }
            } else {
                if (lastLoginDate != null && date.compareTo(lastLoginDate) <= 0) {
                    filteredList.add(user);
                }
            }
        }

        return filteredList;
    }

    public UserProfile loadUserProfileByUserId(Long id) {
        return loadUserProfile(id);
    }

    public void deleteUserById(Long userId) {
        User user = loadUserByUserId(userId);
        getHibernateTemplate().delete(user.getUserProfile());
        getHibernateTemplate().delete(user);
        deleteUserRoles(user);
    }

    public Integer getLoginCountByPeriod(Integer period) {
    	return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from UserProfile up where up.lastLoginDate >= ? AND up.userId > 9", getFromDateForPeriod(period)));
    }

    public void deleteUserRoles(User user) {
    	Session session = getSession();
    	Transaction transaction = session.beginTransaction();
    	Query query = session.createQuery("delete from Authority a where a.userId = :userId");
    	query.setLong("userId", user.getId());
    	query.executeUpdate();
    	transaction.commit();
    	session.close();
    }

    public Integer getLoginCountForDate(Date date) {
    	return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from UserProfile up where up.lastLoginDate BETWEEN ? AND ? AND up.userId > 9", new Object[] {prepareMinimumTimeDate(date), prepareMaximumTimeDate(date)}));
    }

    public Integer getTotalCount() {
    	return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from UserProfile up where up.userId > 9"));
    }

    public Integer getRegistrationCountByPeriod(Integer period) {
    	return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from UserProfile up where up.registrationDate >= ? AND up.userId > 9", getFromDateForPeriod(period)));
    }

    public Integer getRegistrationCountForDate(Date date) {
    	return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from UserProfile up where up.registrationDate BETWEEN ? AND ? AND up.userId > 9", new Object[] {prepareMinimumTimeDate(date), prepareMaximumTimeDate(date)}));
    }


    private Date getFromDateForPeriod(Integer period) {
        Calendar calendar = Calendar.getInstance();
        if (period != null && period.intValue() > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, - period.intValue());
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date prepareMinimumTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date prepareMaximumTimeDate(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.set(Calendar.HOUR_OF_DAY, 23);
    	calendar.set(Calendar.MINUTE, 59);
    	calendar.set(Calendar.SECOND, 59);
    	calendar.set(Calendar.MILLISECOND, 999);
    	return calendar.getTime();
    }
    
    public Collection<User> findUsersByNameOrSurname(String searchQuery) {
        List<User> result = new ArrayList<User>();
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from UserProfile up where lower(up.name) like :queryString OR lower(up.surname) like :queryString");
        query.setString("queryString", "%" + searchQuery.toLowerCase() + "%");

        @SuppressWarnings("unchecked")
		List<UserProfile> list = query.list();
        for (UserProfile userProfile : list) {
            User user = loadUserByUserId(userProfile.getUserId());
            result.add(user);
        }
        transaction.commit();
        session.close();
        return result;
    }

    public Collection<User> findUsersByEmail(String searchQuery) {
        List<User> result = new ArrayList<User>();
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from UserProfile up where lower(up.email) like :queryString");
        query.setString("queryString", "%" + searchQuery.toLowerCase() + "%");

        @SuppressWarnings("unchecked")
		List<UserProfile> list = query.list();
        for (UserProfile userProfile : list) {
            User user = loadUserByUserId(userProfile.getUserId());
            result.add(user);
        }
        transaction.commit();
        session.close();
        return result;
    }

    public Collection<User> findUsersByRole(String userRole) {
    	List<User> result = new ArrayList<User>();
    	Session session = getSession();
    	Transaction transaction = session.beginTransaction();
    	Query query = session.createQuery("from Authority a where a.authority = :userRole");
    	query.setString("userRole", userRole);
    	
    	@SuppressWarnings("unchecked")
		List<Authority> list = query.list();
    	for (Authority authority : list) {
    		User user = loadUserByUserId(authority.getUserId());
    		result.add(user);
    	}
    	transaction.commit();
    	session.close();
    	return result;
    }

	@SuppressWarnings("unchecked")
	public Collection<UserProfile> findUserProfiles(String searchQuery) {
        return getHibernateTemplate().findByNamedParam(
        		"from UserProfile up where lower(up.name) like :queryString OR " +
        		"lower(up.surname) like :queryString OR " +
        		"lower(up.email) like :queryString OR " + 
        		"lower(up.country) like :queryString OR " + 
        		"lower(up.city) like :queryString OR " + 
        		"lower(up.bio) like :queryString", 
        		"queryString", 
        		"%" + searchQuery.toLowerCase() + "%");
	}

	public void storeUserRequest(UserRequest userRequest) {
		getHibernateTemplate().merge(userRequest);
	}

	@SuppressWarnings("unchecked")
	public Collection<UserRequest> loadUserRequestsForUser(Long userId, UserRequestType requestType) {
		if (requestType != null) {
			return getHibernateTemplate().find("from UserRequest ur where ur.inviteeId = ? and ur.requestType = ?", new Object [] {userId, requestType});
		}
		return getHibernateTemplate().findByNamedParam("from UserRequest ur where ur.inviteeId = :userId", "userId", userId);
	}

	@SuppressWarnings("unchecked")
	public Collection<UserRequest> loadUserRequestsByUser(Long userId, UserRequestType requestType) {
		if (requestType != null) {
			return getHibernateTemplate().find("from UserRequest ur where ur.ownerId = ? and ur.requestType = ?", new Object [] {userId, requestType});
		}
		return getHibernateTemplate().findByNamedParam("from UserRequest ur where ur.ownerId = :userId", "userId", userId);
	}

	public Integer getUserRequestForUserCount(Long userId) {
		return DataAccessUtils.intResult(getHibernateTemplate().findByNamedParam("select count(*) from UserRequest ur where ur.inviteeId = :userId", "userId", userId));
	}

	public void deleteUserRequest(UserRequest userRequest) {
		getHibernateTemplate().delete(userRequest);
	}

	public UserRequest loadUserRequestById(Long id) {
		return getHibernateTemplate().get(UserRequest.class, id);
	}

	public void storeGroup(Group group) {
		getHibernateTemplate().saveOrUpdate(group);
	}

	public Group loadGroupById(Long groupId) {
		return getHibernateTemplate().load(Group.class, groupId);
	}

	@SuppressWarnings("unchecked")
	public Collection<Group> loadUserGroups(Long userId) {
		return getHibernateTemplate().findByNamedParam("from Group g where g.ownerId = :userId", "userId", userId);
	}

	public void deleteGroup(Group group) {
		getHibernateTemplate().delete(group);
	}

	public Community storeCommunity(Community community) {
		return getHibernateTemplate().merge(community);
	}

	public Community loadCommunityById(Long communityId) {
		return getHibernateTemplate().get(Community.class, communityId);
	}

	@SuppressWarnings("unchecked")
	public Collection<Community> loadUserCommunities(Long userId) {
//		return getHibernateTemplate().findByNamedParam("from Community g where g.ownerId = :userId", "userId", userId);
		UserProfile userProfile = loadUserProfile(userId);
		return getHibernateTemplate().findByNamedParam("from Community g where :user in elements(g.members)", "user", userProfile);
	}

	public void deleteCommunity(Community community) {
		getHibernateTemplate().delete(community);
	}

	@SuppressWarnings("unchecked")
	public Collection<Community> findCommunities(String searchQuery) {
        return getHibernateTemplate().findByNamedParam("from Community c where lower(c.name) like :queryString OR lower(c.description) like :queryString", "queryString", "%" + searchQuery.toLowerCase() + "%");
	}

	public void storePasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest) {
		getHibernateTemplate().saveOrUpdate(passwordRecoveryRequest);
	}

	public PasswordRecoveryRequest findPasswordRecoveryRequest(String requestHash) {
		List<?> result = getHibernateTemplate().find("from PasswordRecoveryRequest pr where pr.requestHash = ?", requestHash);
		if (result != null && !result.isEmpty()) {
			return (PasswordRecoveryRequest) result.get(0);
		}
		return null;
	}

	public void deletePasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest) {
		getHibernateTemplate().delete(passwordRecoveryRequest);
	}
    
}
