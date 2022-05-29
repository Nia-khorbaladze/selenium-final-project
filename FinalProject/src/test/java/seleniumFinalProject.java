import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import org.openqa.selenium.WebElement;


import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class seleniumFinalProject {
    WebDriver driver;

    @BeforeTest
    @Parameters("browser")
    public void setup(String browser) throws Exception {
        if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }
        else if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else {
            throw new Exception("Browser is not correct");
        }
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("http://tutorialsninja.com/demo/");
        driver.manage().window().maximize();
    }

//    @BeforeMethod
//    public void browserSetup() {
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.get("http://tutorialsninja.com/demo/");
//        driver.manage().window().maximize();
//
//    }

    @Test
    public void shoppingTest(){
        driver.findElement(By.cssSelector("a[title*='My Account']")).click();
        driver.findElement(By.linkText("Register")).click();

        //Name input
        WebElement nameInput = driver.findElement(By.cssSelector("input#input-firstname"));
        nameInput.sendKeys("user");

        //Last name input
        WebElement lastnameInput = driver.findElement(By.id("input-lastname"));
        lastnameInput.sendKeys("lastname");

        //Email input
        String emailChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder emailrand = new StringBuilder();
        Random rnd = new Random();
        while (emailrand.length() < 10) {
            int index = (int) (rnd.nextFloat() * emailChars.length());
            emailrand.append(emailChars.charAt(index));
        }
        String randomizedEmail = emailrand.toString();

        WebElement emailInput = driver.findElement(By.xpath("//*[@id=\"input-email\"]"));
        emailInput.sendKeys(randomizedEmail + "@gmail.com");

        //Phone number input
        WebElement telephoneInput = driver.findElement(By.xpath("//*[@id=\"input-telephone\"]"));
        Random rndNumber = new Random();
        int value = rndNumber.nextInt(100000000);
        telephoneInput.sendKeys("5"+value);

        //Password input
        WebElement passwordInput = driver.findElement(By.cssSelector("input[name='password']"));
        passwordInput.sendKeys("password123");

        WebElement passwordConfirm = driver.findElement(By.cssSelector("input[name='confirm']"));
        passwordConfirm.sendKeys("password123");

        //Subscribe radio buttons
        List<WebElement> RadioButtons = driver.findElements(By.name("newsletter"));
        int Size = RadioButtons.size();
        for(int i=0; i < Size; i++)
        {
            String buttonValue = RadioButtons.get(i).getAttribute("value");
            if (buttonValue.equalsIgnoreCase("1"))
            {
                RadioButtons.get(i).click();
                break;
            }
        }

        //click continue button
        driver.findElement(By.cssSelector("input[name='agree']")).click();
        driver.findElement(By.cssSelector("input[value='Continue']")).click();

        WebElement button = driver.findElement(By.xpath("//*[contains(text(),'Continue')]"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", button);

        //Move to Phones & PDAs and check text on mouse hover
        driver.findElement(By.linkText("Phones & PDAs")).click();

        WebElement palmtreoPhone = driver.findElement(By.cssSelector("img[title='Palm Treo Pro']"));
        String expectedTooltip = "Palm Treo Pro";
        String actualTooltip = palmtreoPhone.getAttribute("title");

        if (actualTooltip.equals(expectedTooltip)) {
            System.out.println("Text on mouse hover: " + actualTooltip);
        }


        //Move to the "Palm Treo Pro" link and open images
        palmtreoPhone.click();
        driver.findElement(By.cssSelector("a[title='Palm Treo Pro']")).click();

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[title='Next (Right arrow key)']")));

        //Move to another images
        WebElement nextButton = driver.findElement(By.cssSelector("button[title^='Next']"));
        String expectedCounter = "3 of 3";

        while (!(driver.findElement(By.xpath("//*[@class='mfp-counter']")).getAttribute("textContent")).equals(expectedCounter)) {
            nextButton.click();
        }

        driver.findElement(By.cssSelector("button[title='Close (Esc)']")).click();
//        driver.findElement(By.className("mfp-close")).click();
//        driver.findElement(By.xpath("//button[contains(text(), '×')]")).click();

        //Write a review
        driver.findElement(By.xpath("//*[contains(text(),'Reviews')]")).click();
        js.executeScript("document.getElementById('input-name').value='user123';");

        driver.findElement(By.id("input-review")).sendKeys("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ");

        List<WebElement> reviewRadio = driver.findElements(By.name("rating"));
        int RadioSize = reviewRadio.size();
        for(int i=0; i < RadioSize; i++)
        {
            String buttonValue = reviewRadio.get(i).getAttribute("value");
            if (buttonValue.equalsIgnoreCase("5"))
            {
                reviewRadio.get(i).click();
                break;
            }
        }

        driver.findElement(By.id("button-review")).click();

                                                   //add items to cart

        //get price and count before items are added
        String text = js.executeScript("return document.getElementById('cart-total').innerHTML").toString();
        String[] stringParts = text.split("\\s", 4);
        String partA1 = stringParts[0];
        String partB1 = stringParts[3];

        partB1 = partB1.replaceAll("\\$", "");
        double doublePriceB1 = Double.parseDouble(partB1);
        double doubleCountA1 = Double.parseDouble(partA1);

        //add phone
        driver.findElement(By.cssSelector("button#button-cart")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Success: You have added ')]")));

        //get phone price
        String phonePriceCheck = driver.findElement(By.xpath("//h2[text()[contains(.,'$')]]")).getText();
        phonePriceCheck = phonePriceCheck.replaceAll("\\$", "");
        String phonePrice = phonePriceCheck.replaceAll(",", "");
        double doublePhonePrice = Double.parseDouble(phonePrice);

        //get price and count after the phone was added
        String textAfterPhone = driver.findElement(By.id("cart-total")).getAttribute("innerHTML");
        String[] stringPartsAfterPhone = textAfterPhone.split("\\s", 9);
        String partA2 = stringPartsAfterPhone[3];
        String partB2 = stringPartsAfterPhone[6];
        partB2 = partB2.replaceAll("\\$", "");
        double doublePriceB2 = Double.parseDouble(partB2);
        double doubleCountA2 = Double.parseDouble(partA2);

        //check price and count
        if((doubleCountA1 + 1) == doubleCountA2) {
            System.out.println("Palm Treo Pro added successfully");
        }
        if((doublePriceB1 + doublePhonePrice) == doublePriceB2) {
            System.out.println("Palm Treo Pro added successfully");
        }

        //add macbook air
        driver.findElement(By.xpath("//*[contains(text(),'Laptops & Notebooks')]")).click();
        driver.findElement(By.linkText("Show All Laptops & Notebooks")).click();
        driver.findElement(By.linkText("MacBook Air")).click();
        driver.findElement(By.cssSelector("button#button-cart")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Success: You have added ')]")));

        //get macbook price
        String macbookPriceCheck = driver.findElement(By.xpath("//h2[text()[contains(.,'$')]]")).getText();
        macbookPriceCheck = macbookPriceCheck.replaceAll("\\$", "");
        String laptopPrice = macbookPriceCheck.replaceAll(",", "");
        double doubleLaptopPrice = Double.parseDouble(laptopPrice);

        //get price and count after the macbook was added
        String textAfterMacbook = driver.findElement(By.id("cart-total")).getAttribute("innerHTML");
        String[] stringPartsAfterLaptop = textAfterMacbook.split("\\s", 9);
        String partA3 = stringPartsAfterLaptop[3];
        String partB3 = stringPartsAfterLaptop[6];
        partB3 = partB3.replaceAll("\\$", "");
        String partB3Replaced = partB3.replaceAll(",", "");
        double doublePriceB3 = Double.parseDouble(partB3Replaced);
        double doubleCountA3 = Double.parseDouble(partA3);

        //check final price and count
        if ((doubleCountA2 + 1) == doubleCountA3) {
            System.out.println("MacBook added successfully");
        }
        if ((doublePriceB2 + doubleLaptopPrice) == doublePriceB3) {
            System.out.println("MacBook added successfully");
        }

        driver.findElement(By.id("cart-total")).click();
        driver.findElement(By.linkText("Checkout")).click();

        //remove phone
        driver.findElement(By.xpath("//table[@class = 'table table-bordered']//td[text() = 'Product 2']/following-sibling::td/div/span/button[2]")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(text(),'Products marked with ***')]")));

        //check that item was removed
        List<WebElement> rows = driver.findElements(By.xpath("//table[@class = 'table table-striped']/tbody/tr"));
        if(rows.size()==doubleCountA3-1) {
            System.out.println("item removed successfully");
        }

        //go to checkout and fill information
        driver.findElement(By.xpath("//a[contains(text(),'Checkout')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-payment-firstname")));
        driver.findElement(By.id("input-payment-firstname")).sendKeys("user123");
        driver.findElement(By.id("input-payment-lastname")).sendKeys("lastname");
        driver.findElement(By.id("input-payment-address-1")).sendKeys("1 Merab Kostava St");
        driver.findElement(By.id("input-payment-city")).sendKeys("Tbilisi");
        driver.findElement(By.id("input-payment-postcode")).sendKeys("0108");

        WebElement countrySelect = driver.findElement(By.name("country_id"));
        Select country = new Select(countrySelect);
        country.selectByVisibleText("Georgia");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#input-payment-zone option[value='1244']")));

        WebElement regionSelect = driver.findElement(By.name("zone_id"));
        Select region = new Select(regionSelect);
        region.selectByVisibleText("Tbilisi");

        driver.findElement(By.id("button-payment-address")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("button-shipping-address")));
        driver.findElement(By.id("button-shipping-address")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("button-shipping-method")));
        driver.findElement(By.id("button-shipping-method")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='agree']")));
        driver.findElement(By.cssSelector("input[name='agree']")).click();
        driver.findElement(By.id("button-payment-method")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("button-confirm")));
        driver.findElement(By.id("button-confirm")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Continue")));
        driver.findElement(By.linkText("Continue")).click();

    }
    @AfterClass
    public void terminateBrowser() {
        driver.close();
    }
}
