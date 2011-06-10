package se.telescopesoftware.betpals.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import se.telescopesoftware.betpals.domain.Authority;
import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.domain.PasswordRecoveryRequest;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.domain.UserRequestType;

@Repository
public class HibernateUserRepositoryImpl implements UserRepository {

	private SessionFactory sessionFactory;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
    public User loadUserByUsername(String username) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from User usr where usr.username = :username");
    	query.setString("username", username);
		List<?> list = query.list();
        if (list == null || list.isEmpty()) {
            throw new UsernameNotFoundException("No user found.");
        }
        return (User) list.get(0);
    }

    public User loadUserByEmail(String email) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from UserProfile up where up.email = :email");
    	query.setString("email", email);
		List<?> list = query.list();
        if (list == null || list.isEmpty()) {
            throw new UsernameNotFoundException("No user found.");
        }
        UserProfile userProfile = (UserProfile) list.get(0);
        return loadUserByUserId(userProfile.getUserId());
    }

    public User loadUserByUserId(Long id) {
    	Session session = sessionFactory.getCurrentSession();
        return (User) session.get(User.class, id);
    }

    public boolean isUsernameExists(String username) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from User usr where usr.username = :username");
    	query.setString("username", username);
		return query.iterate().hasNext();
    }

    private UserProfile loadUserProfile(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from UserProfile profile where profile.user.id = :userId");
    	query.setLong("userId", userId);
		List<?> list = query.list();
        if (list.isEmpty()) {
            UserProfile userProfile = new UserProfile();
            return userProfile;
        }
        return (UserProfile) list.get(0);
    }

    public void storeUser(User user) {
    	Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
    }

    public Long registerUser(User user) {
    	Session session = sessionFactory.getCurrentSession();
        User storedUser = (User) session.merge(user);
        return storedUser.getId();
    }

    public void updateUserProfile(UserProfile userProfile) {
    	Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(userProfile);
    }

    public Collection<User> loadAllUsers(User user, Integer lastLog, Integer lastReg) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from User u where u.id != :userId and u.id != 0 order by username");
    	query.setLong("userId", user.getId());
		@SuppressWarnings("unchecked")
		List<User> list = query.list();

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
    	Session session = sessionFactory.getCurrentSession();
        User user = loadUserByUserId(userId);
        session.delete(user.getUserProfile());
        session.delete(user);
        deleteUserRoles(user);
    }

    public Integer getLoginCountByPeriod(Integer period) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from UserProfile up where up.lastLoginDate >= :fromDate AND up.userId > 9");
    	query.setParameter("fromDate", getFromDateForPeriod(period));
    	return DataAccessUtils.intResult(query.list());
    }

    public void deleteUserRoles(User user) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("delete from Authority a where a.userId = :userId");
    	query.setLong("userId", user.getId());
    	query.executeUpdate();
    }

    public Integer getLoginCountForDate(Date date) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from UserProfile up where up.lastLoginDate BETWEEN :minDate AND :maxDate AND up.userId > 9");
    	query.setParameter("minDate", prepareMinimumTimeDate(date));
    	query.setParameter("maxDate", prepareMaximumTimeDate(date));
    	return DataAccessUtils.intResult(query.list());
    }

    public Integer getTotalCount() {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from UserProfile up where up.userId > 9");
    	return DataAccessUtils.intResult(query.list());
    }

    public Integer getRegistrationCountByPeriod(Integer period) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from UserProfile up where up.registrationDate >= :fromDate AND up.userId > 9");
    	query.setParameter("fromDate", getFromDateForPeriod(period));
    	return DataAccessUtils.intResult(query.list());
    }

    public Integer getRegistrationCountForDate(Date date) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from UserProfile up where up.registrationDate BETWEEN :minDate AND :maxDate AND up.userId > 9");
    	query.setParameter("minDate", prepareMinimumTimeDate(date));
    	query.setParameter("maxDate", prepareMaximumTimeDate(date));
    	return DataAccessUtils.intResult(query.list());
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
    	Session session = sessionFactory.getCurrentSession();
        List<User> result = new ArrayList<User>();
        Query query = session.createQuery("from UserProfile up where lower(up.name) like :queryString OR lower(up.surname) like :queryString");
        query.setString("queryString", "%" + searchQuery.toLowerCase() + "%");

        @SuppressWarnings("unchecked")
		List<UserProfile> list = query.list();
        for (UserProfile userProfile : list) {
            User user = loadUserByUserId(userProfile.getUserId());
            result.add(user);
        }
        return result;
    }

    public Collection<User> findUsersByEmail(String searchQuery) {
    	Session session = sessionFactory.getCurrentSession();
        List<User> result = new ArrayList<User>();
        Query query = session.createQuery("from UserProfile up where lower(up.email) like :queryString");
        query.setString("queryString", "%" + searchQuery.toLowerCase() + "%");

        @SuppressWarnings("unchecked")
		List<UserProfile> list = query.list();
        for (UserProfile userProfile : list) {
            User user = loadUserByUserId(userProfile.getUserId());
            result.add(user);
        }
        return result;
    }

    public Collection<User> findUsersByRole(String userRole) {
    	Session session = sessionFactory.getCurrentSession();
    	List<User> result = new ArrayList<User>();
    	Query query = session.createQuery("from Authority a where a.authority = :userRole");
    	query.setString("userRole", userRole);
    	
    	@SuppressWarnings("unchecked")
		List<Authority> list = query.list();
    	for (Authority authority : list) {
    		User user = loadUserByUserId(authority.getUserId());
    		result.add(user);
    	}
    	return result;
    }

	@SuppressWarnings("unchecked")
	public Collection<UserProfile> findUserProfiles(String searchQuery) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from UserProfile up where lower(up.name) like :queryString OR " +
        		"lower(up.surname) like :queryString OR " +
        		"lower(up.email) like :queryString OR " + 
        		"lower(up.country) like :queryString OR " + 
        		"lower(up.city) like :queryString OR " + 
        		"lower(up.bio) like :queryString");
    	query.setString("queryString", "%" + searchQuery.toLowerCase() + "%");
        return query.list();
	}

	public void storeUserRequest(UserRequest userRequest) {
    	Session session = sessionFactory.getCurrentSession();
		session.merge(userRequest);
	}

	@SuppressWarnings("unchecked")
	public Collection<UserRequest> loadUserRequestsForUser(Long userId, UserRequestType requestType) {
    	Session session = sessionFactory.getCurrentSession();
		if (requestType != null) {
			Query query = session.createQuery("from UserRequest ur where ur.inviteeId = :userId and ur.requestType = :requestType");
	    	query.setLong("userId", userId);
	    	query.setParameter("requestType", requestType);
			return query.list();
		}
		Query query = session.createQuery("from UserRequest ur where ur.inviteeId = :userId");
    	query.setLong("userId", userId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<UserRequest> loadUserRequestsByUser(Long userId, UserRequestType requestType) {
    	Session session = sessionFactory.getCurrentSession();
		if (requestType != null) {
			Query query = session.createQuery("from UserRequest ur where ur.ownerId = :ownerId and ur.requestType = :requestType");
	    	query.setLong("ownerId", userId);
	    	query.setParameter("requestType", requestType);
			return query.list();
		} 
		Query query = session.createQuery("from UserRequest ur where ur.ownerId = :userId");
    	query.setLong("userId", userId);
		return query.list();
	}

	public Integer getUserRequestForUserCount(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from UserRequest ur where ur.inviteeId = :userId");
    	query.setLong("userId", userId);
		return DataAccessUtils.intResult(query.list());
	}

	public void deleteUserRequest(UserRequest userRequest) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(userRequest);
	}

	public UserRequest loadUserRequestById(Long id) {
    	Session session = sessionFactory.getCurrentSession();
		return (UserRequest) session.get(UserRequest.class, id);
	}

	public void storeGroup(Group group) {
    	Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(group);
	}

	public Group loadGroupById(Long groupId) {
    	Session session = sessionFactory.getCurrentSession();
		return (Group) session.get(Group.class, groupId);
	}

	@SuppressWarnings("unchecked")
	public Collection<Group> loadUserGroups(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Group g where g.ownerId = :userId");
    	query.setLong("userId", userId);
		return query.list();
	}

	public void deleteGroup(Group group) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(group);
	}

	public Community storeCommunity(Community community) {
    	Session session = sessionFactory.getCurrentSession();
		return (Community) session.merge(community);
	}

	public Community loadCommunityById(Long communityId) {
    	Session session = sessionFactory.getCurrentSession();
		return (Community) session.get(Community.class, communityId);
	}

	@SuppressWarnings("unchecked")
	public Collection<Community> loadUserCommunities(Long userId) {
		UserProfile userProfile = loadUserProfile(userId);
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Community g where :user in elements(g.members)");
    	query.setParameter("user", userProfile);
		return query.list();
	}

	public void deleteCommunity(Community community) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(community);
	}

	@SuppressWarnings("unchecked")
	public Collection<Community> findCommunities(String searchQuery) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Community c where lower(c.name) like :queryString OR lower(c.description) like :queryString");
    	query.setString("queryString", "%" + searchQuery.toLowerCase() + "%");
        return query.list();
	}

	public void storePasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest) {
    	Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(passwordRecoveryRequest);
	}

	public PasswordRecoveryRequest findPasswordRecoveryRequest(String requestHash) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from PasswordRecoveryRequest pr where pr.requestHash = :requestHash");
    	query.setString("requestHash", requestHash);
		Iterator<?> result = query.iterate();
		if (result.hasNext()) {
			return (PasswordRecoveryRequest) result.next();
		}
		return null;
	}

	public void deletePasswordRecoveryRequest(PasswordRecoveryRequest passwordRecoveryRequest) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(passwordRecoveryRequest);
	}
    
}
