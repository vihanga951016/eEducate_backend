package com.vihanga.eEducate.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class HigherStaffPrincipal implements UserDetails{

	private HigherStaff higherStaff; 
	//To use properties of HigherStaff class

	public HigherStaffPrincipal(HigherStaff higherStaff) {
		super();
		this.higherStaff = higherStaff;
	}

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// What are the things that user has allow to do and not
		
		//Take all authorities of users which is String in an array, and map each one of them as a simple granted authority which is a string
		//'SimpleGrantedAuthority::new' -> create an object. map all user roles into this objects.
		//'collect(Collectors.toList()' collect objects and send it to list.
		return Arrays.stream(this.higherStaff.getAuthorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		// Get Password
		return this.higherStaff.getPassword();
	}

	@Override
	public String getUsername() {
		// Get Username
		return this.higherStaff.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		//This is not in this application
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		//Account is not locked
		return this.higherStaff.isNotLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		//This is not in this application
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Account is active
		return this.higherStaff.isActive();
	}

	
}
