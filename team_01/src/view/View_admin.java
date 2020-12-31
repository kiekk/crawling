package view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import crawling.Crawling;
import dao.BookDAO;
import dao.RentalDAO;
import dao.UserDAO;
import vo.BookVO;
import vo.RentalVO;

public class View_admin {
	static Scanner sc = new Scanner(System.in);
	BookDAO book;
	RentalDAO rent;
	ArrayList<BookVO> books;
	ArrayList<RentalVO> rents;
	//��ϵ� ���� ��Ȳ 
	void showBookList() {
		books = book.bookList(2);
//		books.forEach(value -> System.out.println(value));
		for(int i = 0;i<books.size();i++) {
			System.out.print((i+1)+"��, ");
			System.out.println(books.get(i));
		}
	}
	//�뿩�� ���� ��Ȳ
	void showRentalBookList() {
		//���� ��¥�� �ϸ� ����
		SimpleDateFormat format1 = new SimpleDateFormat ("dd");		
		Date time = new Date();
		String time1 = format1.format(time);
//		System.out.println(time1);
		rents = rent.rentalBookList(2);
		//�뿩�� ������ �����ؾ����� ��������Ʈ�� ����մϴ�.
		if(!rents.isEmpty()) {
			for(int i = 0;i<rents.size();i++) {
				//���� ����Ʈ�� �����ŭ �ݺ����� �����ϸ鼭, ���� ���ڿ� �ݳ� ���ڰ� ������ ��� BOOK_NAME�� ����ǥ��(�ڡڡ� �ݳ����!!)
				if(rents.get(i).getReturn_date().substring(8, 10).equals(time1)){
					rents.get(i).setBook_name(String.format("�ڡڡ�  %s �ݳ����!!!!", rents.get(i).getBook_name()));
				}
			}
			//������ ��ģ �� ����Ʈ ���
//			rents.forEach(value -> System.out.println(value));
			for(int i = 0;i<rents.size();i++) {
				System.out.print((i+1)+"��, ");
				System.out.println(rents.get(i));
			}
		}else {
			System.out.println("���� �뿩�� ������ �����ϴ�.");
		}
	}
	//��ϵ� ���� ����
	void deleteBook() {
		int select = 0;
		int choice = 0;
		//��ϵ� ���� ��Ȳ
		books = book.bookList(2);
//		books.forEach(value -> System.out.println(value));
		for(int i = 0;i<books.size();i++) {
			System.out.print((i+1)+"��, ");
			System.out.println(books.get(i));
		}
		System.out.println("�� ��° ������ �����Ͻðڽ��ϱ�?"); select = sc.nextInt();
		
		//������ �����Ϸ��� �ش� ������ '�뿩����' ���¿��� ���� ������ �����ϰ� �����߽��ϴ�.
		//������ '�뿩��'�� ���� ȸ���� ������ �ִ� ���̶� �����Ͽ�, �ݳ��� �Ϸ�� �Ŀ� ������ �����ϵ��� �մϴ�.
		if(book.rentalCheck(books.get(select-1).getBook_number()).equals("�뿩����")) {		//�뿩 ���� Ȯ��
				System.out.println(books.get(select-1).getBook_name()+" å�� �����Ͻðڽ��ϱ�?");	choice = sc.nextInt();
				if(choice == 1) {
					book.deleteBookList(books.get(select-1).getBook_number());
				}else if (choice == 2) {
					System.out.println("����մϴ�.");
				}
		}else {		//�뿩���ΰ� '�뿩��'�� ���
			System.out.println(books.get(select-1).getBook_name()+" å�� �뿩���Դϴ�.");
			System.out.println("�ݳ��� �� ���¿��� ������ �����մϴ�.");
		}
	}
	//������ ���� ������
	void adminMain() {
		book = new BookDAO();
		rent = new RentalDAO();
		int choice = -1;
		while(choice != 0) {
			System.out.println("������ ������");
			System.out.println("�޴� ����\n1.ȸ�� ��Ȳ\n2.���� ��Ȳ\n3.�뿩 ��Ȳ\n4.���� �߰�\n5.���� ����\n0.�������������̵�");
			choice = sc.nextInt();
			switch(choice) {
			case 1: new UserDAO().selectAll(); break;
			case 2: showBookList(); break; //��ϵ� ������Ȳ�� �뿩Ƚ���� ����
			case 3: showRentalBookList(); break; 
			case 4: new Crawling().searchingBook(); break;
			case 5: deleteBook(); break;
			}//end switch
		}//end while
	}//end adminMain()
}
