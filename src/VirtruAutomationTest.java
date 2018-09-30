import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;




public class VirtruAutomationTest extends AutomationTools
    {

        public static void main(String[] args)
        {
            // Variable to store the path for the text file
            String textFilePath = "../Virtru-Automation-Test/EmailBody.txt";

            // Variables
            String emailSubject;
            String emailBody;
            String textFile;

            // Chrome options for the virtru add-on to be installed
            ChromeOptions options = new ChromeOptions();
            options.addExtensions(new File("../Virtru-Automation-Test/Selenium/googleExtensions/extension_7_6_4_0.crx"));

            // System property to set location of Chrome driver needed.
            System.setProperty("webdriver.chrome.driver", "../Virtru-Automation-Test/Selenium/chromedriver.exe");

            // Start the driver  with the add-on options
            ChromeDriver driver = new ChromeDriver(options);

            // Maximize the window
            driver.manage().window().maximize();

            // Add an implicit wait just in case
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

            // Invoke the first half of the test by logging into the sender account and sending the email
            // The randomly generated subject is returned and stored for later use
            emailSubject = SendEmail(driver, textFilePath);

            // Close and quit the current driver to start a new driver without the virtru add-on
            driver.quit();

            // Starting the new driver without the add-on
            driver = new ChromeDriver();

            // Maximize the window
            driver.manage().window().maximize();

            // Add an implicit wait just in case
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

            // Invoke the second half of the test to get the email body from the receiving account
            // The body of the email is returned and stored for comparison
            emailBody = RetrieveEmailBody(driver, emailSubject);

            // Store the text file into a string
            textFile = GetTextFromFile(textFilePath);

            // Print out both of the strings to see in the console
            // Since there are many ways to do new lines in strings that are invisible, I used regex to get rid of all
            // empty space no matter how many spaces it is with a single space. This fixes any issues with the different
            // ways a new line can be displayed.
            System.out.println(emailBody.replaceAll("\\s+"," ").trim());
            System.out.println(textFile.replaceAll("\\s+"," ").trim());

            // Assert that the text file and the sent email body are the same
            Assert.assertTrue(Objects.equals(emailBody.replaceAll("\\s+"," ").trim(), textFile.replaceAll("\\s+"," ").trim()));

            // Print out true or false if the text file and sent email body are the same
            System.out.println(Objects.equals(emailBody.replaceAll("\\s+"," ").trim(), textFile.replaceAll("\\s+"," ").trim()));


            // Close and quit the driver at the end of the test
            driver.quit();
        }
    }
