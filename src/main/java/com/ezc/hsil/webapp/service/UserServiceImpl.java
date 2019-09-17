package com.ezc.hsil.webapp.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.RequestDetailDto;
import com.ezc.hsil.webapp.dto.RequestHeaderDto;
import com.ezc.hsil.webapp.dto.UserDto;
import com.ezc.hsil.webapp.error.UserAlreadyExistException;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.PasswordResetToken;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.model.VerificationToken;
import com.ezc.hsil.webapp.persistance.dao.PasswordResetTokenRepository;
import com.ezc.hsil.webapp.persistance.dao.RequestDetailsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.RoleRepository;
import com.ezc.hsil.webapp.persistance.dao.UserRepository;
import com.ezc.hsil.webapp.persistance.dao.VerificationTokenRepository;

@Service
@Transactional
public class UserServiceImpl implements IUserService{

	 @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private VerificationTokenRepository tokenRepository;

	    @Autowired
	    private PasswordResetTokenRepository passwordTokenRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Autowired
	    private RoleRepository roleRepository;

	    @Autowired
	    private SessionRegistry sessionRegistry;
	    
	    
	    @Autowired
	    private RequestHeaderRepo reqHeaderRepo;
	    
	    @Autowired
	    private RequestDetailsRepo reqDtlRep;
	    

	    public static final String TOKEN_INVALID = "invalidToken";
	    public static final String TOKEN_EXPIRED = "expired";
	    public static final String TOKEN_VALID = "valid";

	    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
	    public static String APP_NAME = "SpringRegistration";

	    // API

	    @Override
	    public Users registerNewUserAccount(final UserDto accountDto) {
	        if (emailExists(accountDto.getEmail())) {
	            throw new UserAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
	        }
	        final Users user = new Users();

	        user.setFirstName(accountDto.getFirstName());
	        user.setLastName(accountDto.getLastName());
	        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
	        user.setEmail(accountDto.getEmail());
	//        user.setUsing2FA(accountDto.isUsing2FA());
	        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
	        return userRepository.save(user);
	    }

	    @Override
	    public Users getUser(final String verificationToken) {
	        final VerificationToken token = tokenRepository.findByToken(verificationToken);
	        if (token != null) {
	            return token.getUser();
	        }
	        return null;
	    }

	    @Override
	    public VerificationToken getVerificationToken(final String VerificationToken) {
	        return tokenRepository.findByToken(VerificationToken);
	    }

	    @Override
	    public void saveRegisteredUser(final Users user) {
	        userRepository.save(user);
	    }

	    @Override
	    public void deleteUser(final Users user) {
	        final VerificationToken verificationToken = tokenRepository.findByUser(user);

	        if (verificationToken != null) {
	            tokenRepository.delete(verificationToken);
	        }

	        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

	        if (passwordToken != null) {
	            passwordTokenRepository.delete(passwordToken);
	        }

	        userRepository.delete(user);
	    }

	    @Override
	    public void createVerificationTokenForUser(final Users user, final String token) {
	        final VerificationToken myToken = new VerificationToken(token, user);
	        tokenRepository.save(myToken);
	    }

