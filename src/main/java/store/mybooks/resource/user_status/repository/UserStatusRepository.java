package store.mybooks.resource.user_status.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
import store.mybooks.resource.user_status.entity.UserStatus;

/**
 * packageName    : store.mybooks.resource.user.repository<br>
 * fileName       : UserStatusRepository<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
public interface UserStatusRepository extends JpaRepository<UserStatus, String> {

    UserStatusGetResponse queryById(String id);

    List<UserStatusGetResponse> queryAllBy();

}
