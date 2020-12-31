package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import vo.UserVO;

public class UserDAO {
	private static final int KEY_CODE = 3; // ��ȣȭ, ��ȣȭ �� �� ����� Ű��
	Connection conn;
	PreparedStatement pstm;
	ResultSet rs;
	UserVO user;

	public static String session_id;	//�α��ε� ���̵� ���

	// ���̵� �˻�
	public boolean checkId(String phone_number) {
		// Flag
		boolean check = false;
		String query = "SELECT COUNT(*) FROM TL_USER WHERE PHONE_NUMBER = ?";
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, phone_number);
			rs = pstm.executeQuery();

			rs.next();

			if (rs.getInt(1) == 1) {
				check = true;
			}

		} catch (SQLException sqle) {
			System.out.println("UserDAO.java:39, checkId()���� ���� " + sqle);
		} catch (Exception e) {
			System.out.println("UserDAO.java:41, checkId() ���� " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("UserDAO.java:54, checkId() close ����" + sqle.getMessage());
			}
		} // end try
		return check;
	}// end checkId()
	
	// ȸ������
	public boolean join(String phone_number, String pw, String name, int age) {
		String query = "";
		boolean check = false;
		if (!checkId(phone_number)) {
			query = "INSERT INTO TL_USER (USER_NUMBER,PHONE_NUMBER, PW, NAME, AGE)"
					+ "VALUES(SEQ_USER.NEXTVAL, ?, ?, ?, ?)";
			try {
				int idx = 0;
				conn = DBConnection.getConnection();
				pstm = conn.prepareStatement(query);
				pstm.setString(++idx, phone_number);
				pstm.setString(++idx, encrypt(pw));
				pstm.setString(++idx, name);
				pstm.setInt(++idx, age);
				// select �̿ܿ� ��� ���� ��ȯ�������� excuteUpdate()�� ����մϴ�. 0,1�� ��ȯ�մϴ�.
				if (pstm.executeUpdate() == 1) {
					check = true;
				}
			} catch (SQLException sqle) {
				System.out.println("UserDAO.java:80, join() ���� ���� : " + sqle);
			} catch (Exception e) {
				System.out.println("UserDAO.java:82, join() ���� " + e);
			} finally {
				try {
					if (pstm != null) {
						pstm.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException sqle) {
					throw new RuntimeException("UserDAO.java:92, join() close ����" + sqle.getMessage());
				}
			} // end try
		} // end if
		return check;
	}// end join()
	
	// ��ȣȭ
	public String encrypt(String pw) {
		String enc_pw = "";
		// pw�� ���� ���ڷ� ������ ������ ��ȣȭ KEY_CODE�� ����(�ƽ�Ű�ڵ�� ����)���� ���ڸ� enc_pw ������ �����մϴ�.
		for (int i = 0; i < pw.length(); i++) {
			enc_pw += (char) (pw.charAt(i) * KEY_CODE);
		}
		// System.out.println("���� ��ȣ : "+pw+"\tenc��ȣ : "+enc_pw);
		return enc_pw;
	}

	// ��ȣȭ
	public String decrypt(String enc_pw) {
		// String dec_pw;
		// dec_pw���� null���� ���µ�, �̶� "1234"���ڿ��� +=������ �� ��� null1234�� null���� �Էµ˴ϴ�.
		String dec_pw = "";
		for (int i = 0; i < enc_pw.length(); i++) {
			dec_pw += (char) (enc_pw.charAt(i) / KEY_CODE);
		}
		// System.out.println("end��ȣ : "+enc_pw+"\tdec��ȣ : "+dec_pw);
		return dec_pw;
	}

	// �α���
	// �ܺο��� ����ڰ� �Է��� phone_number�� PW�� ���޹޽��ϴ�.
	public boolean login(String phone_number, String pw) {
		String query = "SELECT COUNT(*) FROM TL_USER WHERE phone_number = ? AND PW = ?";
		// Flag
		boolean check = false;
		if (checkId(phone_number)) {
			try {
				conn = DBConnection.getConnection();
				pstm = conn.prepareStatement(query);
				pstm.setString(1, phone_number);
				pstm.setString(2, encrypt(pw));
				rs = pstm.executeQuery();
				
				rs.next();

				if(rs.getInt(1) == 1) {
					System.out.println("�α��� ����");
					session_id = phone_number;
					check = true;
				}else {
					System.out.println("��ȭ��ȣ �Ǵ� ��й�ȣ�� Ȯ�����ּ���");
					check = false;
				}
			} catch (SQLException sqle) {
				System.out.println("UserDAO.java:147, login() ���� ���� : " + sqle);
			} catch (Exception e) {
				System.out.println("UserDAO.java:149, login() ���� " + e);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstm != null) {
						pstm.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException sqle) {
					throw new RuntimeException("UserDAO.java:162, login() close ����" + sqle.getMessage());
				}
			} // end try
		} else {
			System.out.println("�������� �ʴ� ID�Դϴ�.");
		} // end if
		return check;
	}

	// �α׾ƿ�
	public void logout() {
		// ����Ǿ� �ִ� session_id���� �����ݴϴ�.
		// �α��� ����� ����ϴ�.
		session_id = null;
	}

	// ���� - ��й�ȣ ����
	// ������ȣ��, �α����� �Ǿ����� ������ ��й�ȣ�� �� �� �� �Է¹޽��ϴ�.
	// ���� ��й�ȣ�� ���Ӱ� ������ ��й�ȣ�� ���޹޽��ϴ�.
	public boolean update(String pw, String new_pw) {
		// �α��ε� id�� �Է��� pw�� �˻��� �� ã�Ҵٸ� �ش� �������
		// pw�� ���ο� pw�� �������ݴϴ�.
		String query = "UPDATE TL_USER SET PW = ? WHERE PHONE_NUMBER = ? AND PW = ?";
		boolean check = false;
		// �α��� ���¸� ���� Ȯ��
		if (session_id == null) {
			// ���� �α��� ���°� �ƴ϶�� �ٷ� false�� �������ݴϴ�.
			return false;
		}
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, encrypt(new_pw));
			// �̹� �α��� �Ǿ��־�� ��й�ȣ ������ �����ϱ� ������,
			// id�� ���ڷ� ���� �ʰ� session_id�� ���ؼ� id�� ������ �� �ֽ��ϴ�.
			pstm.setString(2, session_id);
			pstm.setString(3, encrypt(pw));

			// SQL�� ��� �Ǽ��� 1�̶�� ��й�ȣ ���� ����
			if (pstm.executeUpdate() == 1) {
				check = true;
			}
		} catch (SQLException sqle) {
			System.out.println("UserDAO.java:241, update() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("UserDAO.java:243, update() ���� " + e);
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("UserDAO.java:253, update() close ����" + sqle.getMessage());
			}
		} // end try
		return check;
	}

	//�� ���� ����
	public UserVO select() {
		String query = "SELECT * FROM TL_USER WHERE PHONE_NUMBER = ?";
		UserVO user = null;
		// �α��� ���¸� ���� Ȯ��
		if (session_id == null) {
			return null;
		}
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, session_id);
			rs = pstm.executeQuery();

			// rs�� ����ִ� �����͸� user�� ����
			// �α��ε� ����ڴ� �Ѹ��̱� ������ while���� �ƴ� if������ �ۼ��մϴ�.
			if (rs.next()) {
				user = new UserVO();
				user.setUser_number(rs.getInt(1)); 		 // USER_NUMBER
				user.setPhone_number(rs.getString(2));   // PHONE_NUMBER
				user.setPw(decrypt(rs.getString(3)));    // PW
				user.setName(rs.getString(4)); 			 // NAME
				user.setAge(rs.getInt(5));				 // AGE
			}

		} catch (SQLException sqle) {
			System.out.println("UserDAO.java:322, select() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("UserDAO.java:324, select() ���� " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("UserDAO.java:337, select() close ����" + sqle.getMessage());
			}
		} // end try
		return user;
	}

	//��ϵ� ȸ�� ��Ȳ
	public ArrayList<UserVO> selectAll() {
		ArrayList<UserVO> users = null;
		UserVO user = null;
		String query = "SELECT * FROM TL_USER";

		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();

			if (rs != null) {
				users = new ArrayList<UserVO>();
			}
			
			while (rs.next()) {
				user = new UserVO();
				user.setUser_number(rs.getInt(1)); 		 // USER_NUMBER
				user.setPhone_number(rs.getString(2));   // PHONE_NUMBER
				user.setPw(decrypt(rs.getString(3)));    // PW
				user.setName(rs.getString(4)); 			 // NAME
				user.setAge(rs.getInt(5));				 // AGE
				users.add(user);
			}
			users.forEach(value -> System.out.println(value));
			
		} catch (SQLException sqle) {
			System.out.println("UserDAO.java:372, selectAll() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("UserDAO.java:374, selectAll() ���� " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("UserDAO.java:387, selectAll() close ����" + sqle.getMessage());
			}
		} // end try
		return users;
	}
	/**
	 * ��й�ȣ ã�� �� ������� ��й�ȣ�� �ӽ� ��й�ȣ�� �������ִ� �޼ҵ�
	 * 
	 * @param user_number
	 * @param temp_pw
	 * @return boolean
	 */
	// ���� ��й�ȣ�� �ӽ� ��й�ȣ�� ����
	public boolean update(int user_number, String temp_pw) {
		String query = "UPDATE TL_USER SET PW = ? WHERE USER_NUMBER = ?";
		boolean check = false;
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, encrypt(temp_pw));
			pstm.setInt(2, user_number);

			if (pstm.executeUpdate() == 1) {
				check = true;
			}
		} catch (SQLException sqle) {
			System.out.println("UserDAO.java:490, update() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("UserDAO.java:492, update() ���� " + e);
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("UserDAO.java:502, update() close ����" + sqle.getMessage());
			}
		} // end try
		return check;
	}

	// ��� ã��
	public boolean findPw(String phone_number) {
		// ������ ������ �������� �ӽ� ��й�ȣ�� ����ϴ�.
		String temp = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*()_+=[]{};:/?";
		Random r = new Random();
		String query = "SELECT USER_NUMBER FROM TL_USER WHERE PHONE_NUMBER = ?";
		boolean check = false;
		String temp_pw = "";

		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, phone_number);
			rs = pstm.executeQuery();

			if (rs.next()) {
				// 8�ڸ� �ӽú�й�ȣ ����
				for (int i = 0; i < 8; i++) {
					temp_pw += temp.charAt(r.nextInt(temp.length()));
				}
				if (update(rs.getInt(1), temp_pw)) {
					System.out.println("�ӽ� ��й�ȣ : " +temp_pw);
					System.out.println("����� �� ������ �ݵ�� ��й�ȣ�� �������ּ���.");
					check = true;
				}
			}
		} catch (SQLException sqle) {
			System.out.println("UserDAO.java:573, findPw() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("UserDAO.java:575, findPw() ���� " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("UserDAO.java:585, findPw() close ����" + sqle.getMessage());
			}
		} // end try
		return check;
	}
}
