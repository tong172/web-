import com.tong.service.user.impl.UserServiceImpl;
import org.junit.Test;

public class MyTest2 {
    @Test
    public void getUserCountTest(){
        UserServiceImpl service = new UserServiceImpl();
        int count = service.getUserCount("孙", 3);
        System.out.println(count);
    }
}
