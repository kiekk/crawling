package crawling;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import dao.BookDAO;


public class Crawling {
	Connection conn;
	PreparedStatement pstm;
	ResultSet rs;
	ArrayList<String> book_name = new ArrayList<String>();
	ArrayList<String> author = new ArrayList<String>();
	ArrayList<String> company = new ArrayList<String>();
	ArrayList<String> book_date = new ArrayList<String>();
	
	//���� �˻� �� ����
	public void searchingBook() {
		Scanner sc = new Scanner(System.in);
		String book_name = "",author = "",company = "",book_date = "";
		int select = 0;
		try {
			Document document;
			System.out.println("�˻��� �������� �Է����ּ���");
			String searchWord = sc.nextLine();
			document = Jsoup.connect("https://www.aladin.co.kr/search/wsearchresult.aspx?SearchTarget=Book&SearchWord="+searchWord).get();
			//������
			Elements elements = (Elements) document.select("#keyword_wrap table tbody tr td #Myform #Search3_Result "
					+ ".ss_book_box table tbody tr td table tbody tr td .ss_book_list li a b");
			book_name = elements.get(0).text();
			//����, ���ǻ�, ������
			elements = (Elements) document.select("#keyword_wrap table tbody tr td #Myform #Search3_Result "
					+ ".ss_book_box table tbody tr td table tbody tr td .ss_book_list li");
//			elements.forEach(value -> System.out.println(value.text()));
			for(int i = 0;i<elements.size();i++) {
				if(!elements.get(i).text().contains("|")) {
					continue;
				}else {
					//�Ľ�, ���ڿ� �ɰ���
					author = elements.get(i).text().substring(0, elements.get(i).text().indexOf("|"));
					company = elements.get(i).text().substring(elements.get(i).text().indexOf("|")+2, elements.get(i).text().lastIndexOf("|"));
					//2020�� 03�� -> 2020.03.01������ ���� �����ֱ�
					//2020.06.01
					book_date = elements.get(i).text().substring(elements.get(i).text().lastIndexOf("|")+2, elements.get(i).text().length()-1).replace("�� ", ".").concat(".01");
					break;
				}
			}
			System.out.println("�˻� ���\n������ : "+book_name+"\n���� : "+author+"\n���ǻ� : "+company+"\n������ : "+book_date);
			System.out.println("�ش� ������ ��Ͽ� �߰��Ͻðڽ��ϱ�?\n1.��\t2.�ƴϿ�"); select = sc.nextInt();
			if(select == 1) {
				new BookDAO().addBookList(book_name, author, company, book_date);
			}else {
				System.out.println("����մϴ�.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//���� ��� ũ�Ѹ�
	void CrawlingBookList() {
		try {
			Document document;
			document = Jsoup.connect("http://www.ypbooks.co.kr/book_arrange.yp?targetpage=book_week_best&pagetype=5&depth=1").get();

			Elements elements = (Elements) document.select("#wrap #allFrame #contents #rightArea01 b b b .bookNewList "
					+ ".listView .nbookTit");
			elements.stream().forEach(name -> book_name.add(name.text()));
//			elements.stream().forEach(name -> System.out.println(name.text()));
			
			elements = (Elements) document.select("#wrap #allFrame #contents #rightArea01 b b b .bookNewList"
					+ " .listView .nbookName");
			//�Ľ�, ���ڿ� �ɰ���
			elements.stream().forEach(value -> {
				StringTokenizer str = new StringTokenizer(value.text(),"|");
				author.add(str.nextToken());
				company.add(str.nextToken());
				book_date.add(str.nextToken());
			}
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//ũ�Ѹ��� ���� ��� ����
		for(int i = 0;i<book_name.size();i ++) {
			new BookDAO().addBookList(book_name.get(i),author.get(i),company.get(i),book_date.get(i));
		}
	}
	public static void main(String[] args) {
		//�������� ũ�Ѹ� �޼ҵ�
		new Crawling().CrawlingBookList();
	}
}
