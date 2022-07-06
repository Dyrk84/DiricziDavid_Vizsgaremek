import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProfilePage extends IndexPage {
    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    private final By profileButton = By.id("profile-btn");
    private final By nameInput = By.id("name");
    private final By bioInput = By.id("bio");
    private final By phoneInput = By.id("phone-number");
    private final By saveButton = By.xpath("//*[@onclick='editUser()']");

    private final By deleteAccountButton = By.xpath("//*[@onclick='showRealDeleteAccBtn()']");
    private final By sureDeleteAccountButton = By.id("delete-account-btn");

    public void clickOnProfileButton() {
        buttonClicker(profileButton);
    }

    public void profileModifier(String name, String bio, String phone) {
        inputFieldLoader(nameInput, name);
        inputFieldLoader(bioInput, bio);
        inputFieldLoader(phoneInput, phone);
    }

    public void clickOnSaveButton() {
        buttonClicker(saveButton);
    }

    public boolean cookieTestData(String profileName, String name, String bio, String phone) {
        String cookieString = driver.manage().getCookieNamed(profileName).getValue(); //kiszedi Stringbe annak a cookienak az adatait, aminek a neve a profileName paraméter

        return cookieString.contains(name) && cookieString.contains(bio) && cookieString.contains(phone);
    }

    public void deleteAccount(){
        buttonClicker(deleteAccountButton);
        buttonClicker(sureDeleteAccountButton);
    }
}