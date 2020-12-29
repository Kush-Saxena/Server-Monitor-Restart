package com.cg.servermonitorrest.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cg.servermonitorrest.resources.SharedVariables;

@Controller
public class MainController {

	@GetMapping(path = "/welcome")
	public String getWelcomePage(Model m) {
		List<String> service = SharedVariables.serviceList;
		List<String> mails = SharedVariables.mailRecipients;
		m.addAttribute("ServiceList", service);
		m.addAttribute("MailList", mails);
		return "welcomePage";
	}
}
