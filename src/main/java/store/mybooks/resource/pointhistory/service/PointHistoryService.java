package store.mybooks.resource.pointhistory.service;

import java.time.LocalDate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.bookorder.repository.BookOrderRepository;
import store.mybooks.resource.pointhistory.dto.mapper.PointHistoryMapper;
import store.mybooks.resource.pointhistory.dto.request.PointHistoryCreateRequest;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryCreateResponse;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryResponse;
import store.mybooks.resource.pointhistory.dto.response.PointResponse;
import store.mybooks.resource.pointhistory.dto.response.PointResponseForUser;
import store.mybooks.resource.pointhistory.entity.PointHistory;
import store.mybooks.resource.pointhistory.exception.AlreadyReceivedSignUpPoint;
import store.mybooks.resource.pointhistory.repository.PointHistoryRepository;
import store.mybooks.resource.pointrule.entity.PointRule;
import store.mybooks.resource.pointrule.exception.PointRuleNotExistException;
import store.mybooks.resource.pointrule.repository.PointRuleRepository;
import store.mybooks.resource.pointrulename.entity.PointRuleName;
import store.mybooks.resource.pointrulename.repository.PointRuleNameRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.utils.TimeUtils;

/**
 * packageName    : store.mybooks.resource.point_history.service<br>
 * fileName       : PointHistoryService<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryService {
    private final UserRepository userRepository;
    private final BookOrderRepository bookOrderRepository;
    private final PointRuleRepository pointRuleRepository;
    private final PointRuleNameRepository pointRuleNameRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final PointHistoryMapper pointHistoryMapper;

    /**
     * methodName : getRemainingPoint<br>
     * author : minsu11<br>
     * description : 유저의 잔여 포인트 조회.
     * <br> *
     *
     * @param userId 잔여 포인트 조회 할 회원 {@code id}
     * @return point response
     */
    @Transactional(readOnly = true)
    public PointResponse getRemainingPoint(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }
        return pointHistoryRepository.getRemainingPoint(userId);
    }

    /**
     * methodName : getPointHistory<br>
     * author : minsu11<br>
     * description : 포인트 내역.
     * <br> *
     *
     * @param pageable 페이징
     * @param userId   회원 아이디
     * @return page
     */
    @Transactional(readOnly = true)
    public PointResponseForUser getPointHistory(Pageable pageable, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }
        PointResponse pointResponse = pointHistoryRepository.getRemainingPoint(userId);
        Page<PointHistoryResponse> pointHistoryResponsePage =
                pointHistoryRepository.getPointHistoryByUserId(pageable, userId);
        return new PointResponseForUser(pointResponse.getRemainingPoint(), pointHistoryResponsePage);
    }

    /**
     * methodName : createPointHistory<br>
     * author : minsu11<br>
     * description : 포인트 내역 생성.
     * <br>
     *
     * @param request the request
     * @param userId  the user id
     * @return the point history create response
     */
    public PointHistoryCreateResponse createPointHistory(PointHistoryCreateRequest request, Long userId) {
        PointRuleName pointRulename = pointRuleNameRepository.findById(request.getPointName())
                .orElseThrow(PointRuleNotExistException::new);

        PointRule pointRule = pointRuleRepository.findPointRuleByPointRuleName(pointRulename.getId())
                .orElseThrow(PointRuleNotExistException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException(userId));

        BookOrder bookOrder = bookOrderRepository.findByNumber(request.getOrderNumber())
                .orElseThrow(BookOrderNotExistException::new);

        PointHistory pointHistory = new PointHistory(request.getPointCost(), user, pointRule, bookOrder);
        return pointHistoryMapper.mapToPointHistoryCreateResponse(pointHistoryRepository.save(pointHistory));
    }

    /**
     * methodName : saveLoginPoint <br>
     * author : damho-lee <br>
     * description : 날마다 첫 로그인 시 포인트 적립.<br>
     *
     * @param userId Long
     * @return boolean
     */
    public boolean saveLoginPoint(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));


        if (Objects.nonNull(user.getLatestLogin())) {
            LocalDate latestLoginDate = user.getLatestLogin().toLocalDate();

            if (!latestLoginDate.isBefore(TimeUtils.nowDate())) {
                return false;
            }
        }

        PointRule pointRule = pointRuleRepository.findPointRuleByPointRuleName("로그인 적립")
                .orElseThrow(PointRuleNotExistException::new);
        pointHistoryRepository.save(new PointHistory(
                pointRule.getCost(),
                user,
                pointRule,
                null
        ));

        return true;
    }

    /**
     * methodName : saveOauthLoginPoint <br>
     * author : damho-lee <br>
     * description : OAuth 로 로그인하는 회원의 로그인 포인트 적립.<br>
     *
     * @param userId 회원 아이디
     */
    public void saveOauthLoginPoint(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));

        PointRule pointRule = pointRuleRepository.findPointRuleByPointRuleName("로그인 적립")
                .orElseThrow(PointRuleNotExistException::new);
        pointHistoryRepository.save(new PointHistory(
                pointRule.getCost(),
                user,
                pointRule,
                null
        ));

    }


    /**
     * methodName : saveSignUpPoint <br>
     * author : damho-lee <br>
     * description : 회원가입 포인트 적립.<br>
     *
     * @param email 회원아이디
     */
    public void saveSignUpPoint(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotExistException(email));
        if (pointHistoryRepository.isAlreadyReceivedSignUpPoint(email)) {
            throw new AlreadyReceivedSignUpPoint();
        }

        PointRule pointRule = pointRuleRepository.findPointRuleByPointRuleName("회원가입 적립")
                .orElseThrow(PointRuleNotExistException::new);

        pointHistoryRepository.save(new PointHistory(
                pointRule.getCost(),
                user,
                pointRule,
                null
        ));
    }

    /**
     * methodName : getUsedPointOrder <br>
     * author : minsu11 <br>
     * description : 주문에서 사용한 포인트.<br>
     *
     * @param orderNumber 주문 번호
     * @return the used point order
     */
    public Integer getUsedPointOrder(String orderNumber) {
        return pointHistoryRepository.getOrderUsedPoint(orderNumber);
    }
}