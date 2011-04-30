package se.telescopesoftware.betpals.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="authority")
public class Authority implements GrantedAuthority {

	private static final long serialVersionUID = 4636060732805014021L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String authority;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="userId")
    private User user;
    
    
    public Authority() {
    }

    public Authority(User user, String role) {
    	this.user = user;
    	this.username = user.getUsername();
    	this.authority = role;
    }
    
    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
	private void setId(long id) {
        this.id = id;
    }

    public Long getUserId() {
        return user.getId();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
