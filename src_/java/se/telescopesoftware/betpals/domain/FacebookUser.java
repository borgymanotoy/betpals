package se.telescopesoftware.betpals.domain;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Helper class to represent facebook User object received as JSON data.
 *
 */
public class FacebookUser {

	private String id;
	private String name;
	private String firstName;
	private String lastName;
	private String email;
	
	public FacebookUser(String jsonData) {
		JSONObject jObject = (JSONObject) JSONSerializer.toJSON(jsonData);
		this.firstName = jObject.getString("first_name");
		this.lastName = jObject.getString("last_name");
		this.name = jObject.getString("name");
		this.email = jObject.getString("email");
		this.id = jObject.getString("id");
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMybetpalsUsername() {
		return "fb_" + id;
	}
	
	public String getMybetpalsPassword() {
		return "fb_" + id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
