package com.blackbox.ids.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/app")
public class ApplicationController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String listApplications(HttpServletRequest request) {
		return "appList";
	}
}
