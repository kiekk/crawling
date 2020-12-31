package vo;

import dao.UserDAO;

public class UserVO {
	private int user_number;
	private String phone_number;
	private String pw;
	private String name;
	private int age;
	public int getUser_number() {
		return user_number;
	}
	public void setUser_number(int user_number) {
		this.user_number = user_number;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {																	//��ȣȭ
		return "ȸ����ȣ : " + user_number + ", ȸ�� ID : " + phone_number + ", ��й�ȣ : " + new UserDAO().encrypt(pw) + ", ȸ���̸� : "
				+ name + ", ���� : " + age;
	}
	
	
}
