package kr.or.nextit.member;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.or.nextit.attach.vo.AttachVO;
import kr.or.nextit.code.service.ICommCodeService;
import kr.or.nextit.common.util.NextITFileUpload;
import kr.or.nextit.common.valid.MemberRegister;
import kr.or.nextit.common.vo.ResultMessageVO;
import kr.or.nextit.common.vo.RoleInfoVO;
import kr.or.nextit.exception.BizDuplicateKeyException;
import kr.or.nextit.exception.BizMailAuthException;
import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.BizNotFoundException;
import kr.or.nextit.member.service.IMemberService;
import kr.or.nextit.member.vo.MemberSearchVO;
import kr.or.nextit.member.vo.MemberVO;

@Controller
public class MemberController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ICommCodeService codeService;

	@Resource(name = "memberService")
	private IMemberService memberService;

	@Autowired
	private NextITFileUpload nextITFileUpload;
	
	@Transactional
	@RequestMapping(value = "/member/memberRegister", method = RequestMethod.POST)
	public String memberRegister(
	        @Validated(value = MemberRegister.class) @ModelAttribute("member") MemberVO member,
	        BindingResult error, Model model, ResultMessageVO resultMessageVO,
	        @RequestParam(required = false) MultipartFile[] profilePhoto) throws Exception {

	    System.out.println("MemberController memberRegister member.toString(): " + member.toString());

	    if (error.hasErrors()) {
	        return "/login/join";
	    }

	    boolean fileuploadFlag = false;

	    if (profilePhoto != null) {
	        try {
	            List<AttachVO> attachList = nextITFileUpload.fileUpload(
	                    profilePhoto, "PROFILEPHOTO", "profilePhoto");
	            if (attachList.size() > 0) {
	                member.setAttachList(attachList);
	                fileuploadFlag = true;  // 파일 업로드 성공
	            } else {
	                // 파일 업로드 실패한 경우
	                throw new BizNotEffectedException("File upload failed.");
	            }
	        } catch (IllegalStateException | IOException e) {
	            e.printStackTrace();
	            // 파일 업로드 실패한 경우
	            throw new BizNotEffectedException("File upload failed.");
	        }
	    }

	    try {
	        if (member.getMemId() != null && !member.getMemId().equals("")) {
	            memberService.registerMember(member);
	        } else {
	            throw new Exception();
	        }
	        if (fileuploadFlag) {
	            return "redirect:/login/sign";
	        } else {
	            resultMessageVO.failSetting(false, "회원등록 실패, 프로필 업로드 실패", "");
	        }
	    } catch (BizDuplicateKeyException bde) {
	        bde.printStackTrace();
	        resultMessageVO.failSetting(false, "회원등록실패", "이미 사용중인 아이디 입니다. 다른 아이디를 사용해주세요");
	    } catch (BizNotEffectedException bne) {
	        bne.printStackTrace();
	        resultMessageVO.failSetting(false, "회원등록실패", "회원등록에 실패하였습니다. 전산실에 문의부탁드립니다. 042-719-8850");
	    } catch (Exception de) {
	        de.printStackTrace();
	        resultMessageVO.failSetting(false, "회원등록실패", "회원등록에 실패하였습니다. 전산실에 문의부탁드립니다. 042-719-8850");
	    }

	    model.addAttribute("resultMessageVO", resultMessageVO);
	    return "/common/message";
	}




	@RequestMapping("/member/memberView")
	public String memberView(@RequestParam String memId, Model model) {
		System.out.println("MemberController memberView memId : " + memId);
		try {
			MemberVO member = memberService.getMember(memId);
			member.setMemPass("");
			model.addAttribute("member", member);
		} catch (BizNotEffectedException bne) {
			model.addAttribute("bne", bne);
			bne.printStackTrace();
		} catch (Exception de) {
			model.addAttribute("de", de);
			de.printStackTrace();
		}
		return "member.memberView";
	}

	@RequestMapping("/member/memberList")
	public String memberList(@ModelAttribute("searchVO") MemberSearchVO searchVO, Model model) {
		System.out.println("MemberController memberList");

		try {
			List<MemberVO> memberList = memberService.getMemberList(searchVO);
			model.addAttribute("memberList", memberList);
		} catch (BizNotFoundException bnf) {
			model.addAttribute("bnf", bnf);
			bnf.printStackTrace();
		} catch (Exception de) {
			model.addAttribute("de", de);
			de.printStackTrace();
		}
		return "member.memberList";
	}

	@RequestMapping(value = "/member/memberMultiDelete", method = RequestMethod.POST)
	public String memberMultiDelete(@RequestParam String memMultiId, Model model, ResultMessageVO resultMessageVO) {
		System.out.println("MemberController memberMultiDelete memMultiId: " + memMultiId);
		try {
			if (memMultiId != null && memMultiId.length() > 2) {
				memberService.removeMultiMember(memMultiId);
			} else {
				throw new Exception();
			}
			return "redirect:/member/memberList";

		} catch (BizNotEffectedException bne) {
			bne.printStackTrace();
			resultMessageVO.failSetting(false, "회원삭제 실패", "");
		} catch (Exception de) {
			de.printStackTrace();
			resultMessageVO.failSetting(false, "회원삭제 실패", "");
		}

		model.addAttribute("resultMessageVO", resultMessageVO);
		return "/common/message";
	}

	@RequestMapping("/member/memberRole")
	public String memberRole(@RequestParam String memId, Model model) {
		System.out.println("MemberController memberRole memId :" + memId);

		try {
			MemberVO member = null;
			if (memId != null && !memId.equals("")) {
				member = memberService.getMemberRole(memId);
			} else {
				throw new Exception();
			}
			model.addAttribute("member", member);
			List<RoleInfoVO> roleInfoList = memberService.getRoleInfo();
			model.addAttribute("roleInfoList", roleInfoList);
		} catch (BizNotEffectedException bne) {
			bne.printStackTrace();
			model.addAttribute("bne", bne);
		} catch (Exception de) {
			de.printStackTrace();
			model.addAttribute("de", de);
		}
		return "member.memberRole";

	}

	@RequestMapping(value = "/member/memberRoleUpdate", method = RequestMethod.POST)
	public String memberRoleUpdate(@RequestParam String memId,
			@RequestParam(required = false, name = "userRole") String[] roles, Model model,
			ResultMessageVO resultMessageVO) {
		System.out
				.println("MemberController memberRoleUpdate memId : " + memId + ", roles : " + Arrays.toString(roles));
		try {
			if (memId != null && !memId.equals("")) {
				memberService.updateUserRole(memId, roles);
			} else {
				throw new Exception();
			}
			return "redirect:/member/memberList";

		} catch (Exception de) {
			resultMessageVO.failSetting(false, "회원 권한 수정 실패", "");
			de.printStackTrace();
		}

		model.addAttribute("resultMessageVO", resultMessageVO);
		return "/common/message";
	}

	@RequestMapping("/member/memberGrid")
	@ResponseBody
	public MemberSearchVO memberGrid(@ModelAttribute MemberSearchVO searchVO) {
		logger.info("memberGrid searchVO.toString():" + searchVO.toString());

		try {
			List<MemberVO> memberList = memberService.getMemberList(searchVO);
			searchVO.setMemberList(memberList);
		} catch (BizNotFoundException e) {
			e.printStackTrace();
		}
		return searchVO;
	}

	@RequestMapping("/member/memberGridUpdate")
	@ResponseBody
	public boolean memberGridUpdate(@RequestBody Map<String, Object> map) {
		logger.info("memberGridUpdate memId : " + map.get("memId"));

		String memId = (String) map.get("memId");
		String memName = (String) map.get("memName");
		String memRole = (String) map.get("memRole");
		String memMail = (String) map.get("memMail");

		MemberVO member = new MemberVO();
		member.setMemId(memId);
		member.setMemName(memName);
		member.setMemRole(memRole);
		member.setMemMail(memMail);

		boolean result = memberService.memberGridUpdate(member);

		return result;
	}

	@RequestMapping("/member/memberGridMultiDelete")
	@ResponseBody
	public boolean memberGridMultiDelete(@RequestParam(name = "memIdArr[]") List<String> memId_arr) {
		logger.info("memId_arr : " + memId_arr);

		boolean result = false;
		if (memId_arr.size() > 0) {
			result = memberService.memberGridMultiDelete(memId_arr);
		}
		return result;
	}

}
