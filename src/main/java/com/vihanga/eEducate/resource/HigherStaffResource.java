package com.vihanga.eEducate.resource;

import com.vihanga.eEducate.constant.SecurityConstant;
import com.vihanga.eEducate.domain.HigherStaff;
import com.vihanga.eEducate.domain.HigherStaffPrincipal;
import com.vihanga.eEducate.domain.HttpResponse;
import com.vihanga.eEducate.exceptions.domain.*;
import com.vihanga.eEducate.service.HigherStaffService;
import com.vihanga.eEducate.utility.JWTTokenPorvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.vihanga.eEducate.constant.FileConstant.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@CrossOrigin
@RequestMapping(path = {"/" , "/eeducate"})
public class HigherStaffResource extends ExceptionHandlling{

	private HigherStaffService higherStaffService;
	private AuthenticationManager authenticationManager;
	private JWTTokenPorvider jwtTokenPorvider;

	public static final String EMAIL_SENT = "An Email with the new password is sent to: ";
	public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully. ";

	@Autowired
	public HigherStaffResource(HigherStaffService higherStaffService, AuthenticationManager authenticationManager, JWTTokenPorvider jwtTokenPorvider) {
		this.higherStaffService = higherStaffService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenPorvider = jwtTokenPorvider;
	}

	@PostMapping("/login")
	public ResponseEntity<HigherStaff> login(@RequestBody HigherStaff higherStaff) {
		authenticate(higherStaff.getUsername(), higherStaff.getPassword());// Authenticate the username and password.
		HigherStaff loginUser = higherStaffService.findStaffByUsername(higherStaff.getUsername());// Find the user by username.
		HigherStaffPrincipal staffPrincipal = new HigherStaffPrincipal(loginUser);// Construct user who find by username, with user principles.
		loginUser.setToken(jwtTokenPorvider.generateJwtToken(staffPrincipal));
		loginUser.setLoggedIn(true);
		/*
			HigherStaffPrinciple class is the class which is get all of the user's authorities, username, password and other features.
		*/
		HttpHeaders jwtHeader = getJwtHeader(staffPrincipal);
		return new ResponseEntity<>(loginUser, jwtHeader ,HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<HigherStaff> register(@RequestBody HigherStaff higherStaff) throws EmailExistException, UsernameExistException, UserNotFoundException, MessagingException {
		HigherStaff staffUser =  higherStaffService.register(higherStaff.getNic(),higherStaff.getName(),higherStaff.getUsername(),higherStaff.getEmail(),higherStaff.getPhone());
		return new ResponseEntity<>(staffUser, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<HigherStaff> addNewUser(@RequestParam("nic") String nic,
												  @RequestParam("name") String name,
												  @RequestParam("username") String username,
												  @RequestParam("email") String email,
												  @RequestParam("phone") Long phone,
												  @RequestParam("role") String role,
												  @RequestParam("isActive") String isActive,
												  @RequestParam("isNotLocked") String isNotLocked,
												  @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
		HigherStaff newHigherStaff = higherStaffService.addStaffMember(nic,name,username,email,phone,role,Boolean.parseBoolean(isActive),Boolean.parseBoolean(isNotLocked),profileImage);
		return new ResponseEntity<>(newHigherStaff, HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<HigherStaff> updateUser(@RequestParam("currentUsername") String currentUsername,
												  @RequestParam("nic") String nic,
												  @RequestParam("name") String name,
												  @RequestParam("username") String username,
												  @RequestParam("email") String email,
												  @RequestParam("phone") Long phone,
												  @RequestParam("role") String role,
												  @RequestParam("isActive") String isActive,
												  @RequestParam("isNotLocked") String isNotLocked,
												  @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
		HigherStaff updatedHigherStaff = higherStaffService.updateStaffMember(currentUsername,nic,name,username,email,phone,role,Boolean.parseBoolean(isActive),Boolean.parseBoolean(isNotLocked),profileImage);
		return new ResponseEntity<>(updatedHigherStaff, HttpStatus.OK);
	}

	@GetMapping("/find/{username}")
	public ResponseEntity<HigherStaff> findUser(@PathVariable("username")String username){
		HigherStaff higherStaff = higherStaffService.findStaffByUsername(username);
		return new ResponseEntity<>(higherStaff, HttpStatus.OK);
	}

	@GetMapping("/find/{email}")
	public ResponseEntity<HigherStaff> findUserByEmail(@PathVariable("email")String email){
		HigherStaff higherStaff = higherStaffService.findStaffByEmail(email);
		return new ResponseEntity<>(higherStaff, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<HigherStaff>> getAllUsers(){
		List<HigherStaff> higherStaff = higherStaffService.getStaff();
		return new ResponseEntity<>(higherStaff, HttpStatus.OK);
	}

	@GetMapping("/resetpassword/{email}")
	public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email")String email) throws EmailNotFoundException {
		higherStaffService.resetPassword(email);
		return response(HttpStatus.OK, EMAIL_SENT +email);
	}

	@DeleteMapping("delete/{id}")
	@PreAuthorize("hasAuthority('user:delete')")
	public ResponseEntity<HttpResponse> deleteUsers(@PathVariable("id") Long id){
		higherStaffService.deleteStaffMember(id);
		return response(HttpStatus.NO_CONTENT, USER_DELETED_SUCCESSFULLY);
	}

	@PostMapping("/updateProfileImage")
	public ResponseEntity<HigherStaff> updateProfileImage(@RequestParam("username") String username, @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
		HigherStaff higherStaff = higherStaffService.updateProfileImage(username,profileImage);
		return new ResponseEntity<>(higherStaff, HttpStatus.OK);
	}

	/*
	* Find the url that where the user's image is located. Means, when user register their account, their image will save at the
	* '/supportportal/staff/' local storage. So, we have to get it. To do that, we should implement this method.
	*/
	@GetMapping(path = "/image/{username}/{filename}", produces = IMAGE_JPEG_VALUE)
	public byte[] getProfileImage(@PathVariable("username")String username, @PathVariable("filename")String filename) throws IOException {
		return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + filename));
	}

	// Get the avatar image from 'https://robohash.org/' api.
	@GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
	public byte[] getTemporaryProfileImage(@PathVariable("username")String username) throws IOException {
		URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
		// Below line is convert url to bytes.
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try(InputStream inputStream = url.openStream()){
			int bytesRead;
			// Byte is cannot read at one time. Because that is to large. For that, we have to split to read.
			byte[] chunk = new byte[1024];
			while((bytesRead = inputStream.read(chunk)) > 0){
				byteArrayOutputStream.write(chunk,0, bytesRead);
				//Meaning - byte is 'byteRead', start from '0', read 1024(chunk) amount of bytes.

			}
		}
		return byteArrayOutputStream.toByteArray();
	}

	private void authenticate(String username, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
	}

	private HttpHeaders getJwtHeader(HigherStaffPrincipal staffPrincipal) {
		HttpHeaders headers = new HttpHeaders();
		/*
			Using staff principles, we can generate jwt token, and in here we can add token and token header together.
		*/
		headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenPorvider.generateJwtToken(staffPrincipal));
		return headers;
	}

	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		HttpResponse body = new HttpResponse(httpStatus.value(),httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());
		return new ResponseEntity<>(body,httpStatus);
	}
}
