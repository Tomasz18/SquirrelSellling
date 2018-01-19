package spoonarchsystems.squirrelselling.ComplaintMaking;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import spoonarchsystems.squirrelselling.Model.Entity.Complaint;
import spoonarchsystems.squirrelselling.Model.Entity.ComplaintPosition;
import spoonarchsystems.squirrelselling.SquirrelsellingApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

@WebAppConfiguration
@ContextConfiguration(classes = SquirrelsellingApplication.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ComplaintMaking {

    @Autowired
    private SessionFactory sessionFactory;

    private WebDriver driver;
    private Session session;

    private void openSession() {
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
    }

    private void closeSession() {
        session.close();
    }

    private String getToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    private void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();

        openSession();
        // Dane testowe
        session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        session.createSQLQuery("TRUNCATE TABLE pozycjezamowien").executeUpdate();
        session.createSQLQuery("TRUNCATE TABLE zamowienia").executeUpdate();
        session.createSQLQuery("TRUNCATE TABLE towary").executeUpdate();
        session.createSQLQuery("TRUNCATE TABLE reklamacje").executeUpdate();
        session.createSQLQuery("TRUNCATE TABLE pozycjereklamacji").executeUpdate();
        session.createSQLQuery("INSERT INTO zamowienia " +
                "(id, data_odbioru, reklamacyjne, data_skompletowania, koszt_dostawy, czas_dostawy, faktura, numer, odbior_osobisty, czas_odroczenia, czas_realizacji, data_sprzedazy, status, data_zlozenia, id_klienta, adres_dostawy, adres_nabywcy) " +
                "VALUES (1, '2017-12-02', 0, '2017-12-01', 0, NULL, 0, '0001/30-11-2017', 1, 0, NULL, '2017-12-02', 'realized', '2017-11-30', 1, NULL, NULL);").executeUpdate();
        session.createSQLQuery("INSERT INTO pozycjezamowien (numer, cena, ilosc, id_towaru, id_zamowienia) VALUES (1, 319.00, 5, 1, 1);").executeUpdate();
        session.createSQLQuery("INSERT INTO pozycjezamowien (numer, cena, ilosc, id_towaru, id_zamowienia) VALUES (2, 352.99, 2, 2, 1);").executeUpdate();
        session.createSQLQuery("INSERT INTO pozycjezamowien (numer, cena, ilosc, id_towaru, id_zamowienia) VALUES (3, 1119.25, 3, 3, 1);").executeUpdate();

        session.createSQLQuery("INSERT INTO towary (id, kod, opis, stan_dyspozycyjny, nazwa, cena_detaliczna, jednostka, waga, cena_hurtowa, prog_hurtowy, nazwa_kategori) " +
                "VALUES (1, 'BOCH45398', 'Udarowa 600W, moment obrotowy 10,8 Nm, gwarancja 3 lata', 200, 'Wiertarka udarowa BOCH 5600', 319.00, 'szt', 0.91, 280.00, 20, 'Wiertarki przewodowe');").executeUpdate();
        session.createSQLQuery("INSERT INTO towary (id, kod, opis, stan_dyspozycyjny, nazwa, cena_detaliczna, jednostka, waga, cena_hurtowa, prog_hurtowy, nazwa_kategori) " +
                "VALUES (2, 'MKTIA98425', 'Udar, obroty prawo-lewo, walizka transportowa w zestawie', 50, 'Wiertarka Makika HK324', 352.99, 'szt', 2.0, 341.89, 22, 'Wiertarki przewodowe');").executeUpdate();
        session.createSQLQuery("INSERT INTO towary (id, kod, opis, stan_dyspozycyjny, nazwa, cena_detaliczna, jednostka, waga, cena_hurtowa, prog_hurtowy, nazwa_kategori) " +
                "VALUES (3, 'SMSNG00658', 'LED 52\", 3x HDMI', 120, 'Telewizor SAMSUNG 52\"', 1199.25, 'szt', 8.2, 1000.00, 10, 'Telewizory');").executeUpdate();
        session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        closeSession();
    }

    @Test //RP01
    public void makeComplainSuccess() {
        setup();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String confirmationViewTitle = "Potwierdzenie złożenia reklamacji";
        String description = "Nie uruchamia się";

        // 1. Przejdź do widoku szczegółów zrealizowanego zamówienia
        driver.get("localhost:8080/order/1");

        // 2. Kliknij przycisk Złóż reklamację
        WebElement hiddenId = driver.findElement(By.name("id"));
        hiddenId.submit();

        // 3. Odznacz pozycje 1 oraz 2
        WebElement firstPositionCheckbox = driver.findElement(By.name("positions[0].number"));
        WebElement secondPositionCheckbox = driver.findElement(By.name("positions[1].number"));
        firstPositionCheckbox.click();
        secondPositionCheckbox.click();

        // 4. Zmień ilość w pozycji 3 na 1,0
        WebElement thirdPositionQuantity = driver.findElement(By.name("positions[2].quantity"));
        thirdPositionQuantity.clear();
        thirdPositionQuantity.sendKeys("1,0");

        // 5. Kliknij przycisk Dodaj do reklamacji
        thirdPositionQuantity.submit();

        // 6. Wpisz opis „Nie uruchamia się”
        WebElement descriptionTextArea = driver.findElement(By.name("description"));
        descriptionTextArea.sendKeys(description);

        // 7. Kliknij przycisk Złóż reklamację
        descriptionTextArea.submit();

        String pageTitle = driver.getTitle();

        driver.quit();

        openSession();
        Complaint complaint =  session.get(Complaint.class, new Integer(1));
        closeSession();

        // 1. Powinien zostać wyświetlony widok potwierdzenia złożenia reklamacji
        assertEquals(confirmationViewTitle, pageTitle);

        // 2. W bazie danych powinna pojawić się nowa reklamacja:
        assertEquals(1, complaint.getId());
        assertEquals(description, complaint.getDescription());
        assertEquals("0001/" + getToday(), complaint.getNumber());
        assertEquals(Complaint.ComplaintStatus.submitted, complaint.getStatus());
        assertEquals(getToday(), dateFormat.format(complaint.getSubmissionDate()));
        assertEquals(1, complaint.getOrder().getId());

        // 3. W bazie danych powinna pojawić się nowa pozycja reklamacji:
        ComplaintPosition complaintPosition = complaint.getPositions().get(0);
        assertEquals(new Integer(1), complaintPosition.getNumber());
        assertEquals(new Double(1), complaintPosition.getQuantity());
        assertEquals(1, complaintPosition.getComplaint().getId());
        assertEquals(3, complaintPosition.getWare().getId());
    }

    @Test //RN01
    public void makeComplainWithoutPositions() {
        setup();
        String errorMessage = "Nieprawidłowa ilość towarów (0 lub przekraczająca ilość towarów w zamówieniu) lub nie wybrano żadnego towaru.";

        // 1. Przejdź do widoku szczegółów zrealizowanego zamówienia
        driver.get("localhost:8080/order/1");

        // 2. Kliknij przycisk Złóż reklamację
        WebElement hiddenId = driver.findElement(By.name("id"));
        hiddenId.submit();

        // 3. Odznacz wszystkie pozycje
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
        for (WebElement checkbox: checkboxes) {
            checkbox.click();
        }

        // 4. Kliknij przycisk Złóż reklamację
        WebElement hiddenOrderId = driver.findElement(By.name("order.id"));
        hiddenOrderId.submit();

        List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + errorMessage + "')]"));
        driver.quit();

        assertEquals(1, list.size());
    }

    @Test //RN02
    public void makeComplainWithZeroQuantity() {
        setup();
        String errorMessage = "Nieprawidłowa ilość towarów (0 lub przekraczająca ilość towarów w zamówieniu) lub nie wybrano żadnego towaru.";

        // 1. Przejdź do widoku szczegółów zrealizowanego zamówienia
        driver.get("localhost:8080/order/1");

        // 2. Kliknij przycisk Złóż reklamację
        WebElement hiddenId = driver.findElement(By.name("id"));
        hiddenId.submit();

        // 3. Zmień ilość w pozycji 1 na 0,0
        WebElement firstPositionQuantity = driver.findElement(By.name("positions[0].quantity"));
        firstPositionQuantity.clear();
        firstPositionQuantity.sendKeys("0");

        // 4. Kliknij przycisk Złóż reklamację
        firstPositionQuantity.submit();

        List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + errorMessage + "')]"));
        driver.quit();

        assertEquals(1, list.size());
    }

    @Test //RN03
    public void makeComplainWithEmptyDescription() {
        setup();
        String errorMessage = "Opis usterek nie może być pusty";

        // 1. Przejdź do widoku szczegółów zrealizowanego zamówienia
        driver.get("localhost:8080/order/1");

        // 2. Kliknij przycisk Złóż reklamację
        WebElement hiddenId = driver.findElement(By.name("id"));
        hiddenId.submit();

        // 3. Kliknij przycisk Dodaj do reklamacji
        WebElement hiddenOrderId = driver.findElement(By.name("order.id"));
        hiddenOrderId.submit();

        // 4. Kliknij przycisk Złóż reklamację
        WebElement descriptionTextArea = driver.findElement(By.name("description"));
        descriptionTextArea.submit();

        List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + errorMessage + "')]"));
        driver.quit();

        assertEquals(1, list.size());
    }
}
