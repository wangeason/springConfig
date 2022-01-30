import com.google.gson.Gson;
import com.reg.RegisterApplication;
import com.reg.RegisterReturn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = com.reg.RegisterApplication.class,
        properties = {
                "spring.cloud.config.uri=http://localhost:8888",
                "spring.config.import=optional:configserver:",
                "ipQuery=http://ip-api.com/json",
                "validChecking=http://localhost:9998/validate?pwd={pwd}"
        })
public class RegisterApplicationTest {
    @Autowired
    private RegisterApplication application;

    @Test
    public void context() throws Exception {
        assertThat(application).isNotNull();
    }

    @Test
    public void emptyInput() throws Exception {
        assertThat(application.userRegister("", "123", "214.48.0.87")).isEqualTo(new RegisterReturn("input is invalid").toJson());
        assertThat(application.userRegister("werwer", "", "214.48.0.87")).isEqualTo(new RegisterReturn("input is invalid").toJson());
        assertThat(application.userRegister("werwer", "123", "")).isEqualTo(new RegisterReturn("input is invalid").toJson());
    }

    @Test
    public void pwdInvalidTest() throws Exception {
        assertThat(application.userRegister("werwer", "123", "214.48.0.87")).isEqualTo(new RegisterReturn("pwd is invalid").toJson());
        assertThat(application.userRegister("werwer", "123asdfaE", "214.48.0.87")).isEqualTo(new RegisterReturn("pwd is invalid").toJson());
        assertThat(application.userRegister("werwer", "123$$$asdf", "214.48.0.87")).isEqualTo(new RegisterReturn("pwd is invalid").toJson());
    }

    @Test
    public void ipInvalidTest() throws Exception {
        assertThat(application.userRegister("werwer", "123asdfaE$", "214.48.0.87")).isEqualTo(new RegisterReturn("user is not eligible to register").toJson());
    }

    @Test
    public void regSuccessTest() throws Exception {

        Gson gson = new Gson();
        assertThat(gson.fromJson(application.userRegister("werwer", "123asdfaE$", "24.48.0.1"), RegisterReturn.class))
                .isEqualTo(new RegisterReturn("werwer", "Canada"));
    }
}
