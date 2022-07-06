import org.openqa.selenium.WebDriver;

public class PageFactory {

    public static IndexPage pageSwitcher(String pageName, WebDriver driver){ //IndexPage a visszatérési típus, mert a főosztályból örökli az összes többi a driverjét
        return switch (pageName) {
            case "IndexPage" -> new IndexPage(driver);
            case "LandingPage" -> new LandingPage(driver);
            case "ProfilePage" -> new ProfilePage(driver);
            default -> null;
        };
    }
}