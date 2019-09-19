package com.ezc.hsil.webapp.error;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandling {

	public GlobalExceptionHandling() {
		 
		
	}
	
	@ExceptionHandler(RequestNotFound.class)
	public String databaseError(Exception exception, Model mav,HttpServletRequest  req) {
		// Nothing to do. Return value 'databaseError' used as logical view name
		// of an error page, passed to view-resolver(s) in usual way.
		log.error("Request raised " + exception.getClass().getSimpleName());
		//model.add
		log.error("Request: " + req.getRequestURI() + " raised " + exception);

		
		mav.addAttribute("exception", exception);
		mav.addAttribute("url", req.getRequestURL());
		mav.addAttribute("timestamp", new Date().toString());
		mav.addAttribute("status", 500);

		
		return "error/500";
		
	}

	@ExceptionHandler(RuntimeException.class)
	public String runTimeError(Exception exception, Model mav,HttpServletRequest  req) {
		// Nothing to do. Return value 'databaseError' used as logical view name
		// of an error page, passed to view-resolver(s) in usual way.
		log.error("Request raised " + exception.getClass().getSimpleName());
		//model.add
		log.error("Request: " + req.getRequestURI() + " raised " + exception);

		
		mav.addAttribute("exception", exception);
		mav.addAttribute("url", req.getRequestURL());
		mav.addAttribute("timestamp", new Date().toString());
		mav.addAttribute("status", 500);

		
		return "error/500";
		
	}


}
