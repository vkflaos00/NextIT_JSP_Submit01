package kr.or.nextit.member.vo;

public class MailAuthVO {

	private String mail;
	private String key;
	private int isAuth;

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(int isAuth) {
		this.isAuth = isAuth;
	}

	@Override
	public String toString() {
		return "MailAuthVO [mail=" + mail + ", key=" + key + ", isAuth=" + isAuth + "]";
	}

}
