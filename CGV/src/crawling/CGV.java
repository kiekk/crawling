package crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CGV {
	//������ ����̹��� ���� �� �ִ� ��ü
	private WebDriver driver;
	
	//ũ�� �������� ����
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	
	public static void main(String[] args) {
		CGV cgv = new CGV();
		
		//�ش� ����Ʈ�� �±װ�ü�� ���� �� �ִ� ��ü
		WebElement el1 = null;
		WebElement el2 = null;
		
		//����̹� ����
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
		//�ɼ��� �� ��
		ChromeOptions options = new ChromeOptions();
		//���� �� �������� �ܺΰ� �ƴ� ���η� �۵��ϰ� �ϴ� ����(�������� ���� ������ �ʰ� ��)
		options.addArguments("headless");
		cgv.driver = new ChromeDriver(options);
		
		//�ɼ��� ���� ��
//		cgv.driver = new ChromeDriver();
		
		//ó���� ������ �ּҸ� �ۼ��Ѵ�.
		String url = "http://www.cgv.co.kr/movies/";
		try {
			//������ �ּҸ� ����ش�.
			cgv.driver.get(url);
			//������ �ε��� �Ϸ�Ǳ������ �ð��� �����ش�.
			Thread.sleep(1000);
			
			//Ŭ���� �̸����� �±׸� ã�Ƽ� el1 ��ü�� �±׸� ����ش�.
			//btn-more-fontbold
			el1 = cgv.driver.findElement(By.className("btn-more-fontbold"));
			//�ش� �±װ� a, button �� Ŭ���̺�Ʈ�� �����Ǿ� �ִٸ� click()�� ���ؼ� ��û�� �� �ִ�.
			el1.click();
			
			//Ŭ�� �� �������� �ε��� �ð��� �����ش�.
			Thread.sleep(1000);
			
			//sect-movie-chart
			el2 = cgv.driver.findElement(By.className("sect-movie-chart"));
			
			//������ �±� �ȿ� ã�� �±װ� �ִٸ� �� �� �� findElement�� ����� �� �ְ�,
			//�ش� �±װ� ���� ���� ������ findElements�� ����ؼ� ListŸ������ �����´�.
			for(WebElement data : el2.findElements(By.className("title"))) {
				//�ش� �±� �ȿ� �ִ� �ؽ�Ʈ�� ������ �� getText()�� ����Ѵ�.
				System.out.println(data.getText());
			}
			
		} catch (InterruptedException e) {
			
		} finally {
			//�ܺο� ���� ��� �������� ��������ش�.
			cgv.driver.close();
			cgv.driver.quit();
		}
	}
}












