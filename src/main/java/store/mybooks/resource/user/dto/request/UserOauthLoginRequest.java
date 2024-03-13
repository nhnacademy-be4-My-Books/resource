package store.mybooks.resource.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user.dto.request<br>
 * fileName       : UserOauthLoginRequest<br>
 * author         : masiljangajji<br>
 * date           : 3/6/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/6/24        masiljangajji       최초 생성
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserOauthLoginRequest {

    @NotNull
    @NotBlank
    @Email
    private String email;

}
