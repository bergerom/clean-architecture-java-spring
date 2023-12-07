import containers.CreateUserContainer;
import ports.CreateUser;
import usecases.UseCaseInteractor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class SpringAPI {

    private final UseCaseInteractor useCaseInteractor;

    public SpringAPI(UseCaseInteractor useCaseInteractor) {
        this.useCaseInteractor = useCaseInteractor;
    }

    @PostMapping
    public String createUser(@RequestBody CreateUserContainer createUser) throws IOException {
        CreateUser.CreateUserRequest createUserRequest
                = new CreateUser.CreateUserRequest(createUser.userName(), createUser.age());
        return useCaseInteractor.createUser(createUserRequest).id();
    }
}
