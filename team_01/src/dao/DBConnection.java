package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public static Connection getConnection() {
		Connection conn = null;
		//������ �ϱ� ���ؼ��� url, user, pw�� �ʿ��մϴ�.
		try {
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String user = "hr";
			String pw = "hr";
			String driver = "oracle.jdbc.driver.OracleDriver";
			
			//1. Ŭ������ �ƴ� ��� Ȥ�� ������ Ŭ����ȭ ��Ű�� �۾�
			//2. �ܺ� Ŭ������ �������� ����
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,pw);
			
			//Exception ��ü���� ���� Ŭ���� �빮�ڸ� �����ͼ� ����ϴ�.
		} catch (ClassNotFoundException cnfe) {
			//���� �߻��� � ����, ��� ° �������� �ۼ����ְ�, �ش� ������ �ۼ��մϴ�.
			System.out.println("DBConnection.java:25, ����̹� �ε� ���� : "+ cnfe);
		} catch (SQLException sqle) {
			System.out.println("DBConnection.java:27, DB ���� ���� : "+ sqle);	
		} catch (Exception e) {
			System.out.println("DBConnection.java:29,: "+ e);
		}
		return conn;
//		��Ʈ�ѷ����� close���ݴϴ�.
	}
}
