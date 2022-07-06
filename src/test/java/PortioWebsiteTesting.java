import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PortioWebsiteTesting {

    WebDriver driver;

    @BeforeEach
    public void Setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("start-maximized");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    /*
    A választott teszt alkalmazásnak legalább az alábbi funkcióit kell lefedni tesztekkel:
             (/) Regisztráció
             (/) Bejelentkezés
             (/) Adatkezelési nyilatkozat használata
             (/) Adatok listázása
             (/) Több oldalas lista bejárása
             (/) Új adat bevitel
             (/) Ismételt és sorozatos adatbevitel adatforrásból
             (/) Meglévő adat módosítás
             (/) Adat vagy adatok törlése
             (/) Adatok lementése felületről
             (-) Kijelentkezés
    */
    @Test //Adatkezelési nyilatkozat használata
    @Epic("Portio website testing")
    @Story("Data privacy statement")
    @Description("Navigate to the website and close the pop-up window. Testing with popup's css.")
    @Severity(SeverityLevel.BLOCKER)
    public void privacyPolicyTest() {
        IndexPage page = PageFactory.pageSwitcher("IndexPage", driver);
        page.navigateToURL(); //fellép az oldalra
        page.closeTheTermsAndConditionsPopUp(); //becsukja a popupot
        boolean actual = page.checkTermsAndConditionValidation(); //megnézi hogy a popup css-e mit ír, az alapján nézem, hogy becsukta-e a popupot

        Assertions.assertFalse(actual);
    }

    @Test //Regisztráció
    @Epic("Portio website testing")
    @Story("Registration")
    @Description("Navigate to the website, close the pop-up window, register with any valid data. Testing with registration division's css.")
    @Severity(SeverityLevel.CRITICAL)
    public void registrationTest() {
        IndexPage page = PageFactory.pageSwitcher("IndexPage", driver);
        page.navigateToURL();
        page.closeTheTermsAndConditionsPopUp();
        page.registrationProcess("David", "pass123", "diriczid@freemail", "something description text");
        boolean actual = page.checkRegistrationValidation();

        Assertions.assertFalse(actual);
    }

    @Test //Bejelentkezés
    @Epic("Portio website testing")
    @Story("Log In")
    @Description("Navigate to the website, close the pop-up window, register with any valid data, and use them for log in. " +
            "Testing with the actual website address.")
    @Severity(SeverityLevel.CRITICAL)
    public void logIn() {
        IndexPage page = PageFactory.pageSwitcher("IndexPage", driver);
        page.navigateToURL();
        page.closeTheTermsAndConditionsPopUp();
        page.registrationProcess("David", "pass123", "diriczid@freemail", "something description text");
        //eddigi volt a regisztráció
        page.logIn("David", "pass123");
        String resultURL = page.getCurrentURL();

        Assertions.assertEquals("https://lennertamas.github.io/portio/landing.html", resultURL);
    }

    @Test //Adatok listázása
    @Epic("Portio website testing")
    @Story("Webelements saveing into file and list")
    @Description("Log in the website, navigate to the experiences and load the names of workplaces into a List. " +
            "Testing this List with a fix List.")
    @Severity(SeverityLevel.NORMAL)
    public void experiencesList() {
        LandingPage page = (LandingPage) PageFactory.pageSwitcher("LandingPage", driver);
        page.toTheWebsite();
        List<String> experiencesListFromPage = page.experiencesListCreator();
        List<String> listForTest = MethodsForTests.fileReader("files/experiences.txt");

        Assertions.assertEquals(listForTest, experiencesListFromPage);
    }

    @Test //Több oldalas lista bejárása
    @Epic("Portio website testing")
    @Story("PagingOnTheBlog")
    @Description("Log in the website, navigate to the blog, paging to the 2. page and look how many post is here. " +
            "Testing with count the divs of posts")
    @Severity(SeverityLevel.NORMAL)
    public void blogPageTest() {
        LandingPage page = (LandingPage) PageFactory.pageSwitcher("LandingPage", driver);
        page.toTheWebsite();
        int postsNumbersFromSecondPageOfBlog = page.numberOfPostsFromSecondPageOfBlog();
        int inspectedPostsNumber = 3;

        Assertions.assertEquals(inspectedPostsNumber, postsNumbersFromSecondPageOfBlog);
    }

    @Test //Új adat bevitel és Meglévő adat módosítás
    @Epic("Portio website testing")
    @Story("Data input and modifying")
    @Description("Registration with valid data, log in to the website, go to Profile menu and modifying the registered" +
            " account with other valid data. I did screenshots from this steps for Allure report. Testing whether the " +
            "registered name data is found in the cookie.")
    @Severity(SeverityLevel.CRITICAL)
    public void cookieTest() {
        ProfilePage page = (ProfilePage) PageFactory.pageSwitcher("ProfilePage", driver);
        page.navigateToURL();
        page.closeTheTermsAndConditionsPopUp();
        page.registrationProcess("David", "pass123", "diriczid@freemail", "something description text");
        page.logIn("David", "pass123");
        page.clickOnProfileButton();
        Allure.addAttachment("profileScreenShot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        page.profileModifier("DefiantlyNotDavid", "Something bio description text", "06123456789");
        Allure.addAttachment("profileFilledScreenShot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        page.clickOnSaveButton();
        Allure.addAttachment("profileSavedScreenShot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        boolean cookieTestData = page.cookieTestData("David", "DefiantlyNotDavid", "Something bio description text", "06123456789");

        Assertions.assertTrue(cookieTestData); //a profilnév megegyezik, de már nem ugyanazok az adatok szerepelnek, így az átírás sikerült
    }

    @Test //Ismételt és sorozatos adatbevitel adatforrásból
    @Epic("Portio website testing")
    @Story("Multiple data input from file")
    @Description("I specify how many accounts I want to register, I create a list. I put this list into a file. " +
            "From this file I register 50 times with the data. After each registration I test if the registration was successful. " +
            "At the end of the test, I check if 50 cookies were actually created on the site.")
    @Severity(SeverityLevel.CRITICAL)
    public void registrationFromFileTest() {
        int numberOfTestDataRow = 10;
        String accountHandlerListPath = "files/multivaluedMapForAccountHandlingInFile.csv";

        ProfilePage page = (ProfilePage) PageFactory.pageSwitcher("ProfilePage", driver);
        page.navigateToURL();
        page.closeTheTermsAndConditionsPopUp();
        List<List<String>> generatedData = MethodsForTests.multivaluedMapWriterForAccountHandling(numberOfTestDataRow);
        MethodsForTests.deleteFile(accountHandlerListPath);
        MethodsForTests.MapToFiles(accountHandlerListPath, generatedData);
        List<List<String>> listForAccountHandling = MethodsForTests.fromFileToStringList(accountHandlerListPath);
        for (int i = 0; i < listForAccountHandling.size(); i++) {
            page.registrationProcess(
                    listForAccountHandling.get(i).get(0),
                    listForAccountHandling.get(i).get(1),
                    listForAccountHandling.get(i).get(2),
                    listForAccountHandling.get(i).get(3)
            );
            String actual = page.showRegistrationMessage();
            Assertions.assertEquals("User registered!", actual); //mindegyikre megnézem, hogy sikerült-e regisztrálni
            page.registrationFieldsClearer();
        }
        int cookiesCounter = page.cookiesCounter();

        Assertions.assertEquals(numberOfTestDataRow, cookiesCounter); //megnézem hogy a regisztrált accountok száma megegyezik-e a cookiek számával
    }

    @Test //Adatok lementése felületről
    @Epic("Portio website testing")
    @Story("Data saving from website into file")
    @Description("I navigated to the blog and downloaded the images from both pages into a folder I created. " +
            "There can be 6 images per page, I test this to see if a total of more than 6 but less than 13 images have " +
            "been downloaded from both pages.")
    @Severity(SeverityLevel.MINOR)
    public void downloadPicturesFromBlogPage() throws IOException {
        String picturesFromBlog = "picturesFromBlog";

        LandingPage page = (LandingPage) PageFactory.pageSwitcher("LandingPage", driver);
        page.toTheWebsite();
        MethodsForTests.folderCreator(picturesFromBlog);
        page.pushTheButtonToTheBlog();
        List<WebElement> picturesFromBlogList = page.picturesFromBlogIntoList();
        page.jpgFromWebelementsIntoFolder(picturesFromBlog, picturesFromBlogList);
        page.pushTheButtonToTheNextPageOfBlog();
        picturesFromBlogList = page.picturesFromBlogIntoList();
        page.jpgFromWebelementsIntoFolder(picturesFromBlog, picturesFromBlogList);

        List<String> filesNames = MethodsForTests.filesNamesFromFolderIntoList(picturesFromBlog);
        boolean result = (filesNames.size() > 6 && filesNames.size() < 13); //Egy oldalon 6 kép van és összesen 2 oldal van

        Assertions.assertTrue(result);
    }

    @Test //Adat vagy adatok törlése
    @Epic("Portio website testing")
    @Story("Data deleting")
    @Description("I have registered five accounts and deleted one, and I will test to see if there are less than " +
            "5 cookies at the end of the test.")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteAccountTest() {
        int numberOfTestDataRow = 5;
        String accountHandlerListPath = "files/multivaluedMapForAccountHandlingInFile.csv";

        ProfilePage page = (ProfilePage) PageFactory.pageSwitcher("ProfilePage", driver);
        page.navigateToURL();
        page.closeTheTermsAndConditionsPopUp();
        List<List<String>> generatedData = MethodsForTests.multivaluedMapWriterForAccountHandling(numberOfTestDataRow);
        MethodsForTests.deleteFile(accountHandlerListPath);
        MethodsForTests.MapToFiles(accountHandlerListPath, generatedData);
        List<List<String>> listForAccountHandling = MethodsForTests.fromFileToStringList(accountHandlerListPath);
        for (int i = 0; i < listForAccountHandling.size(); i++) {
            page.registrationProcess(
                    listForAccountHandling.get(i).get(0),
                    listForAccountHandling.get(i).get(1),
                    listForAccountHandling.get(i).get(2),
                    listForAccountHandling.get(i).get(3)
            );
            String actual = page.showRegistrationMessage();
            Assertions.assertEquals("User registered!", actual); //mindegyikre megnézem, hogy sikerült-e regisztrálni
            page.registrationFieldsClearer();
        }
        page.logIn(listForAccountHandling.get(0).get(0), listForAccountHandling.get(0).get(1));
        page.clickOnProfileButton();
        Assertions.assertEquals(numberOfTestDataRow, page.cookiesCounter());
        page.deleteAccount();

        Assertions.assertNotEquals(numberOfTestDataRow, page.cookiesCounter()); //ha nem ugyanaz, akkor lett törölve account
    }

    @Test
    @Epic("Portio website testing")
    @Story("Log Out")
    @Description("I registered, logged in, logged out and checked that the body of the website was \"openPage\". " +
            "I did Allure screenshots from this steps.")
    @Severity(SeverityLevel.MINOR)
    public void LogoutTest() {
        LandingPage page = (LandingPage) PageFactory.pageSwitcher("LandingPage", driver);
        page.toTheWebsite();
        Allure.addAttachment("loggedIn", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        page.clickOnLogout();
        Allure.addAttachment("loggedOut", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        Assertions.assertTrue(page.isLoggedOut());
    }

    @AfterEach
    public void testShutDown() {
        driver.close();
        driver.quit();
    }
}