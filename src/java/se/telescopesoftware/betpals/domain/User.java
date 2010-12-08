package se.telescopesoftware.betpals.domain;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {

	private static final long serialVersionUID = -707288000475263023L;

	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_SUPERVISOR = "ROLE_SUPERVISOR";
	
	private Long id;
    private String username;
    private String password;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private Set<String> roles = new HashSet<String>();

    private UserProfile userProfile;

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
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        for (String role : roles) {
            grantedAuthorities.add(new GrantedAuthorityImpl(role));
        }
        return grantedAuthorities;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @SuppressWarnings("unused")
	private void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @SuppressWarnings("unused")
	private void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @SuppressWarnings("unused")
	private void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles.addAll(roles);
    }

    public void addRole(String role) {
        this.roles.add(role);
    }
    
    public void removeRole(String role) {
    	this.roles.remove(role);
    }
    
    public void encodeAndSetPassword(String password) {
        PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("username");
        setPassword(passwordEncoder.encodePassword(password, saltSource.getSalt(this)));
    }
    
    public boolean isSupervisor() {
    	return this.roles.contains(ROLE_SUPERVISOR);
    }

    public boolean getSupervisor() {
    	return isSupervisor();
    }

}
