import com.tong.pojo.Role;
import com.tong.service.Role.impl.RoleServiceImpl;
import org.junit.Test;

import java.util.List;

public class MyTest3 {
    @Test
    public void test(){
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        for (Role role : roleList) {
            System.out.println(role.getRoleName());
        }
    }
}
