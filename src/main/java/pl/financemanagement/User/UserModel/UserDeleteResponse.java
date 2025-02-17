package pl.financemanagement.User.UserModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDeleteResponse {

    private boolean success;
    private String message;

}
