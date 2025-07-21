package classes;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseTests;

public class BrowserInteractionCases extends BaseTests {

    // метод ожидания в миллисекундах
    public void wait(int milliSecond){
        try {
            Thread.sleep(milliSecond);
        } catch (InterruptedException ex) {
        }
    }


    // Case 2
    @Test
    public void browserNavigation(){
        driver.navigate().back();
        wait(1000);
        driver.navigate().forward();
       
        //Кнопками в браузере прошлись назад и вперёд, теперь, читая заголовок, убеждаемся, что сайт открылся
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement mainHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()='Find Bugs']")));
        Assertions.assertEquals("Провалена проверка на читаемость заголовка. Возможно сайт не открылся.", "Find Bugs", mainHeader.getText());
        
    }

    // Case 3

        // Этот метод собирает размер выбранного элемента и сравнивает их с шириной и высотой области отображения, 
        // возвращая true, если элемент полностью входит в область отображения, и false, если хотя бы одна граница элемента
        // выходит за границу области отображения браузера
        public boolean isElementInViewport(WebElement element){
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Собираем размеры области отображения браузера
            long viewportHeight = (long) js.executeScript("return window.innerHeight");
            long viewportWidth = (long) js.executeScript("return window.innerWidth");
            long pageYOffset = (long) js.executeScript("return window.pageYOffset");

            // Собираем координаты выбранного элемента и суммируем с его шириной/высотой
            element.getRect();
            long finalWidth = element.getRect().x + element.getRect().width;
            long finalHeight = (element.getRect().y + element.getRect().height) - pageYOffset;

            return  finalWidth >= 0 && 
                    finalHeight >= 0 &&
                    finalWidth <= viewportWidth &&
                    finalHeight <= viewportHeight;

        } 
                                                                    
    // Case 3.1                                                    
    @Test
    public void scrollDown() {
        // Прокручиваем страницу вниз
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        
        // Проверяем, что достигли конца страницы
        assertTrue(js.executeScript("return window.pageYOffset == document.body.scrollHeight - window.innerHeight;").equals(true),
        "Не совпало ожидаемое положение прокрутки в конце страницы");

        //Также проверяем, что элемент, который должен находиться в конце страницы, находится полностью в поле отображения браузера
        WebElement element = driver.findElement(By.xpath("//img[@alt='uTest']"));
        assertTrue(isElementInViewport(element), "В области отображения отсутствует ожидаемый элемент внизу страницы");

    }
    // Case 3.2  
    @Test
    public void scrollUp() {
        // Прокручиваем страницу вверх
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");
        
        // Убеждаемся, что вернулись в начало
        assertTrue(js.executeScript("return window.pageYOffset == 0;").equals(true));

        // Также проверяем, что элемент, который должен находиться на верху страницы, находится полностью в поле отображения браузера
        WebElement element = driver.findElement(By.xpath("//h3[text()='Find Bugs']"));
        assertTrue(isElementInViewport(element), "В области отображения отсутствует ожидаемый элемент на верху страницы");
    }

    /* @Test
    void testSmoothScroll() {
        // Находим элемент для прокрутки
        WebElement element = driver.findElement(By.xpath("//img[@alt='uTest']"));
        WebElement element1 = driver.findElement(By.xpath("//h3[text()='Find Bugs']"));


    
        // Создаем объект Actions
        Actions actions = new Actions(driver);
        
        // Выполняем плавную прокрутку
        actions.moveToElement(element).perform();
        
        // Проверяем, что элемент стал видимым
        assertTrue(element.isDisplayed());
        
        //wait(1000);
        //assertFalse(element1.isDisplayed());
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='uTest']"))); 
        }*/

        


    
    
    /* @Test
    public void clickOnItem(){
        

    WebElement randItem = driver.findElement(By.xpath("//div[@id='ec_product_image_effect_4881370']/a"));
    randItem.click();

    } */



}
