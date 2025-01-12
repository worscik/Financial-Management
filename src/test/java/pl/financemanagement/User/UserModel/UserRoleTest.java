package pl.financemanagement.User.UserModel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserRoleTest {

    @Test
    void getRole() {
        assertThat(UserRole.USER.getRole().equals("USER"));
        assertThat(UserRole.ADMIN.getRole().equals("ADMIN"));
        assertThat(UserRole.DEMO.getRole().equals("DEMO"));
    }
}