package com.company.temp;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.KakaoAPI;

@Controller
public class LoginController {

	@Autowired
	KakaoAPI kakaoAPI;

	@RequestMapping("/callback")
	public String callback(@RequestParam Map<String, Object> map, HttpSession session) {
		String code = (String) map.get("code");
		String access_token = kakaoAPI.getAccessToken(code);
		System.out.println("엑세스 토큰: " + access_token);
		
		Map<String, Object> userInfo = kakaoAPI.getUserInfo(access_token);
		System.out.println("유저정보:" + userInfo);
		// token을 session저장(DB저장)
		session.setAttribute("access_token", access_token);
		session.setAttribute("loginid", userInfo.get("nickname"));
		return "home";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		// 세션에서 token 읽어서
		String access_token = (String) session.getAttribute("access_token");
		kakaoAPI.kakaoLogout(access_token);
		System.out.println("로그아웃확인");
		session.invalidate();
		return "home";
	}

}// end of class
