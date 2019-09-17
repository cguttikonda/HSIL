package com.ezc.hsil.webapp.service;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.ezc.hsil.webapp.dto.RequestDetailDto;
import com.ezc.hsil.webapp.dto.UserDto;
import com.ezc.hsil.webapp.error.UserAlreadyExistException;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.PasswordResetToken;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.model.VerificationToken;

public interface IUserService {

	Users registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

	Users getUser(String verificationToken);

	void saveRegisteredUser(Users user);

	void deleteUser(Users user);

	void createVerificationTokenForUser(Users user, String token);

	VerificationToken getVerificationToken(String VerificationToken);

	VerificationToken generateNewVerificationToken(String token);

	void createPasswordResetTokenForUser(Users user, String token);

	Users findUserByEmail(String email);
	
	Users findUserByUserId(String userId);
	
	Users findUserByUserIdOrEmail(String userId,String email);
	

	PasswordResetToken getPasswordResetToken(String token);

	Users getUserByPasswordResetToken(String token);

	Optional<Users> getUserByID(long id);

	void changeUserPassword(Users user, String password);

	boolean checkIfValidOldPassword(Users user, String password);

	String validateVerificationToken(String token);

	String generateQRUrl(Users user) throws UnsupportedEncodingException;

	Users updateUser2FA(boolean use2FA);

	List<String> getUsersFromSessionRegistry();
	
	

	//void addReqData(Optional<EzcRequestHeader> reqHeader);
	Optional<EzcRequestHeader> findById_O(int id);
	
	EzcRequestHeader findById(int id);

	void addReqData(HashSet<EzcRequestItems> hsItems);

	void addReqData(EzcRequestItems ezcRequestItemses);
	
	EzcRequestHeader findDetailsById(int id);
	
	

}
