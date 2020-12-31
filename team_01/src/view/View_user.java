package view;

import java.util.ArrayList;
import java.util.Scanner;

import dao.BookDAO;
import dao.RentalDAO;
import dao.UserDAO;
import vo.BookVO;
import vo.RentalVO;

public class View_user {
	static Scanner sc = new Scanner(System.in);
	UserDAO user = new UserDAO();
	BookDAO book = new BookDAO();
	RentalDAO rent = new RentalDAO();
	ArrayList<BookVO> books = new ArrayList<BookVO>();
	ArrayList<RentalVO> rents = new ArrayList<RentalVO>();
	//��й�ȣ ���� - �Է¹޴� �޼ҵ�
	void inputUpdate() {
		String pw = "";
		String new_pw = "";
		System.out.println("��й�ȣ ����");
		System.out.print("���� ��й�ȣ : "); pw = sc.next();
		System.out.print("�ٲ� ��й�ȣ : "); new_pw = sc.next();
		//�Է¹��� ������ update()�� ����
		if(user.update(pw, new_pw)) {
			System.out.println("��й�ȣ ���� ����");
		}else {
			System.out.println("��й�ȣ ���� ����");
		}
	}

	//���� ��ġ ã�� - �Է¹޴� �޼ҵ�
	void inputFindBook() {
		sc.nextLine();
		String find = "";
		int flag = 0;
		System.out.println("ã�� ������ ������, �Ǵ� ���� ��ȣ�� �Է����ּ���");
		//���� String���� �Է¹ް�
		find = sc.nextLine();
		//�Է¹��� ���� �������� �������� Ȯ��
		for(int i = 0; i<find.length();i++) {
			if(find.charAt(i) >= '0' && find.charAt(i) <= '9') {
				flag++;
			}
		}//end for
//		System.out.println(flag);
		if(flag == find.length()) {
			int num = Integer.parseInt(find);
			//�ش� ������ �뿩���θ� Ȯ��.
			if((book.rentalCheck(num)).equals("�뿩����")) {
				//�ش� ������ �뿩���ΰ� '�뿩����'�� ��� findbook()���� ������ġ�� ã���ϴ�.
				System.out.println(num+"�� å�� "+ book.findBook(num)+" ��ġ�� �ֽ��ϴ�.");				
			}else {
				System.out.println(num+"�� å�� �뿩���Դϴ�.");
			}
		}else {
			if(book.rentalCheck(find).equals("�뿩����")) {
				System.out.println(find+" å�� "+ book.findBook(find)+" ��ġ�� �ֽ��ϴ�.");							
			}else {
				System.out.println(find+" å�� �뿩���Դϴ�.");
			}
		}//end if
	}
	//�뿩 �ϱ� - �Է� ���� �޼ҵ�
	void inputRentalBook() {
		int choice = -1;			//�뿩�� ������ ��ȣ
		int select = 0;				//�뿩 Ȯ�� 
		books = book.bookList(1);	//�뿩 ������ ���� ����Ʈ

		//���� ����Ʈ ���
//		books.forEach(value -> System.out.println(value));
		for(int i = 0;i<books.size();i++) {
			System.out.print((i+1)+"��, ");
			System.out.println(books.get(i));
		}
		System.out.println("���° ������ �뿩 �Ͻðڽ��ϱ�?(0.�ڷΰ���) : "); choice = sc.nextInt();
		if(choice == 0) return;
		System.out.println(books.get(choice-1).getBook_name()+"������ �뿩�Ͻðڽ��ϱ�?\n1.��\t2.�ƴϿ�"); select = sc.nextInt();
		if(select == 1) {
			System.out.println(books.get(choice-1).getBook_name()+"������ �뿩�մϴ�");
			rent.rentalBook(books.get(choice-1).getBook_number());
		}else if(select == 2) {
			System.out.println("�ٽ� ������ �ּ���.");
		}//end if
	}
	//�ݳ� �ϱ� - �Է� ���� �޼ҵ�
	void inputReturnBook() {
		int choice = -1;
		int select = 0;
		rents = rent.rentalBookList(1);	//ȸ���� �뿩�� ���� ����Ʈ
		//���� ����Ʈ ���
		//�뿩�� ������ �ִ� ��쿡�� ���� ����Ʈ�� ����մϴ�.
		if(!rents.isEmpty()) {
			System.out.println("���� �뿩�� ���� ����Ʈ");
//			rents.forEach(value -> System.out.println(value));
			for(int i = 0;i<rents.size();i++) {
				System.out.print((i+1)+"��, ");
				System.out.println(rents.get(i));
			}
		}else {
			System.out.println("�ݳ��� ������ �����ϴ�.");
			return;
		}
		System.out.println("�ݳ��� ������ '������ȣ'�� �Է����ּ��� (0.�ڷΰ���): "); choice = sc.nextInt();
		if(choice == 0) return;
		
		System.out.println(rents.get(choice-1).getBook_name()+"������ �ݳ��Ͻðڽ��ϱ�?\n1.��\t2.�ƴϿ�"); select = sc.nextInt();
		if(select == 1) {
			System.out.println(rents.get(choice-1).getBook_name()+"������ �ݳ��մϴ�");
			rent.returnBook(rents.get(choice-1).getBook_number());
		}else if(select == 2) {
			System.out.println("�ٽ� ������ �ּ���.");
		}//end if
	}
	//ȸ���� �뿩�� ���� ����Ʈ
	void inputRentalBookList() {
		ArrayList<RentalVO> rents = new ArrayList<RentalVO>();
		rents = rent.rentalBookList(1);
		if(!rents.isEmpty()) {
//			rents.forEach(value -> System.out.println(value));
			System.out.println("���� �뿩�� ���� ����Ʈ");
			for(int i = 0;i<rents.size();i++) {
				System.out.print((i+1)+"��, ");
				System.out.println(rents.get(i));
			}
		}else {
			System.out.println("�뿩�� ������ �����ϴ�.");
			return;
		}
	}
	void inputadd() {
	}
	//ȸ�� ���� ������
	public void userMain() { 
		int choice = -1;
		System.out.println("���� �α��� �� ID : " + user.session_id);
		while(choice != 0) {
			System.out.println("�޴� ����\n1.���� ����\n2.��й�ȣ ����\n3.���� ��ġ ã��\n4.�뿩 Ȯ��\n5.�뿩 �ϱ�\n6.�ݳ� �ϱ�\n0.�α� �ƿ�");
			choice = sc.nextInt();
			if(choice == 0) user.logout();
			switch(choice) {
			case 1: System.out.println(user.select()); break;
			case 2: inputUpdate(); break;
			case 3: inputFindBook(); break;
			case 4: inputRentalBookList(); break;
			case 5: inputRentalBook(); break;
			case 6: inputReturnBook(); break;
			}//end switch
		}//end while
		System.out.println("�α׾ƿ� �Ǿ����ϴ�.");
	}//end userMain()
}
