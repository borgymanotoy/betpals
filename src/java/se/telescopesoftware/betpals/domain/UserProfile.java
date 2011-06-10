package se.telescopesoftware.betpals.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name="userprofile")
public class UserProfile implements Serializable {

	private static final long serialVersionUID = 7328993013096806624L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@NotBlank
    private String name;
    private String surname;
    @NotBlank
    @Email
    private String email;
    
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private String bio;

    private Date registrationDate;
    private Date lastLoginDate;
    private Integer numberOfVisits;

    @Transient
    private String username;
    @Transient
    private String password;
    @Transient
    private String checkPassword;
    @Transient
    private String oldPassword;

    @ManyToOne()
    @JoinColumn(name="userId")
    private User user;

    @OneToMany()
    @JoinTable(
        name="user_friends",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="friend_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
	private Set<UserProfile> friends = new HashSet<UserProfile>();

	@Transient
	private Set<Long> friendsIdSet = new HashSet<Long>();

	@Transient
	private MultipartFile userImageFile;
	@Transient
    private FacebookUser facebookUser;
	@Transient
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
        return user.getId();
    }

    public void setUserId(Long userId) {
    	System.out.println("Setting user id!");

//        this.userId = userId;
    }

    public void setUser(User user) {
    	this.user = user;
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

	public void setFriends(Set<UserProfile> friends) {
		this.friends = friends;
	}

	public Set<UserProfile> getFriends() {
		return friends;
	}
	
	public void addFriend(UserProfile userProfile) {
		this.friends.add(userProfile);
	}

	public Set<Long> getFriendsIdSet() {
		if (friendsIdSet != null && !friendsIdSet.isEmpty()) {
			return friendsIdSet;
		}
		
		Set<Long> idSet = new HashSet<Long>();
		for (UserProfile friend : friends) {
			idSet.add(friend.getUserId());
		}
		
		return idSet;
	}
    
	public void setFriendsIdSet(Set<Long> friendsIdSet) {
		this.friendsIdSet = friendsIdSet;
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

	@Override
	public boolean equals(Object obj) {
		return id != null ? id.compareTo(((UserProfile)obj).getId()) == 0 : super.equals(obj);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : super.hashCode();
	}

	@Override
	public String toString() {
		return "UserProfile [id=" + id + ", name=" + name + ", surname="
				+ surname + ", email=" + email + ", address=" + address
				+ ", city=" + city + ", postalCode=" + postalCode
				+ ", country=" + country + ", registrationDate="
				+ registrationDate + ", lastLoginDate=" + lastLoginDate
				+ ", numberOfVisits=" + numberOfVisits + ", username="
				+ username + ", facebookUser=" + facebookUser
				+ ", facebookAccessToken=" + facebookAccessToken + "]";
	}

}
