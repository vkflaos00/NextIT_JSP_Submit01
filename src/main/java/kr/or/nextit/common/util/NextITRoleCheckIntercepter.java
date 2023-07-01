package kr.or.nextit.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.or.nextit.member.vo.MemberVO;

public class NextITRoleCheckIntercepter extends HandlerInterceptorAdapter{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("memberVO");
		
		if(member == null) {
			logger.info("(NextITRoleCheckIntercepter_preHandle) member is null");
			response.sendRedirect(request.getContextPath()+"/login/none");
			return false;
		}
		
		if("ADMIN".equals(member.getMemRole())){
			return true;
		}
		
		logger.info("(NextITRoleCheckIntercepter) Your Role is not Admin");
		response.sendError(403);
		return false;
	}

}
