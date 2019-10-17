package com.ezc.hsil.webapp.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.persistance.dao.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {

    @Autowired
    private UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
//    private PersistentTokenRepository tokenRepository = new InMemoryTokenRepositoryImpl();
    private PersistentTokenRepository tokenRepository = new JdbcTokenRepositoryImpl() ;
    private String key;

    public CustomRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
        this.key = key;
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        String username = ((Users) successfulAuthentication.getPrincipal()).getUserId();//.getEmail();
        logger.debug("Creating new persistent login for user " + username);
        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, generateSeriesData(), generateTokenData(), new Date());
        try {
            tokenRepository.createNewToken(persistentToken);
            addCookie(persistentToken, request, response);
        } catch (Exception e) {
            logger.error("Failed to save persistent token ", e);
        }
    }

    @Override
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
       // Users auser = userRepository.findByEmail(user.getUsername());
    	Users auser = userRepository.findByUserId(user.getUsername());
        RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(key, auser, authoritiesMapper.mapAuthorities(user.getAuthorities()));
        auth.setDetails(authenticationDetailsSource.buildDetails(request));
        return auth;
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[] { token.getSeries(), token.getTokenValue() }, getTokenValiditySeconds(), request, response);
    }

	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.processAutoLoginCookie(cookieTokens, request, response);
			}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
			//super.logout(request, response, authentication);
			cancelCookie(request, response);
			if (authentication != null) {
				//log.info("authentication:::::{}", authentication.getName());	
				
				tokenRepository.removeUserTokens(((Users)authentication.getPrincipal()).getUserId());
			}
		}

		
	
	
    
    
} 