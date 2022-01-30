import com.validate.ValidateApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = com.validate.ValidateApplication.class,
        properties = {
                "spring.cloud.config.uri=http://localhost:8888",
                "spring.config.import=optional:configserver:",
                "allowedSpecial=_#$%."
        })
public class ValidateApplicationTest {

    @Autowired
    private ValidateApplication application;

    @Test
    public void validation() throws Exception {
        assertThat(application).isNotNull();
        assertThat(application.validate("123YUUY$a")).isEqualTo(true);
        assertThat(application.validate("123")).isEqualTo(false);
        assertThat(application.validate("123YUssdfa")).isEqualTo(false);
        assertThat(application.validate("123$$$$$$$")).isEqualTo(false);
        assertThat(application.validate("JHJas$$$$$$$")).isEqualTo(false);
        assertThat(application.validate("////////////")).isEqualTo(false);
        assertThat(application.validate("123YUUY$a/")).isEqualTo(false);
    }
}
