package store.mybooks.resource.user_grade.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;
import store.mybooks.resource.user_grade.entity.UserGrade;

/**
 * packageName    : store.mybooks.resource.user.repository<br>
 * fileName       : UserGradeRepository<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
public interface UserGradeRepository extends JpaRepository<UserGrade, Integer> {


    Optional<UserGrade> findByUserGradeNameIdAndIsAvailableIsTrue(String userGradeName);

    List<UserGradeGetResponse> queryAllByIsAvailableIsTrueOrderByMinCost();

    List<UserGradeGetResponse> queryAllByOrderByMinCost();

}
