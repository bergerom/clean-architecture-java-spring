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
    Karate createUser() {
        return Karate.run("classpath:karate/create-user.feature");
    }


    @Karate.Test
    Karate getUsers() {
        return Karate.run("classpath:karate/get-users.feature");
    }
}
