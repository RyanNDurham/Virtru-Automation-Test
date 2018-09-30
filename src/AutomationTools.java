import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Set;
import org.testng.Assert;

public class AutomationTools {

    // Sender information
    static String senderUsername = "virtrutestsenderrd";
    static String senderEmail = "virtrutestsenderrd@gmail.com";
    static String senderPass = "P@ssword11";

    // Receiver information
    static String receiverUsername = "virtrutestreceiverrd";
    static String receiverEmail = "virtrutestreceiverrd@gmail.com";
    static String receiverPass = "P@ssword22";


    // Sleep method so the try catch would not have to be written more than once.
    public static void Sleep(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    // Gets text from file and puts it into a string.
    public static String GetTextFromFile(String textFilePath)
    {
        String fileText = "";
        try {
            fileText = new String(Files.readAllBytes(Paths.get(textFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileText;
    }

    // Random subject line generator. Used to find the email on the receiving account as well.
    public static String GenerateRandomSubject()
    {
        // Variables for the generator. alphabet is what contains the characters to be used. emailSubject is where the subject will be stored.
        final String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ?/.";
        String emailSubject = "";

        // Random generator
        Random r = new Random();

        // For loop that uses the length of alphabet for random generator and then adds that character to emailSubject
        // to create a randomly generated 20 character email subject to be used for searching for the email on the
        // receiving email account.
        for(int i = 0; i < 20; i++)
        {
            emailSubject = emailSubject + alphabet.charAt(r.nextInt(alphabet.length()));
        }

        return emailSubject;
    }

    public static void SwitchTabs(WebDriver driver)
    {
        Set<String> handles = driver.getWindowHandles();

        String currentHandle = driver.getWindowHandle();
        for (String handle : handles) {

            if (!handle .equals(currentHandle))
            {
                driver.switchTo().window(handle);
            }
        }
    }

    // Login method to login into Gmail.com
    public static void Login(ChromeDriver driver, String userName, String password)
    {
        // Types in the username and clicks next
        driver.findElement(By.xpath("//input[@type = 'email']")).sendKeys(userName);
        driver.findElement(By.id("identifierNext")).click();

        // Thread sleep for page to load
        Sleep(1000);

        // Types in the password and clicks next
        driver.findElement(By.xpath("//input[@type = 'password']")).sendKeys(password);
        driver.findElement(By.id("passwordNext")).click();

        // Thread sleep for page to load
        Sleep(2000);

        // Assert if the user is logged into the correct account
        Assert.assertTrue(driver.getTitle().contains(userName));
    }

    public static String SendEmail(ChromeDriver driver, String textFilePath)
    {
        String subject;

        // Go to gmail.com
        driver.get("https://gmail.com");

        // Invoke login method to login as Sender
        Login(driver, senderUsername, senderPass);

        // Click compose button to start a new email
        driver.findElement(By.xpath("//div[text() = 'Compose']")).click();

        // Thread sleep for virtru first steps to display
        Sleep(1000);

        // Close Virtru first steps help guide
        driver.findElement(By.xpath("//a[text() = 'Skip, I already know how to use Virtru']")).click();

        // Turn Virtru encryption system on
        driver.findElement(By.xpath("//div[@class = 'virtru-slider']")).click();

        // Click the activation so the sender can send the email
        driver.findElement(By.xpath("//a[@class = 'virtru-firsttime-activation-button virtru-activation-button']")).click();

        // Thread sleep for the page to load
        Sleep(1000);

        // Invoke the subject generator method to get a 20 character random subject used to search for the email later
        subject = GenerateRandomSubject();

        // Input the information for the email being sent
        driver.findElement(By.xpath("//textarea[@name = 'to']")).sendKeys(receiverEmail);
        driver.findElement(By.xpath("//input[@name = 'subjectbox']")).sendKeys(subject);
        driver.findElement(By.xpath("//div[@aria-label = 'Message Body']")).sendKeys(GetTextFromFile(textFilePath));

        // Click the send button to send the email.
        driver.findElements(By.xpath("//div[text() = 'Secure Send']")).get(0).click();

        // Thread sleep to make sure the email is sent
        Sleep(2000);

        // Click the profile icon for logout dropdown
        driver.findElement(By.xpath("//a[contains(@aria-label,'"+ senderEmail +"')]")).click();

        // Thread sleep to wait for dropdown
        Sleep(1000);

        // Logout of sender account
        driver.findElement(By.xpath("//a[text() = 'Sign out']")).click();

        //Thread sleep to show the user has been logged out
        Sleep(3000);

        // Assert that the user has been logged out
        Assert.assertFalse(driver.getTitle().contains(senderUsername));

        // Return the randomly generated subject
        return subject;
    }

    public static String RetrieveEmailBody(ChromeDriver driver, String subject)
    {

        // Go to Gmail.com
        driver.get("https://gmail.com");

        //Invoke login method to login as receiver
        Login(driver, receiverUsername, receiverPass);

        // Store search bar WebElement to follow steps needed to search
        WebElement mailSearchBar = driver.findElement(By.xpath("//input[@aria-label = 'Search mail']"));

        // First Click search bar to gain focus
        mailSearchBar.click();

        // Second send the subject that we are searching for
        mailSearchBar.sendKeys(subject);

        // Third and final step is to press the enter key to finish the search
        mailSearchBar.sendKeys(Keys.ENTER);

        // Thread sleep for the search to load
        Sleep(2000);

        // Click the email based off the subject
        driver.findElements(By.xpath("//span[@class = 'bog']//span[text() = '"+ subject +"']")).get(1).click();

        // Thread sleep for the email to load
        Sleep(1000);

        // Click the button to start the authentication for the encrypted email
        driver.findElement(By.xpath("//a[contains(@href, 'secure.virtru.com/start')]")).click();

        // Thread Sleep for tab to load
        Sleep(4000);

        // Invoke switch tabs method to switch window focus to the newly opened tab
        SwitchTabs(driver);

        // Click the email to authenticate
        driver.findElement(By.cssSelector("a>div[data-email='virtrutestreceiverrd@gmail.com']")).click();

        // Click Gmail as the way to authenticate
        driver.findElement(By.xpath("//a[text() = 'Login With']")).click();

        // thread sleep for the encrypted email to load
        Sleep(4000);

        // Return the body of the email sent to assert if the email body is the same
        return driver.findElement(By.xpath("//span[@id = 'tdf-body']/div")).getText();
    }


}
