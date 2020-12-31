package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import vo.BookVO;
import vo.UserVO;

public class BookDAO {
	static Scanner sc = new Scanner(System.in);
	Connection conn;
	PreparedStatement pstm;
	ResultSet rs;
	//booklist�� ���� ��ü
	BookVO book = null;
	ArrayList<BookVO> books = null;

	//���� ����Ʈ
	//select�� flag �����ν� ���� ���ǿ� �´� ���������� �����͸� �޾ƿ� �� �ֵ��� �մϴ�.
	public ArrayList<BookVO> bookList(int select) {
		//�뿩 ������ ���� ����Ʈ   select == 1
		String query = "SELECT * FROM TL_BOOK WHERE BOOK_RENTAL = '�뿩����'";
		//������Ȳ �뿩Ƚ��,�����Ϸ� ����  select == 2
		String query2 = "SELECT * FROM TL_BOOK ORDER BY RENTAL_COUNT DESC";
		//�α��� ���¸� ���� Ȯ��
		try {
			conn = DBConnection.getConnection();
			switch(select) {
				case 1: pstm = conn.prepareStatement(query); break;
				case 2: pstm = conn.prepareStatement(query2); break;
			}//end switch
			
			rs = pstm.executeQuery();

			books = new ArrayList<BookVO>();				//��������Ʈ�� ������ ArrayList����
			while (rs.next()) {								//��������Ʈ�� null�� ������ �ݺ��ؼ� ����
				book = new BookVO();
				book.setBook_number(rs.getInt(1)); 		 	//������ȣ
				book.setBook_name(rs.getString(2));   		//������
				book.setAuthor(rs.getString(3));   			//����
				book.setCompany(rs.getString(4)); 			//���ǻ�
				book.setBook_date(rs.getString(5).substring(0,10));			//������
				book.setBook_section(rs.getString(6));		//������ġ
				book.setBook_rental(rs.getString(7));		//�뿩��
				book.setRental_count(rs.getInt(8));			//�뿩Ƚ��
				books.add(book);
			}//end while
			//���� ����Ʈ ���  -> �׽�Ʈ��
//			books.forEach(value -> System.out.println(value));

		} catch (SQLException sqle) {
			System.out.println("BookDAO.java:57, bookList() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("BookDAO.java:59, bookList() ���� " + e);
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
				throw new RuntimeException("BookDAO.java:72, bookList() close ����" + sqle.getMessage());
			}//end sub-try
		}//end main-try
		return books;
	}//end bookList()
	//���� ��ġ ã�� - ������ - �����ε�
	public String findBook(String book_name) {
		String bookSection = "";
		//book_name���� �ش� ������ ��ġ�� ��ȸ�մϴ�. �ش� ������ ��ġ�� bookSection�� ��Ƽ� ��ȯ
		String query = "SELECT BOOK_SECTION FROM TL_BOOK WHERE BOOK_NAME LIKE '%' || ? || '%'";
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, book_name);
			rs = pstm.executeQuery();

