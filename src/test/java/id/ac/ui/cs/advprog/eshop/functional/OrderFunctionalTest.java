package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createOrderAndFindHistory_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");

        WebElement authorInput = driver.findElement(By.id("authorInput"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        authorInput.clear();
        authorInput.sendKeys("Safira Sudrajat");
        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/order/history"));

        WebElement historyAuthorInput = driver.findElement(By.id("historyAuthorInput"));
        WebElement searchButton = driver.findElement(By.cssSelector("button[type='submit']"));

        historyAuthorInput.clear();
        historyAuthorInput.sendKeys("Safira Sudrajat");
        searchButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Safira Sudrajat"));
        assertTrue(pageSource.contains("WAITING_PAYMENT"));
    }
}