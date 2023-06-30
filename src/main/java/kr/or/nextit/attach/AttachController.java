package kr.or.nextit.attach;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.nextit.attach.service.IAttachService;
import kr.or.nextit.attach.vo.AttachVO;
import kr.or.nextit.exception.BizNotEffectedException;

@Controller
public class AttachController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IAttachService attachService;
	
	@RequestMapping("/attach/download/{atchNo:^[0-9]+$}")
	public void attachDownload(@PathVariable int atchNo
			, HttpServletResponse resp) throws Exception {
		logger.info("AttachController attachDownload atchNo : "+ atchNo);
		
		 AttachVO attach = attachService.getAttach(atchNo);
		 String originalName = attach.getAtchOriginalName();
		 originalName = URLEncoder.encode(originalName, "utf-8");
		 System.out.println("originalName: "+ originalName);
		 String filePath = attach.getAtchPath();
		 String fileName = attach.getAtchFileName();
		 
		 File file = new File(filePath, fileName);
		 
		 if(file.isFile()) {
			 resp.setHeader("Content-type", "application/octect-stream");
			 resp.setHeader("Content-Transfer-Encoding", "binary");
			 resp.setHeader("Expires", "-1");
			 
			 FileInputStream fis = new FileInputStream(file);
			 FileCopyUtils.copy(fis, resp.getOutputStream());
			 
			 attachService.increaseDownHit(atchNo);
			 
		 }else {
			 printMessage(resp, "해당 첨부파일이 존재 하지 않습니다."
					 	+" 전산실에 문의 부탁드립니다.042-719-8850");
		 }
	}

	private void printMessage(HttpServletResponse resp, String msg) throws IOException {
		
		resp.setCharacterEncoding("utf-8");
		resp.setHeader("Content-type", "text/html; charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		out.println("<script>");
		out.println(" alert('"+ msg +"')");
		out.println(" self.close()");
		out.println("</script>");
		out.println("<h4>첨부파일 문제 :" + msg + "</h4>");
		out.println("<button onclick='self.close()>닫기</button>");
		
		
		
	}
}
