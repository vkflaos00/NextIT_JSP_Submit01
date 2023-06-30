package com.nextit.www;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nextit.www.service.NextitApiService;

import kr.or.nextit.code.service.ICommCodeService;

@Controller
public class NextITApiController {
	
	@Autowired
	private NextitApiService apiService;
	
	@Resource(name="codeService")
	private ICommCodeService codeService;
	
	
	
	@RequestMapping("/apiJoin.do")
	public String apiJoin(Model model) {
		
		return "/login/join";
	}

}
