package UseCaseTests;
import org.junit.*;
import static org.junit.Assert.*;
import entity.*;
import usecase.*;

public class UserManagerTests {

    @Test(timeout = 50)
    public void testaddUser() {
        UserManager UM = new UserManager();
        UserData UD = new AttendeeData("abcd1234", "Supanat Wangsuttham", "ABCD1234");
        assertTrue("Should be true if it works", UM.addUser(UD));
        System.out.println(UD.getUserType());
    }
}
