import com.spring.boot.base.Application;
import org.assertj.core.api.WithAssertions;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.junit.Before;

/**
 * @author : wh
 * @date : 2023/12/7 15:14
 * @description: 单元测试继承该类即可
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@EnableWebMvc
public class SpringbootTestBase implements WithAssertions {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
    }
}

