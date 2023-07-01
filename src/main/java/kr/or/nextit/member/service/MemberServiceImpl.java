package kr.or.nextit.member.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.nextit.attach.mapper.IAttachMapper;
import kr.or.nextit.attach.vo.AttachVO;
import kr.or.nextit.common.vo.RoleInfoVO;
import kr.or.nextit.common.vo.UserRoleVO;
import kr.or.nextit.exception.BizDuplicateKeyException;
import kr.or.nextit.exception.BizMailAuthException;
import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.BizNotFoundException;
import kr.or.nextit.exception.BizPasswordNotMatchedException;
import kr.or.nextit.member.mapper.IMemberMapper;
import kr.or.nextit.member.vo.MailAuthVO;
import kr.or.nextit.member.vo.MemberSearchVO;
import kr.or.nextit.member.vo.MemberVO;

@Service("memberService")
public class MemberServiceImpl implements IMemberService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	private IMemberMapper memMapper;

	@Autowired
	private IAttachMapper attachMapper;

	@Inject
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional 
	public void registerMember(MemberVO member)
			throws BizDuplicateKeyException, BizNotEffectedException, BizMailAuthException {
		logger.info("void registerMember");

		if (member.getMemId() != null && !member.getMemId().equals("")) {

			MemberVO vo = memMapper.getMember(member.getMemId());
			if (vo != null) {
				throw new BizDuplicateKeyException();
			}

			int rowCount = memMapper.checkMailAuth(member.getMemMail());
			if (rowCount != 1) {
				throw new BizMailAuthException();
			}

			String encodePw = passwordEncoder.encode(member.getMemPass());
			logger.info("encodePw : " + encodePw);
			member.setMemPass(encodePw);

			int resultCnt = memMapper.insertMember(member);
			if (resultCnt != 1) {
				throw new BizNotEffectedException();
			}
		}

		List<AttachVO> attachList = member.getAttachList();
		if (attachList != null && attachList.size() > 0) {
			for (AttachVO attch : attachList) {
				attch.setAtchParentNo(member.getMemId());
				attch.setAtchRegId(member.getMemId());

				attachMapper.insertAttach(attch);
			}
		}

	}


	@Override
	public boolean loginCheck(MemberVO member, HttpServletRequest request, HttpServletResponse response)
			throws BizNotEffectedException {
		MemberVO vo = null;
		if (member.getMemId() == null || member.getMemId().equals("")) {
			return false;
		} else {
			vo = memMapper.loginCheck(member);
		}
		try {
			if (vo == null) {
				System.out.println("do not get member info ");
				return false;
			} else {

				boolean match = passwordEncoder.matches(member.getMemPass(), vo.getMemPass());
				logger.info("match : " + match);
				if (!match) {
					return false;
				}

				System.out.println("success login");

				List<UserRoleVO> userRoleList = memMapper.getUserRole(member);
				if (userRoleList != null) {
					vo.setUserRoleList(userRoleList);
				}

				HttpSession session = request.getSession();
				session.setAttribute("memberVO", vo);

				String rememberMe = member.getRememberMe();
				if (rememberMe != null && rememberMe.equals("Y")) {
					System.out.println("rememberMe is Y");
					Cookie cookie = new Cookie("rememberMe", member.getMemId());
					cookie.setMaxAge(60 * 60 * 24);
					cookie.setHttpOnly(true);
					response.addCookie(cookie);
				} else {
					Cookie cookie = new Cookie("rememberMe", "");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
				return true;
			}
		} catch (Exception e) {
			throw new BizNotEffectedException();
		}
	}

	@Override
	public MemberVO getMember(String memId) throws BizNotEffectedException {
		MemberVO member = null;
		if (memId != null && !memId.equals("")) {
			member = memMapper.getMember(memId);
		}
		if (member == null) {
			throw new BizNotEffectedException();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memId", memId);
		map.put("atchCategory", "PROFILEPHOTO");

		Integer atchNo = attachMapper.getAttachNo(map);

		member.setAtchNo(atchNo);

		return member;

	}

	@Override
	public void modifyMember(MemberVO member)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		MemberVO vo = null;
		if (member.getMemId() != null && !member.getMemId().equals("")) {
			vo = memMapper.getMember(member.getMemId());
		}

		if (vo == null) {
			throw new BizNotFoundException();
		}

		boolean match = passwordEncoder.matches(member.getMemPass(), vo.getMemPass());
		logger.info("match : " + match);
		if (!match) {
			throw new BizPasswordNotMatchedException();
		}

		String encodedPw = passwordEncoder.encode(member.getMemPassNew());
		member.setMemPassNew(encodedPw);
		int resultCnt = memMapper.updateMember(member);

		if (resultCnt != 1) {
			throw new BizNotEffectedException();
		}

		List<AttachVO> attachList = member.getAttachList();
		if (attachList != null) {
			for (AttachVO attach : attachList) {
				attach.setAtchParentNo(member.getMemId());
				attach.setAtchRegId(member.getMemId());
				attachMapper.insertAttach(attach);
			}
		}
	}

	@Override
	public void removeMember(MemberVO member)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {

		MemberVO vo = null;
		if (member.getMemId() != null && !member.getMemId().equals("")) {
			vo = memMapper.getMember(member.getMemId());
		}

		if (vo == null) {
			throw new BizNotFoundException();
		}
		boolean match = passwordEncoder.matches(member.getMemPass(), vo.getMemPass());
		logger.info("match : " + match);
		if (!match) {
			throw new BizPasswordNotMatchedException();
		}

		int resultCnt = memMapper.deleteMember(member);
		if (resultCnt != 1) {
			throw new BizNotEffectedException();
		}

	}

	@Override
	public List<MemberVO> getMemberList(MemberSearchVO searchVO) throws BizNotFoundException {
		int totalRowCount = memMapper.getTotalRowCount(searchVO);
		searchVO.setTotalRowCount(totalRowCount);
		searchVO.pageSetting();
		System.out.println("searchVO.toString() : " + searchVO.toString());

		List<MemberVO> memberVO = memMapper.getMemberList(searchVO);

		if (memberVO == null) {
			throw new BizNotFoundException();
		}
		return memberVO;
	}

	@Override
	public void removeMultiMember(String memMultiId) throws BizNotEffectedException {
		System.out.println("memMultiId: " + memMultiId);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Object> list = objectMapper.readValue(memMultiId, new TypeReference<List<Object>>() {
			});
			System.out.println("list: " + list);

			if (list.size() == 0) {
				throw new BizNotEffectedException();
			}

			for (int i = 0; i < list.size(); i++) {
				String memId = (String) list.get(i);
				MemberVO member = new MemberVO();
				member.setMemId(memId);
				int resultCnt = memMapper.deleteMember(member);
				if (resultCnt == 0) {
					throw new BizNotEffectedException();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizNotEffectedException();
		}
	}

	@Override
	public MemberVO getMemberRole(String memId) throws BizNotEffectedException {
		System.out.println("MemberServiceImpl memId: " + memId);

		MemberVO member = null;
		if (memId != null && !memId.equals("")) {
			member = memMapper.getMember(memId);
		}
		if (member == null) {
			throw new BizNotEffectedException();
		}

		List<UserRoleVO> userRoleList = memMapper.getUserRole(member);

		if (userRoleList == null) {
			throw new BizNotEffectedException();
		}

		member.setUserRoleList(userRoleList);
		return member;
	}

	@Override
	public List<RoleInfoVO> getRoleInfo() throws BizNotEffectedException {
		List<RoleInfoVO> roleInfoList = memMapper.getRoleInfo();

		if (roleInfoList == null) {
			throw new BizNotEffectedException();
		}
		return roleInfoList;
	}

	@Override
	@Transactional
	public void updateUserRole(String memId, String[] roles) throws BizNotEffectedException {
		System.out.println("roles.length :" + roles.length);
		if (memId != null && !memId.equals("")) {
			memMapper.deleteUserRole(memId);

			if (roles.length > 0) {
				for (int i = 0; i < roles.length; i++) {
					memMapper.insertMultiRole(memId, roles[i]);
				}
			}
		} else {
			throw new BizNotEffectedException();
		}
	}

	@Override
	public boolean idCheck(String memId) {
		int cnt = memMapper.idCheck(memId);
		if (cnt == 0) {
			return true;
		}
		return false;
	}

	@Override
	public void memberExcelUpload(MemberVO member) throws BizNotEffectedException {
		int resultCnt = memMapper.memberExcelUpload(member);
		if (resultCnt != 1) {
			throw new BizNotEffectedException();
		}
	}

	@Override
	public boolean memberGridUpdate(MemberVO member) {
		int resultCnt = memMapper.memberGridUpdate(member);

		boolean result = true;

		if (resultCnt != 1) {
			result = false;
		}

		return result;
	}

	@Override
	public boolean memberGridMultiDelete(List<String> memId_arr) {
		for (String memId : memId_arr) {
			int resulCnt = memMapper.memberGridMultiDelete(memId);
			if (resulCnt != 1) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void registerMailAut(String mail, String key) {

		MailAuthVO mailAuth = memMapper.getMailAuth(mail);
		if (mailAuth == null) {
			memMapper.insertMailAuth(mail, key);
		} else {
			memMapper.updateMailAuth(mail, key);
		}
	}

	@Override
	public boolean authKeyCompare(MailAuthVO mailAuthVO) {
		MailAuthVO vo = memMapper.getMailAuth(mailAuthVO.getMail());

		if (vo == null) {
			return false;
		} else {
			if (vo.getKey().equals(mailAuthVO.getKey())) {
				memMapper.completeAuth(mailAuthVO.getMail());
				return true;
			} else {
				return false;
			}
		}
	}

}