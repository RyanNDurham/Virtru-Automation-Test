import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class VirtruAutomationTest
    {
        public static void main(String[] args)
        {
            System.setProperty("webdriver.chrome.driver", "../Virtru-Automation-Test/Selenium/chromedriver.exe");
            ChromeDriver driver = new ChromeDriver();
            driver.get("http://www.google.com");
        }
    }
