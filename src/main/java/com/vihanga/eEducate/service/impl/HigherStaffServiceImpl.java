package com.vihanga.eEducate.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import com.vihanga.eEducate.constant.ApplicationConstants;
import com.vihanga.eEducate.constant.FileConstant;
import com.vihanga.eEducate.enumeration.Role;
import com.vihanga.eEducate.exceptions.domain.EmailExistException;
import com.vihanga.eEducate.exceptions.domain.EmailNotFoundException;
import com.vihanga.eEducate.exceptions.domain.UserNotFoundException;
import com.vihanga.eEducate.exceptions.domain.UsernameExistException;
import com.vihanga.eEducate.service.EmailService;
import com.vihanga.eEducate.service.LoginAttemptService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vihanga.eEducate.domain.HigherStaff;
import com.vihanga.eEducate.domain.HigherStaffPrincipal;
import com.vihanga.eEducate.repository.HigherStaffRepository;
import com.vihanga.eEducate.service.HigherStaffService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

//'UserDetailsService' is a interface which is check the user provide the good credentials.
@SuppressWarnings("Duplicates")
@Service
@Transactional
@Qualifier("UserDetailsService")
public class HigherStaffServiceImpl implements HigherStaffService, UserDetailsService{

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	private HigherStaffRepository higherStaffRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private LoginAttemptService loginAttemptService;
	private EmailService emailService;
	
	@Autowired
	public HigherStaffServiceImpl(HigherStaffRepository higherStaffRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService, EmailService emailService) {
		this.higherStaffRepository = higherStaffRepository;
		this.passwordEncoder = passwordEncoder;
		this.loginAttemptService = loginAttemptService;
		this.emailService = emailService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Get the username
		HigherStaff higherStaff = higherStaffRepository.findStaffMemberByUsername(username);
		if(higherStaff == null) {

			LOGGER.error(ApplicationConstants.NO_USER_FOUND_BY_USERNAME +username);
			throw new UsernameNotFoundException(ApplicationConstants.NO_USER_FOUND_BY_USERNAME +username);
			
		} else {
			try {
				validateLoginAttempt(higherStaff);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			higherStaff.setLastLoginDateDisplay(higherStaff.getLastLoginDate());
			higherStaff.setLastLoginDate(new Date());
			//Save the Staff members. Because we should save login date before return the staff member.
			higherStaffRepository.save(higherStaff);
			HigherStaffPrincipal higherStaffPrincipal = new HigherStaffPrincipal(higherStaff); //We can return 'higherStaffPrincipal'
			//as a 'User Details' in HigherStaffPrincipal
			LOGGER.info("Returning found by username: " +username);
			return higherStaffPrincipal;
			
		}
	}

	private void validateLoginAttempt(HigherStaff higherStaff) throws ExecutionException {
		if(higherStaff.isNotLocked()){

			if(loginAttemptService.hasExceededMaxAttempts(higherStaff.getUsername())){
				higherStaff.setNotLocked(false);
			} else {
				higherStaff.setNotLocked(true);
			}
		} else {

			// If user account is not locked, it's means, that user is already logged.
			loginAttemptService.evictUserFromLoginAttemptCache(higherStaff.getUsername());
		}
	}

	@Override
	public HigherStaff register(String nic, String name, String username, String email, Long phone) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
		validateNewUsernameAndEmail(StringUtils.EMPTY, username , email);

		HigherStaff higherStaff = new HigherStaff();
		higherStaff.setUserId(GenerateUserId());
		String password = GeneratePassword();
		higherStaff.setNic(nic);
		higherStaff.setName(name);
		higherStaff.setUsername(username);
		higherStaff.setEmail(email);
		higherStaff.setPhone(phone);
		higherStaff.setJoinDate(new Date());
		higherStaff.setPassword(EncodePassword(password));
		higherStaff.setActive(true);
		higherStaff.setNotLocked(true);
		higherStaff.setRoles(Role.ROLE_STUDENT.name());
		higherStaff.setAuthorities(Role.ROLE_STUDENT.getAuthorities());
		higherStaff.setProfileImageUrl(GetTemporaryProfileImageUrl(username));
		higherStaffRepository.save(higherStaff);
		LOGGER.info("New user password: " +password);
		emailService.sendNewPasswordEmail(name,password,email);

		return higherStaff;
	}

	@Override
	public List<HigherStaff> getStaff() {
		return higherStaffRepository.findAll();
	}

	@Override
	public HigherStaff addStaffMember(String nic, String name, String username, String email, long phone, String role, boolean isNotLocked,  boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
		validateNewUsernameAndEmail("",username,email); //In here, currentUsername is empty, because of this is a new user.

		HigherStaff higherStaff = new HigherStaff();
		String password = GeneratePassword();
		higherStaff.setUserId(GenerateUserId());
		higherStaff.setNic(nic);
		higherStaff.setName(name);
		higherStaff.setUsername(username);
		higherStaff.setEmail(email);
		higherStaff.setPhone(phone);
		higherStaff.setJoinDate(new Date());
		higherStaff.setPassword(EncodePassword(password));
		higherStaff.setActive(isActive);
		higherStaff.setNotLocked(true);
		higherStaff.setRoles(getRoleEnumName(role).name());
		higherStaff.setAuthorities(getRoleEnumName(role).getAuthorities());
		higherStaff.setProfileImageUrl(GetTemporaryProfileImageUrl(username));
		higherStaffRepository.save(higherStaff);
		saveProfileImage(higherStaff, profileImage);
		return higherStaff;
	}

