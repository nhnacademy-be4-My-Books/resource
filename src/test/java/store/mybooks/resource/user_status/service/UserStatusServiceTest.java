package store.mybooks.resource.user_status.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
import store.mybooks.resource.user_status.exception.UserStatusNotExistException;
import store.mybooks.resource.user_status.repository.UserStatusRepository;

/**
 * packageName    : store.mybooks.resource.user_status.service
 * fileName       : UserStatusServiceTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */

@ExtendWith(MockitoExtension.class)
class UserStatusServiceTest {


    @Mock
    UserStatusRepository userStatusRepository;

    @InjectMocks
    UserStatusService userStatusService;

    @Mock
    UserStatusGetResponse userStatusGetResponse;

    @Test
    @DisplayName("UserStatus 로 findUserStatusById 메서드 실행시 동작이 올바른지 테스트")
    void givenUserStatusId_whenCallFindUserStatusById_thenReturnUserStatusGetResponse() {


        when(userStatusRepository.existsById(anyString())).thenReturn(true);
        when(userStatusRepository.queryById(anyString())).thenReturn(userStatusGetResponse);
        when(userStatusGetResponse.getId()).thenReturn("test");


        assertEquals("test", userStatusService.findUserStatusById("test").getId());


        verify(userStatusRepository, times(1)).existsById(anyString());
        verify(userStatusRepository, times(1)).queryById(anyString());
    }

    @Test
    @DisplayName("findUserStatusById 메서드를 실패하는 경우(존재하지 않는 id로 find) UserStatusNotExistException 발생")
    void givenWrongUserStatusId_whenCallFindUserStatusById_thenThrowUserStatusNotExistException() {


        assertThrows(UserStatusNotExistException.class, () -> userStatusService.findUserStatusById("wrong_id"));
        verify(userStatusRepository, times(1)).existsById(anyString());
    }

    @Test
    @DisplayName("findAllUserStatus 메서드를 실행시 Empty List 를 반환")
    void givenNothing_whenCallFindAllUserStatus_thenReturnEmptyList() {

        assertTrue(userStatusService.findAllUserStatus().isEmpty());
        verify(userStatusRepository, times(1)).queryAllBy();
    }

    @Test
    @DisplayName("findAllUserStatus 메서드를 불렀을때 Not Empty List 를 반환")
    void givenUserStatus_whenCallFindAllUserStatus_thenReturnNotEmptyList() {

        when(userStatusService.findAllUserStatus()).thenReturn(List.of(userStatusGetResponse));
        assertFalse(userStatusService.findAllUserStatus().isEmpty());
        verify(userStatusRepository, times(1)).queryAllBy();
    }


}