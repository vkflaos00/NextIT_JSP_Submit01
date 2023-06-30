package kr.or.nextit.member.vo;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import kr.or.nextit.attach.vo.AttachVO;
import kr.or.nextit.common.valid.MemberModify;
import kr.or.nextit.common.valid.MemberRegister;
import kr.or.nextit.common.vo.UserRoleVO;

public class MemberVO {

	@Pattern(regexp = "^\\w{2,10}$", message = "아이디 영문, 숫자 조합 2~10자", groups = MemberRegister.class)
	private String memId;

	@Pattern(regexp = "^\\w{2,10}$", message = "패스워드 영문, 숫자 조합 2~10자", groups = { MemberRegister.class,
			MemberModify.class })
	private String memPass;
	@Pattern(regexp = "^\\w{2,10}$", message = "패스워드 영문, 숫자 조합 2~10자", groups = { MemberRegister.class,
			MemberModify.class })
	private String memPassNew;

	@Size(min = 1, max = 10, message = "이름 10자 이하", groups = { MemberRegister.class, MemberModify.class })
	private String memName;

	private String joinDate;
	private int dDay;
	private int money;
	private String memRole;

	@NotEmpty(message = "이메일 입력")
	@Email(message = "이메일 형식 틀림")
	private String memMail;

	private String memDelYn;
	private String rememberMe;
	private List<String> roleList;
	private List<UserRoleVO> userRoleList;
	private String rnum;
	private List<AttachVO> attachList;
	private Integer atchNo;

	public MemberVO() {
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public String getMemPass() {
		return memPass;
	}

	public void setMemPass(String memPass) {
		this.memPass = memPass;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public int getdDay() {
		return dDay;
	}

	public void setdDay(int dDay) {
		this.dDay = dDay;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getMemRole() {
		return memRole;
	}

	public void setMemRole(String memRole) {
		this.memRole = memRole;
	}

	public String getMemMail() {
		return memMail;
	}

	public void setMemMail(String memMail) {
		this.memMail = memMail;
	}

	public String getMemDelYn() {
		return memDelYn;
	}

	public void setMemDelYn(String memDelYn) {
		this.memDelYn = memDelYn;
	}

	public String getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(String rememberMe) {
		this.rememberMe = rememberMe;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public List<UserRoleVO> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<UserRoleVO> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public String getRnum() {
		return rnum;
	}

	public void setRnum(String rnum) {
		this.rnum = rnum;
	}

	public List<AttachVO> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<AttachVO> attachList) {
		this.attachList = attachList;
	}

	public Integer getAtchNo() {
		return atchNo;
	}

	public void setAtchNo(Integer atchNo) {
		this.atchNo = atchNo;
	}

	public String getMemPassNew() {
		return memPassNew;
	}

	public void setMemPassNew(String memPassNew) {
		this.memPassNew = memPassNew;
	}

	@Override
	public String toString() {
		return "MemberVO [memId=" + memId + ", memPass=" + memPass + ", memPassNew=" + memPassNew + ", memName="
				+ memName + ", joinDate=" + joinDate + ", dDay=" + dDay + ", money=" + money + ", memRole=" + memRole
				+ ", memMail=" + memMail + ", memDelYn=" + memDelYn + ", rememberMe=" + rememberMe + ", roleList="
				+ roleList + ", userRoleList=" + userRoleList + ", rnum=" + rnum + ", attachList=" + attachList
				+ ", atchNo=" + atchNo + "]";
	}

	public MemberVO(
			@Pattern(regexp = "^\\w{2,10}$", message = "아이디 영문, 숫자 조합 2~10자", groups = MemberRegister.class) String memId,
			@Pattern(regexp = "^\\w{2,10}$", message = "패스워드 영문, 숫자 조합 2~10자", groups = { MemberRegister.class,
					MemberModify.class }) String memPass,
			@Pattern(regexp = "^\\w{2,10}$", message = "패스워드 영문, 숫자 조합 2~10자", groups = { MemberRegister.class,
					MemberModify.class }) String memPassNew,
			@Size(min = 1, max = 10, message = "이름 10자 이하", groups = { MemberRegister.class,
					MemberModify.class }) String memName,
			String joinDate, int dDay, int money, String memRole,
			@NotEmpty(message = "이메일 입력") @Email(message = "이메일 형식 틀림") String memMail, String memDelYn,
			String rememberMe, List<String> roleList, List<UserRoleVO> userRoleList, String rnum,
			List<AttachVO> attachList, Integer atchNo) {
		super();
		this.memId = memId;
		this.memPass = memPass;
		this.memPassNew = memPassNew;
		this.memName = memName;
		this.joinDate = joinDate;
		this.dDay = dDay;
		this.money = money;
		this.memRole = memRole;
		this.memMail = memMail;
		this.memDelYn = memDelYn;
		this.rememberMe = rememberMe;
		this.roleList = roleList;
		this.userRoleList = userRoleList;
		this.rnum = rnum;
		this.attachList = attachList;
		this.atchNo = atchNo;
	}

}