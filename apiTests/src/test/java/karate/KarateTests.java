package karate;


import app.SpringFrameworkLauncher;
import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KarateTests {

    @BeforeAll
    public void setUpApp() throws InterruptedException {
        new SpringFrameworkLauncher().launch();
    }

    @Karate.Test
    Karate dummyTest() {
        return Karate.run("classpath:karate/dummy.feature");
    }

    @Karate.Test
    Karate createUserAndAddScore() throws InterruptedException {
        return Karate.run("classpath:karate/createUserAndAddScore.feature");
    }
}