	@Override
	public HigherStaff updateStaffMember(String currentUsername, String newNic, String newName, String newUsername, String newEmail, long newPhone, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
		HigherStaff currentHigherStaffUser = validateNewUsernameAndEmail(currentUsername,newUsername,newEmail); //In here, currentUsername is empty, because of this is a new user.
		currentHigherStaffUser.setNic(newNic);
		currentHigherStaffUser.setName(newName);
		currentHigherStaffUser.setUsername(newUsername);
		currentHigherStaffUser.setEmail(newEmail);
		currentHigherStaffUser.setPhone(newPhone);
		currentHigherStaffUser.setActive(isActive);
		currentHigherStaffUser.setNotLocked(true);
		currentHigherStaffUser.setAuthorities(getRoleEnumName(role).getAuthorities());
		currentHigherStaffUser.setRoles(getRoleEnumName(role).name());
		currentHigherStaffUser.setAuthorities(getRoleEnumName(role).getAuthorities());
		higherStaffRepository.save(currentHigherStaffUser);
		saveProfileImage(currentHigherStaffUser, profileImage);
		return currentHigherStaffUser;
	}

	@Override
	public void deleteStaffMember(long id) {
		higherStaffRepository.deleteById(id);
	}

	@Override
	public void resetPassword(String email) throws EmailNotFoundException {
		HigherStaff higherStaff = higherStaffRepository.findStaffMemberByEmail(email);
		if(higherStaff == null){
			throw new EmailNotFoundException(ApplicationConstants.NO_USER_FOUND_BY_EMAIL + email);
		}

		String password = GeneratePassword();
		higherStaff.setPassword(EncodePassword(password));
		higherStaffRepository.save(higherStaff);
		emailService.sendNewPasswordEmail(higherStaff.getName(), password, higherStaff.getEmail());

	}

	@Override
	public HigherStaff updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
		HigherStaff higherStaff = validateNewUsernameAndEmail(username, null, null);
		saveProfileImage(higherStaff, profileImage);
		return higherStaff;
	}

	private void saveProfileImage(HigherStaff higherStaff, MultipartFile profileImage) throws IOException {
		if(profileImage != null){
			Path userFolder = Paths.get(FileConstant.USER_FOLDER + higherStaff.getUsername()).toAbsolutePath().normalize();
			if(!Files.exists(userFolder)){
				Files.createDirectories(userFolder);
				LOGGER.info(FileConstant.DIRECTORY_CREATED + userFolder);
			}

			Files.deleteIfExists(Paths.get(userFolder + higherStaff.getUsername()
					+ FileConstant.DOT + FileConstant.JPG_EXTENTION));
			Files.copy(profileImage.getInputStream(),userFolder.resolve(higherStaff.getUsername()
					+ FileConstant.DOT + FileConstant.JPG_EXTENTION), REPLACE_EXISTING);
			higherStaff.setProfileImageUrl(setProfileImageUrl(higherStaff.getUsername()));
			higherStaffRepository.save(higherStaff);
			LOGGER.info(FileConstant.FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
		}
	}

	private String setProfileImageUrl(String username) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.USER_IMAGE_PATH + username +
				FileConstant.FORWARD_SLASH
		+ username + FileConstant.DOT + FileConstant.JPG_EXTENTION).toUriString();
	}

	private Role getRoleEnumName(String role) {
		return Role.valueOf(role.toUpperCase());
	}

	// This method will generate the user id.
	private String GenerateUserId() {
		return RandomStringUtils.randomNumeric(10);// Generate some random string with 10 number length.
	}


	// This method will generate the password.
	private String GeneratePassword() {
		return RandomStringUtils.randomAlphanumeric(10);
	}

	//This method will encode the password.
	private String EncodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	private String GetTemporaryProfileImageUrl(String username) {
	/*
		'ServletUriComponentsBuilder.fromCurrentContextPath()' will return the url of the source.
	*/
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.DEFAULT_USER_IMAGE_PATH + username).toUriString();
	}

	// This method will validate the username and email for user.
	private HigherStaff validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {

		HigherStaff userByNewUsername = findStaffByUsername(newUsername); // This is the username which user is give as a new username.
		HigherStaff userByNewEmail = findStaffByEmail(newEmail);

		if(StringUtils.isNoneBlank(currentUsername)){//Means, the user is pass a username.
			HigherStaff currentUser = findStaffByUsername(currentUsername);//Find the username that user is already passed
			if(currentUser == null){ // If it is null
				throw new UserNotFoundException(ApplicationConstants.NO_USER_FOUND_BY_USERNAME +currentUsername); // Then username will not found.
			}

			if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())){
				/*
				* If, the newly given username is not null and that user's user id is not equals to one of the existed user's
				* id, then user is already existed. Means, if the user is a brand new user and his given username is already
				* exist, that username cannot take to new user.
				*/
				throw new UsernameExistException(ApplicationConstants.USERNAME_ALREADY_EXIST);
			}

			if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())){
				/*
				 * Same case of the new user's username.
				 */
				throw new EmailExistException(ApplicationConstants.EMAIL_ALREADY_EXIST);
			}

			return currentUser;

		} else {
			if(userByNewUsername != null){
				throw new UsernameExistException(ApplicationConstants.USERNAME_ALREADY_EXIST);
			}

			if(userByNewEmail != null){
				throw new EmailExistException(ApplicationConstants.EMAIL_ALREADY_EXIST);
			}

			return null;
		}
	}


	@Override
	public HigherStaff findStaffByUsername(String username) {
		return higherStaffRepository.findStaffMemberByUsername(username);
	}

	@Override
	public HigherStaff findStaffByEmail(String email) {
		return higherStaffRepository.findStaffMemberByEmail(email);
	}
}
