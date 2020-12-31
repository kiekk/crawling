package crawling;

import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Melon {
	private WebDriver driver;
	private String url;
	
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	
	public Melon() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
//		ChromeOptions options = new ChromeOptions();
//		driver = new ChromeDriver(options);
		
		driver = new ChromeDriver();
		url = "https://www.melon.com/";
	}
	
	public static void main(String[] args) {
		Melon m = new Melon();
		m.operate();
	}
	
	public void operate() {
		//���縦 ã���ִ� �޼ҵ� ���
		//����̹� ����(close, quit)
		try {
			searchSong();
		} catch (Exception e) {
			
		} finally {
			driver.close();
			driver.quit();
		}
	}
	
	//�뷡 ������ �Է¹ް�, �˻� ����� �ѷ��� �� ����ڰ� ������ �뷡�� ���縦 ������ش�.
	public void searchSong() {
		//������ �±� ��ҵ��� ������ ����
		WebElement element = null;
		Scanner sc = new Scanner(System.in);
		
		//����ڿ��� ������ �޼���
		String searchMsg = "�뷡 ���� : ";
		String noSuchMsg = "�˻��� ����� �����ϴ�.";
		String lyricNumMsg = "�� ��ȣ : ";
		
		//�˻�â �±� ������ ����
		//sendKeys("")
		//sendKeys(Keys.RETURN) : ����
		//ui-autocomplete-input
		try {
			//ã�ư� �ּҸ� �˷���.
			driver.get(url);
			
			//�˻�â �±� ��������
			element = driver.findElement(By.className("ui-autocomplete-input")); 
			System.out.print(searchMsg);
			
			//����ڰ� �Է��� Ű���带 ��� ����Ʈ �˻�â�� �־��ֱ�
			element.sendKeys(sc.nextLine());
			
			//���� ���ֱ�
			element.sendKeys(Keys.RETURN);
			
			//�˻� ��� ������ �ε� ��ٷ��ֱ�
			Thread.sleep(1000);
			
			//�˻� ����� ���� ���� �޼��� �±� ��������
			element = driver.findElement(By.className("section_no_data"));
			
			//����� �Դٴ� ���� �˻� ����� ���ٴ� ��!
			System.out.println(noSuchMsg);
			
		} catch (NoSuchElementException nsee) {
			//���� �˻� ����� ���� �ʴٸ� ���ܰ� �߻��Ͽ� ����� ���Եȴ�
			//�˻������ �ִٸ� section_no_data �±׸� ã�� ���ϱ� ����!
			
			//�� ������ ǥ�õ� �����±׸� ������ �´�.
			element = driver.findElement(By.id("frm_searchSong"));
			
			//�ش� ���� �±׿��� �� ��ȣ, ���, ��Ƽ��Ʈ���� ListŸ������ �����´�.
			//���� Ŭ���� �̸��� id�� ���� ���� �ֱ� �����̴�.
			List<WebElement> numList = element.findElements(By.className("no"));
			List<WebElement> titleList = element.findElements(By.className("fc_gray"));
			List<WebElement> artistList = element.findElements(By.id("artistName"));
			
			for (int i = 0; i < numList.size(); i++) {
				//����ڿ��� �˻��� �� �������� ������ش�.
				System.out.println(numList.get(i).getText() + ". " 
						+ titleList.get(i).getText() + ", ��Ƽ��Ʈ : " + artistList.get(i).getText());
			}
			System.out.print(lyricNumMsg);
			//����ڰ� ������ �� ��ȣ�� �Է¹޴´�.
			int num = sc.nextInt();
			
			//�� ��� �󼼺��� ��ư�� ��ġ�Ǿ� �ֱ� ������ List Ÿ������ ��� �����´�.
			List<WebElement> detailList = element.findElements(By.className("btn_icon_detail"));
			
			//����ڰ� ������ �� ��ȣ�� 1���� �����ϱ� ������ �ε��� ��ȣ�� Ȱ���ϱ� ���ؼ� -1�� ���ش�.
			//����ڰ� ������ ���� �󼼺��� a �±׸� Ŭ�����ش�.
			detailList.get(num - 1).click();
			try {Thread.sleep(1000);} catch (InterruptedException e) {;}

			try {
				//�󼼺��� ���������� ���� ��ġ�� ��ư ��ü�� ������ �´�.
				driver.findElement(By.className("button_more")).click();
				try {Thread.sleep(1000);} catch (InterruptedException e) {;}
				
				//���� ��ġ�� ��ư�� �ִٸ� ���� �ؽ�Ʈ�� ��� �ִ� �±װ�ü�� ������ �´�.
				element = driver.findElement(By.className("lyric"));
				//�ش� �±� �ȿ� �ִ� ���縦 �����ͼ� ������ش�.
				System.out.println(element.getText());
				
			} catch (NoSuchElementException nsee2) {
				//��ġ�� ��ư�� ���ٸ� ���簡 ���ų�, ���� �뷡�̹Ƿ� ��� �޼����� ������ش�.
				System.out.println("�ش� ���� ���縦 ������ �� �����ϴ�.");
			}
		} catch (InterruptedException itte) {;}
		
		
		//By.id("")
		//By.className("")
		
		//�˻� ����� ���� ���������� �������� ������ֱ�
		//����ڰ� ������ ���� ���� ��ư Ŭ�� > ���ĺ��� Ŭ�� �� ���� �ؽ�Ʈ ������ֱ�
		
	}
}










