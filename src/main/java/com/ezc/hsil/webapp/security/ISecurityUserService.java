package com.ezc.hsil.webapp.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(long id, String token);

}
