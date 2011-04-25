package se.telescopesoftware.betpals.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

public class UserProfile implements Serializable {

	private static final long serialVersionUID = 7328993013096806624L;

	private Long id;
    private Long userId;
    private String name;
    private String surname;
    private String email;
    
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private String bio;

    private Date registrationDate;
    private Date lastLoginDate;
    private Integer numberOfVisits;

    private String username;
    private String password;
    private String checkPassword;
    private String oldPassword;

	private Set<Long> friendsIdSet = new HashSet<Long>();

	private MultipartFile userImageFile;
    private FacebookUser facebookUser;
    private String facebookAccessToken;
	
	public UserProfile() {
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
	private void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getNumberOfVisits() {
        return numberOfVisits;
    }

    public void setNumberOfVisits(Integer numberOfVisits) {
        this.numberOfVisits = numberOfVisits;
    }

    public void registerVisit() {
        setLastLoginDate(new Date(System.currentTimeMillis()));
        Integer numberOfVisits = getNumberOfVisits();
        if (numberOfVisits != null) {
            setNumberOfVisits(new Integer(numberOfVisits.intValue() + 1));
        } else {
            setNumberOfVisits(new Integer(1));
        }
    }

    public String getFullName() {
        StringBuffer sb = new StringBuffer();
        if (name != null) {
            sb.append(name);
            sb.append(" ");
        }
        if (surname != null) {
            sb.append(surname);
        }

        return sb.toString();
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

	public Set<Long> getFriendsIdSet() {
		return friendsIdSet;
	}

	public void setFriendsIdSet(Set<Long> friendsIdSet) {
		this.friendsIdSet = friendsIdSet;
	}
	
	public void addFriend(Long id) {
		this.friendsIdSet.add(id);
	}

	public MultipartFile getUserImageFile() {
		return userImageFile;
	}

	public void setUserImageFile(MultipartFile userImageFile) {
		this.userImageFile = userImageFile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public FacebookUser getFacebookUser() {
		return facebookUser;
	}

	public void setFacebookUser(FacebookUser facebookUser) {
		this.facebookUser = facebookUser;
	}

	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}

	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebookAccessToken = facebookAccessToken;
	}

}
