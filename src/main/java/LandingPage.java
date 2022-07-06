import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class LandingPage extends IndexPage {

    private final By experiencesButtonXpath = By.xpath("//*[@class=\"btn-group nav mt-5\"]//*[@href=\"#experience\"]");
    private final By experiencesListXpath = By.xpath("//*[@id=\"experience\"]//h4");

    private final By buttonToBlogXpath = By.xpath("//*[@id=\"blog\"]//*[@class=\"blog-preview__header_button desktop\"]//*");
    private final By buttonToNextPageOnBlogXpath = By.xpath("//*[@aria-label=\"Page navigation\"]//*[@rel=\"next\"]");
    private final By postsDivsOnPageTwo = By.xpath("//*[@class=\"row\"]/*[@class=\"col-lg-4\"]");

    private final By picturesFromBlogSite = By.xpath("//*[@class=\"blog-page__item-thumb\"]//img");

    public LandingPage(WebDriver driver) {
        super(driver);
    }

    public List<String> experiencesListCreator() {
        buttonClicker(experiencesButtonXpath);
        //List<WebElement> experiencesListWE = driver.findElements(experiencesListXpath);
        List<WebElement> experiencesListWE = elementsFinder(experiencesListXpath);
        System.out.println(experiencesListWE);
        List<String> experiencesListString = new LinkedList<>();
        for (int i = 0; i < experiencesListWE.size(); i++) {
            experiencesListString.add(experiencesListWE.get(i).getText());
        }
        return experiencesListString;
    }

    public int numberOfPostsFromSecondPageOfBlog() {
        pushTheButtonToTheBlog();
        pushTheButtonToTheNextPageOfBlog();
        List<WebElement> listOfPostsDivsOnPageTwo = elementsFinder(postsDivsOnPageTwo);
        return listOfPostsDivsOnPageTwo.size();
    }

    public void pushTheButtonToTheBlog(){
        buttonClicker(buttonToBlogXpath);
    }

    public void pushTheButtonToTheNextPageOfBlog(){
        buttonClicker(buttonToNextPageOnBlogXpath);
    }

    public List<WebElement> picturesFromBlogIntoList(){
        return elementsFinder(picturesFromBlogSite);
    }

    public boolean isNext() {
        return driver.findElement(By.xpath("//*[@rel=\"next\"]")).isDisplayed();
    }

    public static void jpgFromWebelementsIntoFolder(String path, List<WebElement> picturesFromBlogList) throws IOException {
        System.out.println(picturesFromBlogList.size());
        for (WebElement picture : picturesFromBlogList) {
            System.out.println(picture);
            String pictureLink = picture.getAttribute("src"); //kiveszi a kép linkjét a Webelementből attributum alapján
            String[] picturePieces = pictureLink.split("/"); //lementi tömbbe a link szétdarabolt részeit, és az utolsót felhasználja az elnevezésre
            String filename = picturePieces[picturePieces.length - 1];

            URL pictureURL = new URL(pictureLink);
            BufferedImage savedImage = ImageIO.read(pictureURL); //read image from url
            ImageIO.write(savedImage, "jpg", new File("files/" + path + "/" + filename));
        }
    }
}