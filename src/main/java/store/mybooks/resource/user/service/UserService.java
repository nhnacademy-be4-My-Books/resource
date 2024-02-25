package store.mybooks.resource.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.user.dto.mapper.UserMapper;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserLoginRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserLoginResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserAlreadyExistException;
import store.mybooks.resource.user.exception.UserAlreadyResignException;
import store.mybooks.resource.user.exception.UserLoginFailException;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade.exception.UserGradeIdNotExistException;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
import store.mybooks.resource.user_grade_name.enumeration.UserGradeNameEnum;
import store.mybooks.resource.user_grade_name.exception.UserGradeNameNotExistException;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.enumeration.UserStatusEnum;
import store.mybooks.resource.user_status.exception.UserStatusNotExistException;
import store.mybooks.resource.user_status.repository.UserStatusRepository;


/**
 * packageName    : store.mybooks.resource.user.service
 * fileName       : UserService
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {


    private final UserRepository userRepository;

    private final UserStatusRepository userStatusRepository;

    private final UserGradeRepository userGradeRepository;

    private final UserMapper userMapper;

    /**
     * Create user user create response.
     * User를 생성함
     * User의 email이 중복되는 경우 UserAlreadyExistException
     * UserStatus가 존재하지 않는 경우 UserStatusNotExistException
     * 사용중인 UserGrade가 존재하지 않는 경우 UserGradeNameNotExistException
     *
     * @param createRequest the create request
     * @return the user create response
     * @throws UserAlreadyExistException
     * @throws UserStatusNotExistException
     * @throws UserGradeNameNotExistException
     */
    @Transactional
    public UserCreateResponse createUser(UserCreateRequest createRequest) {

        // 이미 존재하면 예외처리
        if (userRepository.findByEmail(createRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistException(createRequest.getEmail());
        }

        String userStatusName = UserStatusEnum.ACTIVE.toString();
        String userGradeName = UserGradeNameEnum.NORMAL.toString();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));

        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeName)
                .orElseThrow(() -> new UserGradeNameNotExistException(userGradeName));


        User user = new User(createRequest.getEmail(), createRequest.getBirth(), createRequest.getPassword(),
                createRequest.getPhoneNumber(), createRequest.getIsAdmin(), createRequest.getName(), userStatus,
                userGrade);

        userRepository.save(user);

        return userMapper.toUserCreateResponse(user);
    }

    /**
     * Modify user user modify response.
     * id 찾은 User를 수정함 , 이름 ,비밀번호,핸드폰번호 등 변경될 수 있는 모든 Field에 대해서 변경처리를 함
     * (후에 세분화 예정)
     * UserStatus가 존재하지 않는 경우 UserStatusNotExistException
     * 사용중인 UserGrade가 존재하지 않는 경우 UserGradeNameNotExistException
     *
     * @param id            the id
     * @param modifyRequest the modify request
     * @return the user modify response
     * @throws UserStatusNotExistException
     * @throws UserGradeNameNotExistException
     */
    @Transactional
    public UserModifyResponse modifyUser(Long id, UserModifyRequest modifyRequest) {

        // 없으면 예외처리
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException(id));

        String userStatusName = modifyRequest.getUserStatusName();
        String userGradeName = modifyRequest.getUserGradeName();

        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));
        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue(userGradeName).orElseThrow(
                UserGradeIdNotExistException::new);

        user.modifyUser(modifyRequest.getName(), modifyRequest.getPassword(), modifyRequest.getLatestLogin(),
                modifyRequest.getDeletedAt(), modifyRequest.getGradeChangeDate(), modifyRequest.getPhoneNumber());

        user.modifyUserStatus(userStatus);
        user.modifyUserGrade(userGrade);

        return userMapper.toUserModifyResponse(user);

    }

    /**
     * Delete user user delete response.
     * id로 찾은 User를 삭제함
     * 강삭제가 아닌 약삭제로 User의 상태를 "탈퇴"로 변경함
     *
     * @param id the id
     * @return the user delete response
     */
    @Transactional
    public UserDeleteResponse deleteUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotExistException(id));

        String userStatusName = UserStatusEnum.RESIGN.toString();
        UserStatus userStatus = userStatusRepository.findById(userStatusName)
                .orElseThrow(() -> new UserStatusNotExistException(userStatusName));

        user.modifyByDeleteRequest(userStatus);
        return new UserDeleteResponse(String.format("[%s] 유저 삭제완료", user.getEmail()));
    }

    /**
     * Find by email user get response.
     * id를 이용해 User를 반환함
     *
     * @param id the id
     * @return the user get response
     */
    public UserGetResponse findById(Long id) {

        return userRepository.queryById(id).orElseThrow(() -> new UserNotExistException(id));
    }


    /**
     * Find all user page.
     * 모든 User를 Pagination 해서 반환함
     *
     * @param pageable the pageable
     * @return the page
     */
    public Page<UserGetResponse> findAllUser(Pageable pageable) {

        return userRepository.queryAllBy(pageable);
    }

    public UserLoginResponse loginUser(UserLoginRequest userLoginRequest) {

        // 아이디,비밀번호 확인
        User user = userRepository.findByEmailAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword())
                .orElseThrow(UserLoginFailException::new);
        // 탈퇴한 회원인지 확인
        if (user.getUserStatus().getId().equals(UserStatusEnum.RESIGN.toString())) {
            throw new UserAlreadyResignException();
        }

        user.modifyLatestLogin();

        return new UserLoginResponse(true);

    }


}

