import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;

import static org.junit.Assert.assertEquals;
@RunWith(DataProviderRunner.class)

public class First_Test {
    private WebDriver driver;
    private String baseUrl;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "https://sandbox.cardpay.com/MI/cardpayment2.html?orderXml=PE9SREVSIFdBTExFVF9JRD0nODI5OScgT1JERVJfTlVNQkVSPSc0NTgyMTEnIEFNT1VOVD0nMjkxLjg2JyBDVVJSRU5DWT0nRVVSJyAgRU1BSUw9J2N1c3RvbWVyQGV4YW1wbGUuY29tJz4KPEFERFJFU1MgQ09VTlRSWT0nVVNBJyBTVEFURT0nTlknIFpJUD0nMTAwMDEnIENJVFk9J05ZJyBTVFJFRVQ9JzY3NyBTVFJFRVQnIFBIT05FPSc4NzY5OTA5MCcgVFlQRT0nQklMTElORycvPgo8L09SREVSPg==&sha512=998150a2b27484b776a1628bfe7505a9cb430f276dfa35b14315c1c8f03381a90490f6608f0dcff789273e05926cd782e1bb941418a9673f43c47595aa7b8b0d";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    }

    @DataProvider
    public static Object[][] dataPR() {
        return new Object[][]{
                {"4000000000000002", "CONFIRMED"},
                {"5555555555554444", "DECLINED BY ISSUING BANK"}
        };

    }

    @Test
    @UseDataProvider("dataPR")
    public void confirmedPayment(String CardNum, String PR) {
        driver.get(baseUrl);
        String Order_number = (String) driver.findElement(By.id("order-number")).getText();
        String Unlimint_Payment_Page = (String) driver.getTitle();
        String Total = (String) driver.findElement(By.id("total-amount")).getText();
        String currency = (String) driver.findElement(By.id("currency")).getText();
        driver.findElement(By.id("input-card-number")).click();
        driver.findElement(By.id("input-card-number")).clear();
        driver.findElement(By.id("input-card-number")).sendKeys(CardNum);
        driver.findElement(By.id("input-card-holder")).click();
        driver.findElement(By.id("input-card-holder")).clear();
        driver.findElement(By.id("input-card-holder")).sendKeys("BORIS");
        driver.findElement(By.id("card-expires-month")).click();
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("01");
        driver.findElement(By.id("card-expires-year")).click();
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2025");
        driver.findElement(By.id("input-card-cvc")).click();
        driver.findElement(By.id("input-card-cvc")).clear();
        driver.findElement(By.id("input-card-cvc")).sendKeys("123");
        driver.findElement(By.id("action-submit")).click();
        driver.findElement(By.id("success")).click();
        assertEquals(Order_number, driver.findElement(By.xpath("//*[@id=\"payment-item-ordernumber\"]/div[2]")).getText());
        assertEquals(PR, driver.findElement(By.xpath("//*[@id=\"payment-item-status\"]/div[2]")).getText().toUpperCase());
    }
 @Test
 public void screenshotCVC() throws IOException {
        driver.get(baseUrl);
        driver.manage().window().fullscreen();

     Actions action = new Actions(driver);

     WebElement CVChint = driver.findElement(By.xpath("//*[@id=\"cvc-hint-toggle\"]"));
     WebElement CVCBuble = driver.findElement(By.xpath("//*[@id=\"cvc-hint-bubble\"]"));
     action.moveToElement(CVChint).click().build().perform();

     Screenshot screenshot= new AShot().shootingStrategy(ShootingStrategies.viewportPasting(10000)).takeScreenshot(driver);

     ImageIO.write(screenshot.getImage(), "jpg", new File("target/3.jpg"));
 }


    @After
    public void tearDown() {
        driver.quit();
    }
}
