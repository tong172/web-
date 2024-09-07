import com.tong.pojo.User;
import com.tong.service.user.impl.UserServiceImpl;
import org.junit.Test;

public class MyTest {
    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        User admin = userService.login("admin", "123");
        System.out.println(admin.getUserPassword());
    }
}
