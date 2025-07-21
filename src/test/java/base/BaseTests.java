package base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BaseTests {

    protected static WebDriver driver;
    protected static String url = "https://academybugs.com/find-bugs/";
    

    @BeforeAll
    public static void beforeAll(){

        
        // Перед всеми тестами запускается браузер, увеличивается на весь экран и происходит переход на тестируемый веб-сайт;
        
        //System.setProperty("webdriver.chrome.driver", "E:\\Files\\WebDrivers\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);

        // Установка таймеров
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(8));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5)); // ожидание выполнения АСИНХРОННОГО скрипта, который работает параллельно с тест-кейсом
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15)); // ожидает появление элемента на странице



        // Case 1. Убедиться, что страница открылась, прочитав её заголовок и ключевой элемент
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement mainHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()='Find Bugs']")));
        assertEquals("Find Bugs – AcademyBugs.com", driver.getTitle());
        Assertions.assertEquals("Find Bugs", mainHeader.getText(), "Провалена проверка на читаемость элемента");


        
                
    }

    @BeforeEach
    void beforeEach(){
        // Перед каждым кейсом происходит возврат на главную страницу
        // driver.get(url);
        
    }

    @AfterEach 
    void afterEach(){

    }

    @AfterAll
    static void afeterAll(){
        
    }
}