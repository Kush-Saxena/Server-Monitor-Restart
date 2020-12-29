package com.cg.servermonitorrest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cg.servermonitorrest.services.web.WebHelperService;

@Controller
public class ConfigController {

	@Autowired
	private WebHelperService webHelper;

	@PostMapping("/editMail")
	String addToMailList(@RequestParam String mailId) {
		Boolean isUpdateSuccess = webHelper.updateMailingList(mailId);

		if (isUpdateSuccess)
			return "successPage";
		return "failPage";
	}

	@PostMapping("/editMonitor")
	String addToServiceList(@RequestParam String serviceId) {
		Boolean isUpdateSuccess = webHelper.updateServiceList(serviceId);
		if (isUpdateSuccess)
			return "successPage";
		return "failPage";
	}

}
