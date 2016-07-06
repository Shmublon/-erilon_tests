package test.java;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class GameGnomClassTest {

    private WebDriver driver;
    private ScreenshotHelper screenshotHelper;


    @Before
    public void openBrowser() {


        driver = new ChromeDriver();
        driver.get("http://erilon-staging.herokuapp.com/");
        screenshotHelper = new GameGnomClassTest.ScreenshotHelper();
    }

    @After
    public void saveScreenshotAndCloseBrowser() throws IOException, InterruptedException {

        final Wait<WebDriver> wait = new WebDriverWait(driver, 5).ignoring(StaleElementReferenceException.class, ElementNotVisibleException.class);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        screenshotHelper.saveScreenshot("screenshot.png");
        WebElement mainMenuButton = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[1]/h4/a"));
        mainMenuButton.click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/ul/li[10]/a"))));
        WebElement myCharactersButton = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/ul/li[10]/a"));
        myCharactersButton.click();
        driver.switchTo().alert().accept();
        WebElement moreForCharacterButton = driver.findElement(By.xpath("/html/body/div/div[1]/table/tbody/tr[4]/td[2]/div/button"));
        moreForCharacterButton.click();
        WebElement xDellButton = driver.findElement(By.xpath("/html/body/div/div[1]/table/tbody/tr[4]/td[2]/div/ul/li[3]/a"));
        xDellButton.click();
        driver.close();
    }

    @Test
    public void gameTest() throws IOException, InterruptedException {

        final Wait<WebDriver> wait = new WebDriverWait(driver, 5).ignoring(StaleElementReferenceException.class, ElementNotVisibleException.class);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        assertEquals("The page title should e qual Google at the start of the test.", "Вход", driver.getTitle());
        WebElement enterField = driver.findElement(By.name("form-username"));
        enterField.sendKeys("shmublon");
        WebElement enterButton = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[2]/div/div[2]/form/button"));
        enterButton.click();
        WebElement nameField = driver.findElement(By.id("name"));
        nameField.sendKeys("Тестовый персонаж");
        Select raceSelector = new Select(driver.findElement(By.id("race_id")));
        raceSelector.selectByVisibleText("Гном");
        WebElement experienceField = driver.findElement(By.id("experience"));
        experienceField.sendKeys("200");
        WebElement addCharacterButton = driver.findElement(By.xpath("/html/body/div/div[2]/form/div[5]/div/button"));
        addCharacterButton.click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("/html/body/div/h3"))));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("/html/body/div/div[3]/div[1]/div[2]/p"))));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("/html/body/div/form/div/div[2]/div/p/strong"))));

        assertEquals("Name should be \"Тестовый персонаж\"", "тестовый персонаж",
                driver.findElement(By.xpath("/html/body/div/h3")).getText().toLowerCase());
        assertEquals("Расса персонажа должна быть \"Гном\"", "гном"
                , driver.findElement(By.xpath("/html/body/div/div[3]/div[1]/div[2]/p")).getText().toLowerCase());
        assertEquals("Опыт персонажа должна быть 200", "200"
                , driver.findElement(By.xpath("/html/body/div/form/div/div[2]/div/p/strong")).getText().toLowerCase());
    }

    private class ScreenshotHelper {

        public void saveScreenshot(String screenshotFileName) throws IOException {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(screenshotFileName));
        }
    }
}