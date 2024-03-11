import com.heima.common.redis.CacheService;
import com.heima.user.UserApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    CacheService cacheService;

    @Test
    public void test(){
        return;
    }
}
