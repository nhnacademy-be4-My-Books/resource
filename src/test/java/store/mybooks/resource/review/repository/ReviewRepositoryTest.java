package store.mybooks.resource.review.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.coupon.entity.Coupon;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.orderdetail.entity.OrderDetail;
import store.mybooks.resource.orderdetailstatus.entity.OrderDetailStatus;
import store.mybooks.resource.ordersstatus.entity.OrdersStatus;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.review.dto.response.ReviewDetailGetResponse;
import store.mybooks.resource.review.dto.response.ReviewGetResponse;
import store.mybooks.resource.review.dto.response.ReviewRateResponse;
import store.mybooks.resource.review.entity.Review;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.usercoupon.entity.UserCoupon;

/**
 * packageName    : store.mybooks.resource.review.repository<br>
 * fileName       : ReviewRepositoryTest<br>
 * author         : masiljangajji<br>
 * date           : 3/24/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/24/24        masiljangajji       최초 생성
 */

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    TestEntityManager testEntityManager;

    Review review;

    Review review2;

    LocalDate localDate;

    static Long id;
    static Long id2;

    static Long userId;

    static Long bookId;

    static Long reviewId;

    static Long reviewId2;

    @BeforeEach
    void setUp() {
        localDate = LocalDate.of(1111,11,11);

        UserStatus userStatus = new UserStatus("test_status");
        UserGradeName userGradeName = new UserGradeName("test_user_grade");
        testEntityManager.persist(userStatus);
        testEntityManager.persist(userGradeName);
        UserGrade userGrade = new UserGrade(1, 100, 5, userGradeName);
        testEntityManager.persist(userGrade);
        User user = new User("test@test.com", localDate, "password@123", "01012345678", false, "name", userStatus,
                userGrade, "oauthId");
        userId=testEntityManager.persist(user).getId();
        OrdersStatus ordersStatus = new OrdersStatus("order_status");
        testEntityManager.persist(ordersStatus);
        DeliveryRuleName deliveryRuleName = new DeliveryRuleName("test");
        testEntityManager.persist(deliveryRuleName);
        DeliveryRule deliveryRule = new DeliveryRule(deliveryRuleName, "name", 100, 10000);
        testEntityManager.persist(deliveryRule);
        BookStatus bookStatus = new BookStatus("test");
        testEntityManager.persist(bookStatus);
        Publisher publisher = new Publisher("test");
        testEntityManager.persist(publisher);
        Book book = new Book();
        book.setModifyRequest(bookStatus, publisher, "name", "isbn", localDate, 1, "index", "explantation", 1000, 500,
                500, 500, true);
        bookId=testEntityManager.persist(book).getId();


        Category category = new Category(null, "test");
        testEntityManager.persist(category);
        Coupon coupon = new Coupon("test", book, category, 1, 10, 1, 1, localDate, localDate, true, true);
        testEntityManager.persist(coupon);
        UserCoupon userCoupon = new UserCoupon(user, coupon);
        testEntityManager.persist(userCoupon);

        BookOrder bookOrder =
                new BookOrder(id, localDate, localDate, localDate, "test", "testName", "testAddress", "testPohne",
                        "testMesage", 10000, 100, 100, true, "number", "password", user, ordersStatus, deliveryRule,
                        userCoupon);
        testEntityManager.persist(bookOrder);
        BookOrder bookOrder2 =
                new BookOrder(id2, localDate, localDate, localDate, "test", "testName", "testAddress", "testPohne",
                        "testMesage", 10000, 100, 100, false, "number", "password", user, ordersStatus, deliveryRule,
                        null);
        testEntityManager.persist(bookOrder2);



        OrderDetailStatus orderDetailStatus = new OrderDetailStatus("test");
        testEntityManager.persist(orderDetailStatus);

        OrderDetail orderDetail;
        OrderDetail orderDetail2;

        orderDetail =
                new OrderDetail(id, 10000, 1, true, bookOrder, book, orderDetailStatus, userCoupon, null, localDate);
        testEntityManager.persist(orderDetail);
        orderDetail2 =
                new OrderDetail(id2, 50000, 1, false, bookOrder2, book, orderDetailStatus, null, null, localDate);
        testEntityManager.persist(orderDetail2);

        review = new Review(user, orderDetail, 5, "review_title", "review_content");
        reviewId=testEntityManager.persist(review).getId();
        review2 = new Review(user, orderDetail2, 3, "review_title_2", "review_content_2");
        reviewId2=testEntityManager.persist(review2).getId();

    }


    @Test
    @DisplayName("유저 아이디로 리뷰 조회")
    void givenUserId_whenCallGetReviewByUserId_thenReturnReviewGetResponsePage() {

        reviewRepository.save(review);
        reviewRepository.save(review2);

        Page<ReviewGetResponse> page = reviewRepository.getReviewByUserId(userId, PageRequest.of(0, 2));

        assertThat(page).isNotNull();
        List<ReviewGetResponse> list = page.getContent();


        assertThat(list.get(0).getReviewId()).isEqualTo(reviewId);
        assertThat(list.get(0).getRate()).isEqualTo(5);
        assertThat(list.get(0).getBookId()).isEqualTo(bookId);
        assertThat(list.get(0).getBookName()).isEqualTo("name");
        assertThat(list.get(0).getTitle()).isEqualTo("review_title");
        assertThat(list.get(0).getUserName()).isEqualTo("name");
        assertThat(list.get(0).getReviewImage()).isNull();
        assertThat(list.get(1).getReviewId()).isEqualTo(reviewId2);
        assertThat(list.get(1).getRate()).isEqualTo(3);
        assertThat(list.get(1).getBookId()).isEqualTo(bookId);
        assertThat(list.get(1).getBookName()).isEqualTo("name");
        assertThat(list.get(1).getTitle()).isEqualTo("review_title_2");
        assertThat(list.get(1).getUserName()).isEqualTo("name");
        assertThat(list.get(1).getReviewImage()).isNull();


    }

    @Test
    @DisplayName(("리뷰 아이디로 리뷰 조회"))
    void givenReviewId_whenCallGetReview_thenReturnReviewGetResponse() {

        Optional<ReviewGetResponse> response = reviewRepository.getReview(review.getId());

        assertTrue(response.isPresent());

        ReviewGetResponse reviewGetResponse = response.get();
        assertThat(reviewGetResponse.getBookId()).isEqualTo(bookId);
        assertThat(reviewGetResponse.getBookName()).isEqualTo("name");
        assertThat(reviewGetResponse.getReviewId()).isEqualTo(reviewId);
        assertThat(reviewGetResponse.getUserName()).isEqualTo("name");
        assertThat(reviewGetResponse.getRate()).isEqualTo(5);
        assertThat(reviewGetResponse.getTitle()).isEqualTo("review_title");
        assertThat(reviewGetResponse.getContent()).isEqualTo("review_content");
        assertThat(reviewGetResponse.getReviewImage()).isNull();
    }

    @Test
    @DisplayName(("책 아이디로 리뷰 조회"))
    void givenBookId_whenCallGetReviewByBookId_thenReturnReviewDetailGetResponsePage() {


        Page<ReviewDetailGetResponse> page2 = reviewRepository.getReviewByBookId(bookId, PageRequest.of(0, 2));
        assertThat(page2).isNotNull();
        List<ReviewDetailGetResponse> list2 = page2.getContent();

        assertThat(list2.get(0).getReviewId()).isEqualTo(reviewId);
        assertThat(list2.get(0).getRate()).isEqualTo(5);
        assertThat(list2.get(0).getUserName()).isEqualTo("name");
        assertThat(list2.get(0).getTitle()).isEqualTo("review_title");
        assertThat(list2.get(0).getContent()).isEqualTo("review_content");
        assertThat(list2.get(0).getReviewImage()).isNull();

        assertThat(list2.get(1).getReviewId()).isEqualTo(reviewId2);
        assertThat(list2.get(1).getRate()).isEqualTo(3);
        assertThat(list2.get(1).getUserName()).isEqualTo("name");
        assertThat(list2.get(1).getTitle()).isEqualTo("review_title_2");
        assertThat(list2.get(1).getContent()).isEqualTo("review_content_2");
        assertThat(list2.get(1).getReviewImage()).isNull();
    }
    @Test
    @DisplayName("책 아이디로 전체 리뷰 평점평균 및 개수 조회")
    void givenBookId_whenCallGetReviewRate_thenReturnReviewRateResponse() {



        ReviewRateResponse response2 = reviewRepository.getReviewRate(bookId);

        assertThat(response2.getTotalCount()).isEqualTo(2);
        assertThat(response2.getAverageRate()).isEqualTo(4.0);
    }


}