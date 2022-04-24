package com.vihanga.eEducate.service;

import com.vihanga.eEducate.domain.HigherStaff;
import com.vihanga.eEducate.exceptions.domain.EmailExistException;
import com.vihanga.eEducate.exceptions.domain.EmailNotFoundException;
import com.vihanga.eEducate.exceptions.domain.UserNotFoundException;
import com.vihanga.eEducate.exceptions.domain.UsernameExistException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface HigherStaffService {

    //Save the users in database.
    HigherStaff register(String nic, String name, String username, String email, Long phone) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException;

    //Get all users from database
    List<HigherStaff> getStaff();

    HigherStaff addStaffMember(String nic, String name, String username, String email, long phone , String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException;
    HigherStaff updateStaffMember(String currentUsername ,String newNic, String newName, String newUsername, String newEmail, long newPhone , String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException;
    void deleteStaffMember(long id);
    void resetPassword(String email) throws EmailNotFoundException;
    HigherStaff updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException;
    HigherStaff findStaffByUsername(String username);
    HigherStaff findStaffByEmail(String email);

}