			if (rs.next()) {
				bookSection = rs.getString(1);
//				System.out.println(bookSection);		//->�׽�Ʈ
			}//end if
		} catch (SQLException sqle) {
			System.out.println("BookDAO.java:93, findBook() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("BookDAO.java:95, findBook() ���� " + e);
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
				throw new RuntimeException("BookDAO.java:108, findBook() close ����" + sqle.getMessage());
			}//end sub-try
		}//end main-try
		return bookSection;
	}//end findBook()
	//���� ��ġ ã�� - ������ȣ - �����ε�
	public String findBook(int book_number) {
		String bookSection = "";
		//book_number���� �ش� ������ ��ġ�� ��ȸ�մϴ�. �ش� ������ ��ġ�� bookSection�� ��Ƽ� ��ȯ
		String query = "SELECT BOOK_SECTION FROM TL_BOOK WHERE BOOK_NUMBER = ?";
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setInt(1, book_number);
			rs = pstm.executeQuery();

			rs.next(); 
			bookSection = rs.getString(1);
//			System.out.println(bookSection);
			
		} catch (SQLException sqle) {
			System.out.println("BookDAO.java:129, findBook() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("BookDAO.java:131, findBook() ���� " + e);
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
				throw new RuntimeException("BookDAO.java:144, findBook() close ����" + sqle.getMessage());
			}//end sub-try
		}//end main-try
		return bookSection;
	}//end findBook()
	//�뿩���ɿ��� ����
	public boolean switchRental (int book_number) {
		//�뿩�� ���� �뿩 Ƚ�� 1����
		String query = "UPDATE TL_BOOK SET BOOK_RENTAL = ?, RENTAL_COUNT = RENTAL_COUNT + 1 WHERE BOOK_NUMBER = ?";
		//�ݳ��� ���� �뿩���ɿ��θ� ����
		String query2 = "UPDATE TL_BOOK SET BOOK_RENTAL = ? WHERE BOOK_NUMBER = ?";
		boolean check = false;
		//�뿩���� üũ -> �뿩���θ� ���� ('�뿩����'or'�뿩��')
		String bookRental = rentalCheck(book_number);
		try {
			conn = DBConnection.getConnection();
			//�ش絵���� �뿩���ΰ� "�뿩����"�ϰ��  query�� ����
			if(bookRental.equals("�뿩����")) {
				pstm = conn.prepareStatement(query);
				pstm.setString(1, "�뿩��");
			//�ش絵���� �뿩���ΰ� "�뿩��"�ϰ�� query2�� ����
			}else if(bookRental.equals("�뿩��")) {
				pstm = conn.prepareStatement(query2);
				pstm.setString(1, "�뿩����");
			}//end if
			pstm.setInt(2, book_number);
			
			if(pstm.executeUpdate() == 1) {
				System.out.println("�뿩 ���� ���� ����");
				check = true;
			}//end if

		} catch (SQLException sqle) {
			System.out.println("BookDAO.java:177, switchRental() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("BookDAO.java:179, switchRental() ���� " + e);
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("BookDAO.java:189, switchRental() close ����" + sqle.getMessage());
			}//end sub-try
		}//end main-try
		return check;
	}//end switchRental()
	//�뿩���� Ȯ�� - �����ε� String
	public String rentalCheck(String book_name) {
		//�뿩 ���θ� ������ ����
		String bookRental = null;
		//book_number�� ���ؼ� �ش� ������ �뿩 ���θ� �����ɴϴ�.
		String query = "SELECT BOOK_RENTAL FROM TL_BOOK WHERE BOOK_NAME LIKE '%' || ? || '%'";
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, book_name);
			rs = pstm.executeQuery();

			rs.next();

			bookRental = rs.getString(1);
		} catch (SQLException sqle) {
			System.out.println("BookDAO.java:211, rentalCheck() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("BookDAO.java:213, rentalCheck() ���� " + e);
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
				throw new RuntimeException("BookDAO.java:226, rentalCheck() close ����" + sqle.getMessage());
			}//end sub-try
		}//end main-try
		return bookRental;
	}//end rentalCheck()
	//�뿩���� Ȯ�� - �����ε� int
	public String rentalCheck(int book_number) {
		//�뿩 ���θ� ������ ����
		String bookRental = "";
		//book_number�� ���ؼ� �ش� ������ �뿩 ���θ� �����ɴϴ�.
		String query = "SELECT BOOK_RENTAL FROM TL_BOOK WHERE BOOK_NUMBER = ?";
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setInt(1, book_number);
			rs = pstm.executeQuery();

			rs.next();

			bookRental = rs.getString(1);
			
		} catch (SQLException sqle) {
			System.out.println("BookDAO.java:248, rentalCheck() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("BookDAO.java:250, rentalCheck() ���� " + e);
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
				throw new RuntimeException("BookDAO.java:263, rentalCheck() close ����" + sqle.getMessage());
			}//end sub-try
		}//end main-try
		return bookRental;
	}//end rentalCheck()
	//���� ��� �߰�
	public void addBookList(String book_name,String author,String company,String book_date) {
		Random r = new Random();
		String query = "INSERT INTO TL_BOOK VALUES (SEQ_BOOK.NEXTVAL,?,?,?,?,?,'�뿩����',0)";
		String section = "ABCDEFGHIJ";	//�������� ������ġ(Section)�� �����ϱ� ���� ���ڿ��� ����. charAt���� ����
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, book_name);
			pstm.setString(2, author);
			pstm.setString(3, company);
			pstm.setString(4, book_date);
			pstm.setString(5, String.format("%s", section.charAt(r.nextInt(section.length()))));	//�����ϰ� ������ġ ����

			if(pstm.executeUpdate() == 1) {
				System.out.println("���� ��� �߰� ����");
			}//end if
		} catch (SQLException sqle) {
			System.out.println("BookDAO.java:286, addBookList() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("BookDAO.java:288, addBookList() ���� " + e);
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("UserDAO.java:298, addBookList() close ����" + sqle.getMessage());
			}//end sub-try
		}//end main-try
	}//end addBookList()
	//���� ��� ����
	public void deleteBookList(int book_number) {
		//���޹��� book_number�� ��ġ�ϴ� ������ �����͸� ����
		String query = "DELETE FROM TL_BOOK WHERE BOOK_NUMBER = ?";
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setInt(1, book_number);

			if(pstm.executeUpdate() == 1) {
				System.out.println("���� ���� ����");
			}
		} catch (SQLException sqle) {
			System.out.println("UserDAO.java:315, deleteBookList() ���� ���� : " + sqle);
		} catch (Exception e) {
			System.out.println("UserDAO.java:317, deleteBookList() ���� " + e);
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				throw new RuntimeException("BookDAO.java:327, deleteBookList() close ����" + sqle.getMessage());
			}//end sub-try
		}//end main-try
	}//end deleteBookList()
}
