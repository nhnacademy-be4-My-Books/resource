package store.mybooks.resource.user_grade.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.repository.UserGradeNameRepository;

/**
 * packageName    : store.mybooks.resource.user_grade.repository
 * fileName       : UserGradeRepositoryTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */

@DataJpaTest
class UserGradeRepositoryTest {

    @Autowired
    UserGradeRepository userGradeRepository;

    @Autowired
    UserGradeNameRepository userGradeNameRepository;

    LocalDate localDate;

    Integer userGradeId;

    @BeforeEach
    void setUp() {

        localDate = LocalDate.now();

        UserGradeName userGradeName1 = new UserGradeName("일반");
        UserGradeName userGradeName2 = new UserGradeName("로얄");

        userGradeNameRepository.save(userGradeName1);
        userGradeNameRepository.save(userGradeName2);

        UserGrade userGrade1 = new UserGrade(1, userGradeName1, 0, 1000, 3, localDate, true);
        UserGrade userGrade2 = new UserGrade(2, userGradeName1, 0, 1000, 3, localDate, false);
        UserGrade userGrade3 = new UserGrade(3, userGradeName2, 0, 1000, 3, localDate, true);

        userGradeId = userGradeRepository.save(userGrade1).getId();
        userGradeRepository.save(userGrade2);
        userGradeRepository.save(userGrade3);

    }

    @Test
    @DisplayName("UserGradeId 로 QueryById 실행시 UserGradeGetResponse 반환")
    void givenUserGradeId_whenCallQueryById_thenReturnUserGradeGetResponse() {

        UserGradeGetResponse userGradeGetResponse = userGradeRepository.queryById(userGradeId);

        assertEquals("일반", userGradeGetResponse.getUserGradeNameId());
        assertEquals(3, userGradeGetResponse.getRate());
        assertEquals(localDate, userGradeGetResponse.getCreatedDate());
        assertEquals(1000, userGradeGetResponse.getMaxCost());
        assertEquals(0, userGradeGetResponse.getMinCost());
    }

    @Test
    @DisplayName("사용가능한 UserGradeName 으로 findByUserGradeNameIdAndIsAvailableIsTrue 실행시 UserGrade 반환")
    void givenUserGradeName_whenCallFindByUserGradeNameIdAndIsAvailableIsTrue_thenReturnOptionalUserGrade() {

        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue("일반").get();

        assertEquals("일반", userGrade.getUserGradeName().getId());
        assertEquals(true, userGrade.getIsAvailable());
    }

    @Test
    @DisplayName("queryAllBy 실행시 List<UserGradeGetResponse> 반환")
    void givenNothing_whenQueryAllBy_thenReturnUserGradeGetResponseList() {

        List<UserGradeGetResponse> list = userGradeRepository.queryAllByAndIsAvailableIsTrue();

        assertEquals(2, list.size());

    }


}