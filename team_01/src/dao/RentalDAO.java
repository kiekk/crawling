package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vo.RentalVO;

public class RentalDAO {
	Connection conn;
	PreparedStatement pstm;
	ResultSet rs;
	UserDAO user;
	BookDAO book = new BookDAO();
	RentalVO rent;
	ArrayList<RentalVO> rents;
	//�뿩���� ���� ����Ʈ
	public ArrayList<RentalVO> rentalBookList(int select){
		user = new UserDAO();
		//ȸ���� �뿩�� ���� ����Ʈ - ���� ����
		String query = "SELECT * FROM TL_RENTAL WHERE USER_NUMBER = (SELECT USER_NUMBER FROM TL_USER WHERE PHONE_NUMBER = ?)";
		//���� �뿩�� ���� ����Ʈ
		String query2 = "SELECT * FROM TL_RENTAL";
		try {
			conn = DBConnection.getConnection();
			switch(select) {
				//ȸ���� �뿩�� ��������Ʈ�� ����Ҷ�, select��� flag������ 1�ΰ�� query�� ����
				case 1: 
					pstm = conn.prepareStatement(query); 
					pstm.setString(1, user.session_id);
					break;
				//���� �뿩�� ��������Ʈ�� ����Ҷ�, select��� flag������ 2�ΰ�� query2�� ����
				case 2: pstm = conn.prepareStatement(query2); break;
			}//end switch
		
			rs = pstm.executeQuery();
			//rs != null -> ��������Ʈ�� �����ϴ� ����̱� ������ ��ü ����. rs == null�̸� ���� X
			if(rs != null) {rents = new  ArrayList<RentalVO>();}
			
			while (rs.next()) {
				rent = new RentalVO();
				rent.setName(rs.getString(1));
				rent.setUser_number(rs.getInt(2));
				rent.setBook_number(rs.getInt(3));
				rent.setBook_name(rs.getString(4));
				rent.setRental_date(rs.getString(5));
				rent.setReturn_date(rs.getString(6));
				rents.add(rent);
			}//end while
		} catch (SQLException sqle) {
			System.out.println("RentalDAO.java:53, rentalBookList() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("RentalDAO.java:55, rentalBookList() ���� " + e);
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
				throw new RuntimeException("RentalDAO.java:68, rentalBookList() close ����" + sqle.getMessage());
			}//end sub-try
		}//end main-try
		return rents;
	}//end rentalBookList()
	//�뿩 �ϱ�
	public void rentalBook(int book_number) {
		//TL_USER ���̺��� NAME, USER_NUMBER
		//TL_BOOK ���̺��� BOOK_NUMBER, BOOK_NAME
		String query = "INSERT INTO TL_RENTAL "
				+ "VALUES("
				+ "(SELECT NAME FROM TL_USER WHERE PHONE_NUMBER = ?),"				//TL_USER ���̺��� NAME
				+ "(SELECT USER_NUMBER FROM TL_USER WHERE PHONE_NUMBER = ?), "		//TL_USER ���̺��� USER_NUMBER
				+ "(SELECT BOOK_NUMBER FROM TL_BOOK WHERE BOOK_NUMBER = ?),"		//TL_BOOK ���̺��� BOOK_NUMBER
				+ "(SELECT BOOK_NAME FROM TL_BOOK WHERE BOOK_NUMBER = ?),"			//TL_BOOK ���̺��� BOOK_NAME
				+ "SYSDATE,"														//�뿩 ��¥(����ð�)
				+ "(SYSDATE)"													//�ݳ� ��¥(�뿩 ��¥ + 7��)
				+ ")";
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, user.session_id);
			pstm.setString(2, user.session_id);
			pstm.setInt(3, book_number);
			pstm.setInt(4, book_number);

			if(pstm.executeUpdate() == 1) {
				//�ش� ������ �뿩���θ� ���� '�뿩����'->'�뿩��'
				if(book.switchRental(book_number)) {
					System.out.println("�뿩 ����");					
				}else {
					System.out.println("�뿩 ����");
				}//end sub-if
			}//end main-if
		} catch (SQLException sqle) {
			System.out.println("RentalDAO.java:103, rentalBook() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("RentalDAO.java:105, rentalBook() ���� " + e);
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("RentalDAO.java:115, rentalBook() close ����" + sqle.getMessage());
			}//end sub-try
		} // end main-try
	}//end rentalBook()
	//�ݳ� �ϱ�
	public void returnBook(int book_number) {
		String query = "DELETE FROM TL_RENTAL WHERE BOOK_NUMBER = ?";
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setInt(1, book_number);

			if(pstm.executeUpdate() == 1) {
				//�ش� ������ �뿩���θ� ���� '�뿩��' -> '�뿩����'
				if(book.switchRental(book_number)) {
					System.out.println("�ݳ� ����");					
				}
			}
		} catch (SQLException sqle) {
			System.out.println("RentalDAO.java:134, returnBook() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("RentalDAO.java:136, returnBook() ���� " + e);
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("RentalDAO.java:146, returnBook() close ����" + sqle.getMessage());
			}
		} // end try
	}//end returnBook()
}
