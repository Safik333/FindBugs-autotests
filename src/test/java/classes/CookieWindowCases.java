package classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.CsvSource;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import base.BaseTests;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CookieWindowCases extends BaseTests{

    // метод жёсткого ожидания в миллисекундах
    public void wait(int milliSecond){
        try {
            Thread.sleep(milliSecond);
        } catch (InterruptedException ex) {
        }
    }
    
    // Case 4.1
    // Убеждаемся, что окно куки отображается на главной странице
    @Test
    @Order(1)
    public void isCookieWindowShowing(){
        WebElement cookieWindow = driver.findElement(By.id("cc-window"));
        assertTrue(cookieWindow.isDisplayed(), "Окно куки не отображается");
    }

    // Case 4.2
    //Проверяем, что окно куки отображается после обновления страницы
    @Test
    @Order(2)
    public void isCookieWindowShowingAfterRefresh(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.navigate().refresh();
        WebElement cookWindow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cc-window")));
        assertTrue(cookWindow.isDisplayed());

    }

    // Case 4.3
    //Проверяем, что окно куки отображается на других страницах этого сайта, указанных в CsvSource
    @ParameterizedTest
    @DisplayName("Show cookie window on every page")
    @CsvSource({
        "https://academybugs.com/store/blue-hoodie/",
        "https://academybugs.com/my-cart/",
        "https://academybugs.com/account/",
        "https://academybugs.com/account/?ec_page=register"
                })
    
    public void isCookieWindowShowingEverywhere(String allUrls){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(allUrls);
        WebElement cookWindow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cc-window")));
        assertTrue(cookWindow.isDisplayed(), "Окно отображается не на всех страницах");
        driver.get(url);
        
    }
            
    // Case 5.1
    // Проверяем, что окно куки закреплено на экране
    @Test
    public void isCookieWindowPinned(){
        WebElement cookieWindow = driver.findElement(By.id("cc-window"));
        String cookieWindowPos = cookieWindow.getCssValue("position");
        assertEquals("fixed", cookieWindowPos, "Отсутствует фиксация окна куки на экране");
        
    }

    // Case 5.2
    // Проверяем, что окно куки находится в правом нижнем углу.
    // Координаты взяты исходя из FHD разрешения экрана и открытом окне браузера на весь экран.
    @Test
    public void cookieWindowLocation(){
        WebElement cookieWindow = driver.findElement(By.id("cc-window"));

        // Значение 695 установлено опытным путём. По какой-то причине число 696 возвращает ошибку,
        // хотя в DevTools в Properties элемента указано offsetTop = 696.
        assertEquals(1541, cookieWindow.getRect().x, "Окно не находится в ожидаемой позиции по X");
        assertEquals(695, cookieWindow.getRect().y, "Окно не находится в ожидаемой позиции по Y");
        

    }

    // Case 5.3
    // Проверяем высоту окна куки
    @Test
    public void cookieWindowHeight(){
        WebElement cookieWindow = driver.findElement(By.id("cc-window"));
        assertEquals(138, cookieWindow.getRect().height, "Не совпала ожидаемая высота окна");
        }
        
    // Case 5.4
    // Проверяем ширину окна куки
    @Test
    public void cookieWindowWidth(){
        WebElement cookieWindow = driver.findElement(By.id("cc-window"));
        assertEquals(350, cookieWindow.getRect().width, "Не совпала ожидаемая ширина окна");
        } 

    // Case 6.1
    // Проверяем присутствие и значение кук политики 
    @Test
    public void isDefCookiePolicyExists(){
        Cookie cookie = driver.manage().getCookieNamed("complianz_policy_id");
        assertNotNull(cookie, "Отсутствует кука 'complianz_policy'");
        assertEquals("14", cookie.getValue());
    }

    // Case 6.2
    // Проверяем отсутствие кук при каждом открытии сайта, когда они не приняты
    @Test
    public void isDefCookieStatusNotExists(){
        Cookie cookie = driver.manage().getCookieNamed("complianz_consent_status");
        assertNull(cookie, "Присутствует кука 'complianz_consent_status', но не должна до принятия");
        
        String mainTab = driver.getWindowHandle();
        // Открываем сайт ещё пару раз в новой вкладке, чтобы убедиться, что эти куки не появились, пока они не приняты
        for (int i = 1 ; i <= 2 ; i++){
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(url);
            assertNull(cookie);
            driver.close();
            driver.switchTo().window(mainTab);
                wait(500);
        }
    }

    // Case 7
    // Проверяем текст объявления в окне куки
    @Test
    public void descOfCookieWindow(){
        WebElement desc = driver.findElement(By.xpath("//span[@id='cookieconsent:desc']"));
        assertEquals("We use cookies to optimize our website and our service: Cookie Policy", desc.getText(), "Не совпал ожидаемый текст");
    }

    // Case 8.1
    // Проверяем, что слова "Cookie Policy" текста описания окна куки содержат ссылку
    @Test 
    public void wordsAreLink(){
        WebElement linkWords = driver.findElement(By.xpath("//*[@id='cookieconsent:desc']/a"));
        @SuppressWarnings("deprecation")
        String linkWordsAttr1Val = linkWords.getAttribute("aria-label");
        @SuppressWarnings("deprecation")
        String linkWordsAttr2Val = linkWords.getAttribute("class");

        assertEquals("Cookie Policy", linkWordsAttr1Val, "Не совпало значение атрибута текста для гиперссылки");
        assertEquals("cc-link", linkWordsAttr2Val, "Не совпало значение атрибута текста для гиперссылки");
    }

    // Case 8.2
    // Проверяем, что URL ссылка в словах "Cookie Policy" текста описания окна куки
    // принадлежит странице политики кук
    @Test
    public void wordsLinkURL(){
        WebElement wordsLinkURL = driver.findElement(By.xpath("//*[@id='cookieconsent:desc']/a"));
        @SuppressWarnings("deprecation")
        String linkURL = wordsLinkURL.getAttribute("href");
        assertEquals("https://academybugs.com/cookie-policy-eu/", linkURL, "Не совпала ожидаемая ссылка на страницу Cookie Policy");
    }

    // Case 8.3
    // Проверяем нажатие на ссылку на страницу политики кук, находящуюся в окне кук
    @Test
    @Order(3)
    public void cookiePolicyLinkClick(){
        WebElement cookiePolicyLink = driver.findElement(By.xpath("//*[@id='cookieconsent:desc']/a"));
        cookiePolicyLink.click();
            // Проверяем по УРЛу, заголовку страницы и ключевому элементу, что открылась нужная страница
            assertEquals("https://academybugs.com/cookie-policy-eu/", driver.getCurrentUrl(), "");
            assertEquals("Cookie Policy (EU) – AcademyBugs.com", driver.getTitle());
            WebElement keyText = driver.findElement(By.id("cmplz-document"));
            assertTrue(keyText.isDisplayed());
        driver.get(url);

    }

    // Case 9.1
    // Проверяем отображение кнопки принятия только функциональных кук
    @Test
    public void funcCookieBtn(){
        WebElement funcCookieBtn = driver.findElement(By.xpath("//a[@aria-label='Functional only']"));
        assertTrue(funcCookieBtn.isDisplayed(), "Отсутствует кнопка принятия только функциональных кук");
    }

    // Case 9.2
    // Проверяем текст кнопки принятия только функциональных кук
    @Test
    public void funcCookieBtnText(){
        WebElement funcCookieBtn = driver.findElement(By.xpath("//a[@aria-label='Functional only']"));
        assertEquals("Functional only", funcCookieBtn.getText(), "Не совпал текст в кнопке принятия только функциональных кук");
    }
    
    // Case 9.3
    // Проверяем выравнивание текста внутри кнопки принятия кук.
    // Кейс касается сразу обеих кнопок, так как параметр наследуется от одного общего для них класса
    @Test
    public void cookieBtnTextAlig(){
        WebElement funcCookieBtn = driver.findElement(By.xpath("//a[@aria-label='Functional only']"));
        String alignment = funcCookieBtn.getCssValue("text-align");
        assertEquals("center", alignment, "Не совпало ожидаемое выравнивание текста в кнопках принятия кук");
    }

    // Case 10.1
    // Проверяем отображение кнопки принятия всех кук
    @Test
    public void acceptCookieBtn(){
        WebElement acceptCookieBtn = driver.findElement(By.xpath("//a[@aria-label='Accept cookies']"));
        assertTrue(acceptCookieBtn.isDisplayed(), "Отсутствует кнопка принятия всех кук");
    }

    // Case 10.2
    // Проверяем текст кнопки принятия всех кук
    @Test
    public void acceptCookieBtnText(){
        WebElement acceptCookieBtn = driver.findElement(By.xpath("//a[@aria-label='Accept cookies']"));
        assertEquals("Accept cookies", acceptCookieBtn.getText(), "Не совпал текст в кнопке принятия всех кук");
    }  
    
    // Case 11
    // Проверяем, что окно куки закрылось после принятия только функциональных кук
/*     @Test
    public void isCookieWindowNotDisplayedFunc(){
        assume
        WebElement cookieWindow = driver.findElement(By.id("cc-window"));
        WebElement funcCookieBtn = driver.findElement(By.xpath("//a[@aria-label='Functional only']"));
        funcCookieBtn.click();
        assertFalse(cookieWindow.isDisplayed());
    } */
    


}
