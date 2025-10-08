package utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


public class ReusableMethods {
    //===================Ekran goruntusunu alma =======================//
    //To take screen shot with given name.

    public static String getScreenshot(String name) throws IOException {
        // Ekran goruntusunu given tarix ile adlandirma
    String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //TakeScreeenshoot, ekran goruntusunu almaq ucun Seleniumda bir ara birimdir
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        //Ekran goruntusunun kayd edilecegi tam yol
        String target = System.getProperty("user.dir") + "/target/Screenshots/" + date + ".png";
        File finalDestination = new File(target);
        //Kaynak ekran goruntusunu belirtilen yola kopyala
        FileUtils.copyFile(source, finalDestination);
        return target;
    }

    //=============Pencere deyistirme ====================//
    //Hedef basliga sahib pencereye gecis yapar///

    public static void switchWindow(String targetTittle){
        String origin = Driver.getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(handle);
            if(Driver.getDriver().getTitle().equals(targetTittle)){
                return;
            }

        }
        Driver.getDriver().switchTo().window(origin);
    }

    //================Uzerine gelme Hoover===================//
    //Belirtilen web elementinin uzerine gelir(hover islemi yapar)

    public static void hover(WebElement element){
        Actions actions = new Actions(Driver.getDriver());
        actions.moveToElement(element).perform();
    }

    //===============Web Element listesini String Listesine donusturme===============//
    //Belirtilen web element listesini string listesine donusturur//
    public static List<String> stringListeCevir(List<WebElement> list){
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : list) {
            if(!el.getText().equals("")){
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //============ Bir Elementin Metnini dondurme(By locator ile) ===============//
    //Belirtilen By Locator ile web elementlerinin metinlerini dondurur.
    public static List<String> getElementsText (By locator){
        List<WebElement> elems = new ArrayList<>();
        List<String> elemTexts= new  ArrayList<>();

        for (WebElement el : elems) {
            if(!el.getText().isEmpty()){
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //================Zorlu Bekleme===================//
    //Thread.sleep kullanarak
    //Belirtilen sure kadar bekle

    public static void bekle(int saniye){
        try{
            Thread.sleep(saniye*1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    //=============Belirtilen bir sure boyunca web elementin gorunduyunu yoxlayir(Explicit wait) ==============//
    //Belirtilen sure boyunca belirtilen web elementin gorunur olmasini bekler
    public static WebElement waitForVisibility(WebElement element,int timeout){
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    //=============Belirtilen bir sure boyunca By loator ile web elementin gorunduyunu yoxlayir(Explicit wait) ==============//
    //Belirtilen sure boyunca belirtilen By loator ile web elementin gorunur olmasini bekler
    public static WebElement waitForVisibility(By locator,int timeout){
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //=============Belirtilen bir sure boyunca belirtilen web elementin tiklanabilir olmasini bekler.
    public static WebElement waitForClickability(WebElement element,int timeout){
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    //=============Belirtilen bir sure boyunca belirtilen web elementin By locator ile tiklanabilir olmasini bekler.
    public static WebElement waitForClickability(By locator,int timeout){
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    //=============Belirtilen sure boyunca WebElemente tiklamayi dener=================//
    //eger tiklamaq olmasa hata verir
    public static void clickWithTimeOut(WebElement element,int timeout){
        for (int i = 0;i <timeout;i++){
            try{
                element.click();
                return;
            }catch (WebDriverException e){
                bekle(1);
            }
        }
    }
    //Sayfnin belirli bir surede yuklenmesini bekler
    public static void waitForPageToLoad(long timeout){
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try{
            System.out.println("Waiting for page to load...");
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(expectation);
        }catch (Exception e){
            System.out.println("Waiting for page to load failed");
        }
    }
//=======================Fluent wait========================//
    //Belirlernen sure boyunca webElement olmasini gozleyir

    public static WebElement fluentWait(final WebElement webElement,int timeout){
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver())
                .withTimeout(Duration.ofSeconds(timeout))//her seferinde gosterilen muddet qeder gozleyecek
                .pollingEvery(Duration.ofSeconds(1));//Her 1 saniyyeden bir Elementi yoxlayir

        WebElement element =wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {return webElement;}
        });
        return element;
    }
}
 