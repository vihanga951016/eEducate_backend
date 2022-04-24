package com.vihanga.eEducate.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HigherStaff implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, updatable = false)
	private Long id;// this one is the primary key
	private String userId;
	private String nic;
	private String name;
	private String username;
	private String password;
	private String email;
	private Long phone;
	private String profileImageUrl;
	private Date lastLoginDate;// last time login
	private Date lastLoginDateDisplay;// for display last time login
	private Date joinDate;
	private String roles;
	private String[] authorities; //give permission or assign for do some things
	private boolean isLoggedIn;
	private boolean isActive;
	private boolean isNotLocked;
	private String token;

	public HigherStaff() {

	}

	public HigherStaff(Long id, String userId, String nic, String name, String username, String password, String email, Long phone,
					   String profileImageUrl, Date lastLoginDate, Date lastLoginDateDisplay, Date joinDate, String roles,
					   String[] authorities,boolean isLoggedIn, boolean isActive, boolean isNotLocked, String token) {
		super();
		this.id = id;
		this.userId = userId;
		this.nic = nic;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.profileImageUrl = profileImageUrl;
		this.lastLoginDate = lastLoginDate;
		this.lastLoginDateDisplay = lastLoginDateDisplay;
		this.joinDate = joinDate;
		this.roles = roles;
		this.authorities = authorities;
		this.isLoggedIn = isLoggedIn;
		this.isActive = isActive;
		this.isNotLocked = isNotLocked;
		this.token = token;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getPhone() {
		return phone;
	}
	public void setPhone(Long phone) {
		this.phone = phone;
	}
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public Date getLastLoginDateDisplay() {
		return lastLoginDateDisplay;
	}
	public void setLastLoginDateDisplay(Date lastLoginDateDisplay) {
		this.lastLoginDateDisplay = lastLoginDateDisplay;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public String[] getAuthorities() {
		return authorities;
	}
	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isNotLocked() {
		return isNotLocked;
	}
	public void setNotLocked(boolean isNotLocked) {
		this.isNotLocked = isNotLocked;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public boolean isLoggedIn() { return isLoggedIn; }
	public void setLoggedIn(boolean loggedIn) { isLoggedIn = loggedIn; }

	public void setToken(String token) {
		this.token = token;
	}
}
