package dependent;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import base.BaseTests;
import classes.CookieWindowCases;


// Эти тесты зависят от выполнения предыдущих проверок и далее друг от друга, 
// поэтому установлена конкретная последовательность их выполнения 
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepCookieCases extends BaseTests {

/*     @BeforeAll
    public static void setupCheck(){
        assumeTrue(setupCompleted);
    } */

    // Метод жёсткого ожидания
    public void wait(int milliSecond){
        try {
            Thread.sleep(milliSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Case 11
    // Проверяем, что окно куки закрывается при нажатии на кнопку Functional only
    @Test
    @Order(1)
    public void funcOnlyClosesWindow(){ 
        WebElement funcCookieBtn = driver.findElement(By.xpath("//a[@aria-label='Functional only']"));
        WebElement cookieWindow = driver.findElement(By.id("cc-window"));
        String animation = cookieWindow.getCssValue("transition");

        // Проверяем наличие анимации исчезновения
        assertEquals("opacity 1s", animation);

        // Убеждаемся, что окно куки исчезает после их принятия, учитываем время анимации исчезновения
        funcCookieBtn.click();
        wait(1200);
        assertFalse(cookieWindow.isDisplayed(), "Окно куки отображается после принятия кук");
    }

    // Case 12.1
    // Проверяем, что окно кук не отображается при новом открытии страницы
    @Test
    public void funcOnlyNoWindowOpenPage(){
        driver.get(url);
        WebElement cookieWindow = driver.findElement(By.id("cc-window"));
        assertFalse(cookieWindow.isDisplayed(), "Окно куки отображается после принятия кук, после открытия страницы в том же окне того же браузера");
    }

    // Case 12.2
    // Проверяем, что окно кук не отображается при обновлении страницы
    @Test
    public void funcOnlyNoWindowRefresh(){
        driver.navigate().refresh();
        WebElement cookieWindow = driver.findElement(By.id("cc-window"));
        assertFalse(cookieWindow.isDisplayed(), "Окно куки отображается после принятия кук, после обновления страницы");
    }

    // Case 13
    // Проверяем наличие и значение кук после принятия только функциональных
    @Test
    @Order(4)
    public void funcOnlyCookieValue(){
        Cookie cookieStatus = driver.manage().getCookieNamed("complianz_consent_status");
        assertNotNull(cookieStatus, "Отсутствует кука 'complianz_consent_status'");
        assertEquals("dismiss", cookieStatus.getValue(), "Не совпало ожидаемое значение куки при принятии только функциональных");
    }
}
