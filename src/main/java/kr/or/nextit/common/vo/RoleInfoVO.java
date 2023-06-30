package kr.or.nextit.common.vo;

public class RoleInfoVO {

	private String roleName;
	private String roleKor;

	@Override
	public String toString() {
		return "RoleInfoVO [roleName=" + roleName + ", roleKor=" + roleKor + "]";
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleKor() {
		return roleKor;
	}

	public void setRoleKor(String roleKor) {
		this.roleKor = roleKor;
	}
}