	    @Override
	    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
	        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
	        vToken.updateToken(UUID.randomUUID()
	            .toString());
	        vToken = tokenRepository.save(vToken);
	        return vToken;
	    }

	    @Override
	    public void createPasswordResetTokenForUser(final Users user, final String token) {
	        final PasswordResetToken myToken = new PasswordResetToken(token, user);
	        passwordTokenRepository.save(myToken);
	    }

	    @Override
	    public Users findUserByEmail(final String email) {
	        return userRepository.findByEmail(email);
	    }

	    @Override
	    public PasswordResetToken getPasswordResetToken(final String token) {
	        return passwordTokenRepository.findByToken(token);
	    }

	    @Override
	    public Users getUserByPasswordResetToken(final String token) {
	        return passwordTokenRepository.findByToken(token)
	            .getUser();
	    }

	    
	    
	    @Override
	    public Optional<Users> getUserByID(final long id) {
	        return userRepository.findById(id);
	    }

	    @Override
	    public void changeUserPassword(final Users user, final String password) {
	        user.setPassword(passwordEncoder.encode(password));
	        userRepository.save(user);
	    }

	    @Override
	    public boolean checkIfValidOldPassword(final Users user, final String oldPassword) {
	        return passwordEncoder.matches(oldPassword, user.getPassword());
	    }

	    @Override
	    public String validateVerificationToken(String token) {
	        final VerificationToken verificationToken = tokenRepository.findByToken(token);
	        if (verificationToken == null) {
	            return TOKEN_INVALID;
	        }

	        final Users user = verificationToken.getUser();
	        final Calendar cal = Calendar.getInstance();
	        if ((verificationToken.getExpiryDate()
	            .getTime()
	            - cal.getTime()
	                .getTime()) <= 0) {
	            tokenRepository.delete(verificationToken);
	            return TOKEN_EXPIRED;
	        }

	        user.setEnabled(true);
	        // tokenRepository.delete(verificationToken);
	        userRepository.save(user);
	        return TOKEN_VALID;
	    }

	    @Override
	    public String generateQRUrl(Users user) throws UnsupportedEncodingException {
	        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), APP_NAME), "UTF-8");
	    }

	    @Override
	    public Users updateUser2FA(boolean use2FA) {
	        final Authentication curAuth = SecurityContextHolder.getContext()
	            .getAuthentication();
	        Users currentUser = (Users) curAuth.getPrincipal();
	     //   currentUser.setUsing2FA(use2FA);
	        currentUser = userRepository.save(currentUser);
	        final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(), curAuth.getAuthorities());
	        SecurityContextHolder.getContext()
	            .setAuthentication(auth);
	        return currentUser;
	    }

	    private boolean emailExists(final String email) {
	        return userRepository.findByEmail(email) != null;
	    }

	    @Override
	    public List<String> getUsersFromSessionRegistry() {
	        return sessionRegistry.getAllPrincipals()
	            .stream()
	            .filter((u) -> !sessionRegistry.getAllSessions(u, false)
	                .isEmpty())
	            .map(o -> {
	                if (o instanceof Users) {
	                    return ((Users) o).getEmail();
	                } else {
	                    return o.toString();
	                }
	            })
	            .collect(Collectors.toList());

	    }

		@Override
		public Users findUserByUserId(String userId) {

		
			return userRepository.findByUserId(userId);
		
		
		}

		@Override
		public Users findUserByUserIdOrEmail(String userId, String email) {
			// TODO Auto-generated method stub
			return userRepository.findByUserIdOrEmail(userId,email);
		}

		//@Override
//		public void addReqData(EzcRequestHeader reqHeader) {
//		
//			
//			reqHeaderRepo.save(reqHeader);
//			
//
//		}

		@Override
		public Optional<EzcRequestHeader> findById_O(int id) {
			// TODO Auto-generated method stub
			return reqHeaderRepo.findById(id);
		}

		@Override
		public void addReqData(HashSet<EzcRequestItems> ezcRequestItemses) {
			
			//System.out.print("reqHeader.get()::::::"+reqHeader.get());
			//reqHeaderRepo.save(reqHeader); 
			Optional<EzcRequestHeader> reqHeader = reqHeaderRepo.findById(5);
			Set<EzcRequestItems> itemSet = new HashSet<EzcRequestItems>();
			
			
			reqHeader.ifPresent(rq->{
				
				rq.setErhModifiedBy("ADMIN_2");
				rq.setErhModifiedOn(new Date());
				//rq.setEzcRequestItemses(ezcRequestItemses);
				ezcRequestItemses.stream().forEach(items->{
					
					if(!"".equals(items.getEriPlumberName())) {
						itemSet.add(items);
						rq.setEzcRequestItemses(itemSet);
					}
					
				});
				rq.getEzcRequestItemses()
					.stream()
						.forEach(rt->{
								rt.setEzcRequestHeader(rq);
					
						});
				
				});
			
			
//			if(reqHeader.isPresent()) {
//				
//				reqHeader.get().setErhModifiedBy("ADMIN_2");
//				reqHeader.get().setErhModifiedOn(new Date());
//				
//				//reqHeader.map()  
//	    		
//	    	//	reqHeaderRepo.save(reqHeader);
//	    		
//	    		
//				Set<EzcRequestItems> dtlSet =   reqHeader.get().getEzcRequestItemses();
//	//			dtlSet.stream()
//	//				.forEach(reqItems->System.out.print("reqItems::::::"+reqItems.getEriDoa()));
//	//			
//				//System.out.println();
//				for(EzcRequestItems rt: ezcRequestItemses) {
//				
//					rt.setEzcRequestHeader(reqHeader.get());
//					dtlSet.add(rt);
//					
//			//		reqDtlRep.save(rt);
//				}
//				reqHeader.get().setEzcRequestItemses(dtlSet);
//			}
//			//reqHeaderRepo.save(reqHeader);
			
		}

		@Override
		public void addReqData(EzcRequestItems ezcRequestItemses) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public EzcRequestHeader findById(int id) {
			// TODO Auto-generated method stub
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public EzcRequestHeader findDetailsById(int id) {

			
			return reqHeaderRepo.getById(id);
				
			
			
			//return reqHeaderRepo.findById(id).get().getEzcRequestItemses().stream().collect(Collectors.toList())		
			
		}
		
		
		
		
		
		
		
		
		

}
