package vo;

public class BookVO {
	private int book_number;			//������ȣ
	private String book_name;			//������
	private String author;				//����
	private String company;				//���ǻ�
	private String book_date;			//������
	private String book_section;		//������ġ
	private String book_rental;			//�뿩����('�뿩��', '�뿩����')
	private int rental_count;			//�뿩Ƚ��
	
	public int getBook_number() {
		return book_number;
	}
	public void setBook_number(int book_number) {
		this.book_number = book_number;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBook_date() {
		return book_date;
	}
	public void setBook_date(String book_date) {
		this.book_date = book_date;
	}
	public String getBook_section() {
		return book_section;
	}
	public void setBook_section(String book_section) {
		this.book_section = book_section;
	}
	public String getBook_rental() {
		return book_rental;
	}
	public void setBook_rental(String book_rental) {
		this.book_rental = book_rental;
	}
	public int getRental_count() {
		return rental_count;
	}
	public void setRental_count(int rental_count) {
		this.rental_count = rental_count;
	}
	
	@Override
	public String toString() {
		return "���� ��ȣ : " + book_number + ", ������ : " + book_name + ", ���� : " + author + ", ���ǻ� : "
				+ company + ", ������ : " + book_date + ", ���� ��ġ : " + book_section + ", �뿩���ɿ��� : "
				+ book_rental + ", [�뿩 Ƚ�� : " + rental_count +"]";
		

	}
	
}
