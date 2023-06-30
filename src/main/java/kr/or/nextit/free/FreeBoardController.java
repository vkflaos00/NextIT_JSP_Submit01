package kr.or.nextit.free;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.or.nextit.attach.vo.AttachVO;
import kr.or.nextit.code.service.ICommCodeService;
import kr.or.nextit.code.vo.CodeVO;
import kr.or.nextit.common.util.NextITFileUpload;
import kr.or.nextit.common.valid.FreeFrom;
import kr.or.nextit.common.valid.FreeModify;
import kr.or.nextit.common.vo.ResultMessageVO;
import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.BizPasswordNotMatchedException;
import kr.or.nextit.free.service.IFreeBoardService;
import kr.or.nextit.free.vo.FreeBoardSearchVO;
import kr.or.nextit.free.vo.FreeBoardVO;


@Controller
@RequestMapping("/free")
public class FreeBoardController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private NextITFileUpload nextITFileUpload;
	
	@Resource(name="codeService")
	private ICommCodeService codeService;
	
	@ModelAttribute("categoryList")
	public List<CodeVO>	categoryList(){
		return codeService.getCodeListByParent("BC00");
	}
	
	@Resource(name="freeBoardService")
	private IFreeBoardService freeBoardService;	
	
	@RequestMapping("/freeList")
	public String freeList(@ModelAttribute("searchVO") FreeBoardSearchVO searchVO, Model model) {
		try{
			List<FreeBoardVO> freeBoardList = freeBoardService.getBoardList(searchVO);
			model.addAttribute("freeBoardList", freeBoardList);
		}catch(BizNotEffectedException bne){
			model.addAttribute("bne", bne);
			bne.printStackTrace();
		}catch(Exception de){
			model.addAttribute("de", de);
			de.printStackTrace();
		}
		return "free.freeList";
	}
	

	@RequestMapping("/freeForm")
	public String freeForm(@ModelAttribute("freeBoard") FreeBoardVO freeBoard ) {
		return "free.freeForm";
	}
	
	@RequestMapping("/freeRegister")
	public String freeRegister( @Validated(value = FreeFrom.class) @ModelAttribute("freeBoard") FreeBoardVO freeBoard
			, BindingResult error 
			,Model model
			,ResultMessageVO resultMessageVO
			,MultipartHttpServletRequest mRequest
			) {
		if(error.hasErrors()) {
			return "free.freeForm";
		}

		List<MultipartFile> fileList = mRequest.getFiles("boFiles");
		MultipartFile[] boFiles = new MultipartFile[fileList.size()];
		for(int i =0 ; i<fileList.size(); i++) {
			boFiles[i] = fileList.get(i);
		}
		
		boolean fileuploadFlag = true;
		if(boFiles != null) {
			 try {
				List<AttachVO>  attachList = nextITFileUpload.fileUpload(boFiles, "FREE", "free");
				if(attachList.size()>0) {
					freeBoard.setAttachList(attachList);
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				fileuploadFlag = false;
			}
		}

		try{
			if(freeBoard.getBoTitle() != null && ! freeBoard.getBoTitle().equals("") ) {
		
				freeBoardService.registerBoard(freeBoard);
			}else {
				throw new Exception();
			}
			
			if(fileuploadFlag) {
				return "redirect:/free/freeList";
			}else {
				resultMessageVO.failSetting(false
						, "파일업로드실패"
						, "게시글은 등록되었으나 파일이 업로드 되지 못하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850");
			}
		}catch(Exception de){
			resultMessageVO.failSetting(false
					, "게시글 등록 실패"
					, "게시글 등록 실패 하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850");
			de.printStackTrace();
		}
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "/common/message";
		
	}

	private String nextITXSScheck(String value) {
		
		StringBuffer strBuff = new StringBuffer();
		for(int i=0; i<value.length(); i++	) {
			char c = value.charAt(i);
			switch (c) {
			case '<':
				strBuff.append("&lt");
				break;
			case '>':
				strBuff.append("&gt");
				break;	
			case '&':
				strBuff.append("&amp");
				break;	
			case '"':
				strBuff.append("&quot");
				break;		
			case '\'':
				strBuff.append("&apos");
				break;		
				
			default:
				strBuff.append(c);
				break;
			}
		}
		
		value = strBuff.toString();
		return value;
	}


	@RequestMapping("/freeView")
	public String freeView(@ModelAttribute("searchVO") FreeBoardSearchVO searchVO, @RequestParam String boNo ,Model model) {
		try{
			FreeBoardVO freeBoard = freeBoardService.getBoard(boNo);
			freeBoardService.increaseHit(boNo);
			model.addAttribute("freeBoard", freeBoard);
		}catch(BizNotEffectedException bne){
			model.addAttribute("bne", bne);
			bne.printStackTrace();
		}catch(Exception de){
			model.addAttribute("de", de);
			de.printStackTrace();
		}
		
		return "free.freeView";
	}
	

	@RequestMapping("/freeEdit")
	public String freeEdit(@ModelAttribute("searchVO") FreeBoardSearchVO searchVO, @RequestParam String boNo, Model model) {
		try{
			FreeBoardVO freeBoard = freeBoardService.getBoard(boNo);
			freeBoardService.increaseHit(boNo);
			System.out.println("freeBoard: "+ freeBoard.toString());
			model.addAttribute("freeBoard", freeBoard);
		}catch(BizNotEffectedException bne){
			model.addAttribute("bne", bne);
			bne.printStackTrace();
		}catch(Exception de){
			model.addAttribute("de", de);
			de.printStackTrace();
		}
		return "free.freeEdit";
	}
	
	@RequestMapping("/freeModify")
	public String freeModify(
			@Validated(value = FreeModify.class) @ModelAttribute("freeBoard") FreeBoardVO freeBoard
			,BindingResult error
			, Model model
			, ResultMessageVO resultMessageVO
			,@RequestParam(required = false)MultipartFile[] boFiles
			
			) {

		if(error.hasErrors()) {
			return "free.freeEdit";
		}
		
		boolean fileuploadFlag = true;
		if(boFiles != null) {
			 try {
				List<AttachVO>  attachList = nextITFileUpload.fileUpload(boFiles, "FREE", "free");
				if(attachList.size()>0) {
					freeBoard.setAttachList(attachList);
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				fileuploadFlag = false;
			}
		}
		
		
		try{
			if( freeBoard.getBoNo()!= null && ! freeBoard.getBoNo().equals("") ) {
				freeBoardService.modifyBoard(freeBoard);
			}else {
				throw new Exception();
			}
			//return "redirect:/free/freeView?boNo="+freeBoard.getBoNo();
			
			
			if(fileuploadFlag) {
				return "redirect:/free/freeView?boNo="+freeBoard.getBoNo();
			}else {
				resultMessageVO.failSetting(false
						, "파일업로드실패"
						, "게시글은 수정되었으나 파일이 업로드 되지 못하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850");
			}
			
			
			
		}catch(BizPasswordNotMatchedException bpn){
			resultMessageVO.failSetting(false
					, "게시글 수정 실패"
					, "입력하신 비밀번호가 올바르지 않습니다. 다시 입력해주세요");
			bpn.printStackTrace();
		}catch(Exception de){
			resultMessageVO.failSetting(false
					, "게시글 수정 실패"
					, "게시글 수정 실패 하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850");
			de.printStackTrace();
		}
		
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "/common/message";
	}

	
	
	@RequestMapping("/freeDelete")
	public String freeDelete(@ModelAttribute FreeBoardVO freeBoard
			, Model model
			, ResultMessageVO resultMessageVO) {
		try{
			if( freeBoard.getBoNo()!= null && ! freeBoard.getBoNo().equals("") ) {
				freeBoardService.deleteBoard(freeBoard);
			}else {
				throw new Exception();
			}
			return "redirect:/free/freeList";
			
		}catch(BizPasswordNotMatchedException bpn){
			resultMessageVO.failSetting(false
					, "게시글 삭제 실패"
					, "입력하신 비밀번호가 올바르지 않습니다. 다시 입력해주세요");
			bpn.printStackTrace();
		}catch(Exception de){
			resultMessageVO.failSetting(false
					, "게시글 삭제 실패"
					, "게시글 삭제 실패 하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850");
			de.printStackTrace();
		}
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "/common/message";
	}
	

	@RequestMapping("/freeHide")
	public String freeHide(@RequestParam String memId
			, @RequestParam String boNo
			, Model model
			, ResultMessageVO resultMessageVO) {
		try{
			if( boNo != null && ! boNo.equals("") ) {   
				freeBoardService.hideBoard(memId, boNo);
			}else {
				throw new BizNotEffectedException();
			}
			return "redirect:/free/freeList";
		}catch(Exception de){
			resultMessageVO.failSetting(false
					, "게시글 숨김 실패"
					, "");
			de.printStackTrace();
		}
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "/common/message";
		
	}
	
}