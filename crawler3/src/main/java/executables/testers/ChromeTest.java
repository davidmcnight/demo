package executables.testers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeTest {

    public static void main(String[] args){
        try {
            System.out.println("Test");
            System.setProperty("webdriver.chrome.driver","/Users/specbooks/Tools/chromedriver");
            WebDriver webDriver = new ChromeDriver();
            webDriver.get("https://www.baldwinhardware.com/products/category/door-hardware/estate/knobs");
            Thread.sleep(6000);
            String pageSource = webDriver.getPageSource();
            webDriver.close();

        System.out.println(pageSource);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
