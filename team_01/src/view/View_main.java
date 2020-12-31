package view;

import java.util.Scanner;

import dao.UserDAO;

public class View_main {
	static Scanner sc = new Scanner(System.in);
	static UserDAO user = new UserDAO();
	//input�޼ҵ忡�� ����� �������� ���������� �����մϴ�.
	String id, pw, name, phone_number;
	int age;
	
	//�α��� - �Է¹��� �޼ҵ�
	void inputLogin() {
		System.out.println("�α���");
		System.out.print("��ȭ��ȣ : "); phone_number = sc.next();
		System.out.print("��й�ȣ : "); pw = sc.next();
		
		//�Է¹��� ������ login()�� ����
		if(user.login(phone_number, pw)) {
			new View_user().userMain();
		}
	}
	//ȸ������ - �Է¹��� �޼ҵ�
	void inputJoin() {
		System.out.println("ȸ������");
		System.out.print("��ȭ��ȣ(��ȭ��ȣ�� ID�� ���˴ϴ�.) : "); phone_number = sc.next();
		System.out.print("��й�ȣ : "); pw = sc.next();
		System.out.print("�̸� : "); name = sc.next();
		System.out.print("���� : "); age = sc.nextInt();
		
		//�Է¹��� ������ join()�� ����
		if(user.join(phone_number, pw, name, age)) {
			System.out.println("ȸ������ ����\n�α��� ���ּ���");			
		}else {
			System.out.println("ȸ������ ����\n�ٽ� Ȯ�����ּ���.");
		}
	}
	//��й�ȣã�� - �Է¹��� �޼ҵ�
	void inputFindPw() {
		System.out.println("��й�ȣ ã��");
		System.out.print("��ȭ��ȣ : "); phone_number = sc.next();
		
		//�Է¹��� ��ȭ��ȣ�� findPw()�� ����
		user.findPw(phone_number);
	}
	//���� ������
	public void mainPage() {
		int choice = -1;
		while(choice != 0) {
		System.out.println("   KOREA ������");
		System.out.println("��������������������������������");
		System.out.println("�޴� ����\n1.�α���\n2.ȸ�� ����\n3.��й�ȣ ã��\n0.���� �ϱ�");
			choice = sc.nextInt();
			if(choice == 9999) new View_admin().adminMain(); 
			switch(choice) {
			case 1: inputLogin(); break;
			case 2: inputJoin(); break;
			case 3: inputFindPw(); break;
			}//end switch
		}//end while
		System.out.println("�����մϴ�.");
	}
}
