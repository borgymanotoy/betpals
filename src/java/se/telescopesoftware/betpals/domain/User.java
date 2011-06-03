package se.telescopesoftware.betpals.domain;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.codec.Base64;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="users")
public class User implements UserDetails {

	private static final long serialVersionUID = -707288000475263023L;

	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_SUPERVISOR = "ROLE_SUPERVISOR";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    private String username;
    private String password;
    private boolean enabled = true;

    @OneToMany(mappedBy="user")
    @Cascade(CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Authority> authorities = new HashSet<Authority>();
    
    @OneToMany(mappedBy = "user")
    @Cascade(CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<UserProfile> userProfileSet = new HashSet<UserProfile>();

    
    public User() {
    }

    public User(String username, String password) {
        this(username, password, true);
    }

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String username) {
    	this.username = username;
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
	private void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String username() {
        return username;
    }

    @SuppressWarnings("unused")
	private void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>(authorities);
    }

    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UserProfile getUserProfile() {
    	if (userProfileSet.isEmpty()) {
    		return null;
    	}
        return userProfileSet.iterator().next();
    }

    public void setUserProfile(UserProfile userProfile) {
    	Set<UserProfile> profileSet = new HashSet<UserProfile>();
    	profileSet.add(userProfile);
        this.userProfileSet = profileSet;
    }

    public void addRole(String role) {
    	Authority authority = new Authority(this, role);
        this.authorities.add(authority);
    }
    
    public void encodeAndSetPassword(String password) {
        PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("username");
        setPassword(passwordEncoder.encodePassword(password, saltSource.getSalt(this)));
    }
    
    public boolean checkPassword(String password) {
    	PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
    	ReflectionSaltSource saltSource = new ReflectionSaltSource();
    	saltSource.setUserPropertyToUse("username");
    	String encodedPassword = passwordEncoder.encodePassword(password, saltSource.getSalt(this));
    	
    	return encodedPassword.equals(this.password);
    }
    
    public boolean isSupervisor() {
    	for (GrantedAuthority authority : authorities) {
    		if (authority.getAuthority().equalsIgnoreCase(ROLE_SUPERVISOR)) {
    			return true;
    		}
    	}
    	return false;
    }

    public boolean getSupervisor() {
    	return isSupervisor();
    }

	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}
	
	public String getEncodedLink() {
		StringBuffer sb = new StringBuffer();
		sb.append(System.currentTimeMillis());
		sb.append("/");
		sb.append(getId());
		return new String(Base64.encode(sb.toString().getBytes())); 
	}

	@Override
	public String toString() {
		return "User " + username  + " [id=" + id + ", enabled="
				+ enabled + "]";
	}


}
