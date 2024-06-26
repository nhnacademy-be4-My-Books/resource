package store.mybooks.resource.bookorder.repository;

import com.querydsl.core.types.Projections;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderPaymentInfoRespones;
import store.mybooks.resource.bookorder.dto.response.BookOrderUserResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminResponse;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.entity.QBookOrder;
import store.mybooks.resource.bookorder.eumulation.BookOrderStatusName;
import store.mybooks.resource.image.entity.QImage;
import store.mybooks.resource.image_status.entity.QImageStatus;
import store.mybooks.resource.image_status.enumeration.ImageStatusEnum;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.orderdetail.entity.QOrderDetail;
import store.mybooks.resource.orderdetailstatus.entity.QOrderDetailStatus;
import store.mybooks.resource.ordersstatus.enumulation.OrdersStatusEnum;

/**
 * packageName    : store.mybooks.resource.book_order.repository<br>
 * fileName       : BookOrderRepositoryImpl<br>
 * author         : minsu11<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/2/24        minsu11       최초 생성<br>
 */
@Slf4j
public class BookOrderRepositoryImpl extends QuerydslRepositorySupport implements BookOrderRepositoryCustom {
    private static final QBookOrder bookOrder = QBookOrder.bookOrder;
    private static final QImage image = QImage.image;
    private static final QOrderDetail orderDetail = QOrderDetail.orderDetail;
    private static final QImageStatus imageStatus = QImageStatus.imageStatus;


    private static final QOrderDetailStatus orderDetailStatus = QOrderDetailStatus.orderDetailStatus;

    public BookOrderRepositoryImpl() {
        super(BookOrder.class);
    }


    @Override
    public Page<BookOrderUserResponse> getBookOrderPageByUserId(Long userId, Pageable pageable) {


        List<BookOrderUserResponse> bookOrderResponseList =
                from(bookOrder)
                        .select(Projections.constructor(BookOrderUserResponse.class,
                                bookOrder.orderStatus.id,
                                bookOrder.deliveryRule.deliveryRuleName.id,
                                bookOrder.deliveryRule.cost,
                                bookOrder.date,
                                bookOrder.invoiceNumber,
                                bookOrder.receiverName,
                                bookOrder.receiverAddress,
                                bookOrder.receiverPhoneNumber,
                                bookOrder.receiverMessage,
                                bookOrder.totalCost,
                                bookOrder.pointCost,
                                bookOrder.couponCost,

                                bookOrder.number,
                                bookOrder.id
                        ))
                        .where(bookOrder.user.id.eq(userId)
                                .and(bookOrder.orderStatus.id.eq(BookOrderStatusName.ORDER_WAIT.toString()).not()))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(bookOrder.id.desc())
                        .fetch();
        for (BookOrderUserResponse bookOrderUserResponse : bookOrderResponseList) {
            List<OrderDetailInfoResponse> orderDetailInfoResponses =
                    from(orderDetail)
                            .join(image).on(image.book.eq(orderDetail.book))
                            .join(image.imageStatus, imageStatus)
                            .join(orderDetail.detailStatus, orderDetailStatus)
                            .where(imageStatus.id.eq(ImageStatusEnum.THUMBNAIL.getName()))
                            .select(Projections.constructor(
                                    OrderDetailInfoResponse.class,
                                    orderDetail.book.id,
                                    orderDetail.book.name,
                                    orderDetail.userCoupon.id,
                                    orderDetail.amount,
                                    orderDetail.bookCost,
                                    orderDetail.isCouponUsed,
                                    image.path.concat(image.fileName).concat(image.extension),
                                    orderDetail.detailStatus.id,
                                    orderDetail.id

                            ))
                            .where(orderDetail.bookOrder.number
                                    .eq(bookOrderUserResponse.getNumber()))
                            .orderBy(orderDetail.bookOrder.date.desc())
                            .fetch();
            bookOrderUserResponse.createOrderDetailInfos(orderDetailInfoResponses);
        }


        long count = from(bookOrder)
                .where(bookOrder.user.id.eq(userId)
                        .and(bookOrder.orderStatus.id.eq(BookOrderStatusName.ORDER_WAIT.toString()).not()))
                .fetchCount();

        return new PageImpl<>(bookOrderResponseList, pageable, count);
    }

    @Override
    public Page<BookOrderAdminResponse> getBookOrderPageByOrderStatusId(Pageable pageable) {
        List<BookOrderAdminResponse> bookOrderAdminResponseList =
                from(bookOrder)
                        .select(Projections.constructor(BookOrderAdminResponse.class,
                                bookOrder.id,
                                bookOrder.user.id,
                                bookOrder.orderStatus.id,
                                bookOrder.date,
                                bookOrder.outDate,
                                bookOrder.invoiceNumber,
                                bookOrder.number))
                        .where(bookOrder.orderStatus.id.eq(OrdersStatusEnum.WAIT.toString()))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();
        long count = from(bookOrder)
                .where(bookOrder.orderStatus.id.eq(OrdersStatusEnum.WAIT.toString()))
                .fetchCount();

        return new PageImpl<>(bookOrderAdminResponseList, pageable, count);
    }

    @Override
    public Boolean existBookOrderByOrderNumber(String orderNumber) {

        return Objects.nonNull(
                from(bookOrder)
                        .where(bookOrder.number.eq(orderNumber))
        );
    }

    @Override
    public Optional<BookOrderInfoPayResponse> findBookOrderInfo(String orderNumber) {
        List<OrderDetailInfoResponse> orderDetailInfoResponses =
                from(orderDetail)
                        .join(image).on(image.book.eq(orderDetail.book))
                        .join(image.imageStatus, imageStatus)
                        .join(orderDetail.detailStatus, orderDetailStatus)
                        .where(imageStatus.id.eq(ImageStatusEnum.THUMBNAIL.getName()))
                        .select(Projections.constructor(
                                        OrderDetailInfoResponse.class,
                                        orderDetail.book.id,
                                        orderDetail.book.name,
                                        orderDetail.userCoupon.id,
                                        orderDetail.bookCost,
                                        orderDetail.amount,
                                        orderDetail.isCouponUsed,
                                        image.path.concat(image.fileName).concat(image.extension),
                                        orderDetail.detailStatus.id,
                                        orderDetail.id
                                )
                        )
                        .where(orderDetail.bookOrder.number.eq(orderNumber))
                        .fetch();

        BookOrderInfoPayResponse bookOrderInfo =
                from(bookOrder)
                        .select(Projections.constructor(BookOrderInfoPayResponse.class,
                                bookOrder.orderStatus.id,
                                bookOrder.number,
                                bookOrder.totalCost,
                                bookOrder.isCouponUsed,
                                bookOrder.pointCost))
                        .where(bookOrder.number.eq(orderNumber))
                        .fetchOne();
        bookOrderInfo.updateOrderDetails(orderDetailInfoResponses);

        return Optional.of(bookOrderInfo);
    }

    @Override
    public Optional<BookOrderPaymentInfoRespones> findOrderPayInfo(String orderNumber) {
        BookOrderPaymentInfoRespones bookOrderInfoPayResponse = from(bookOrder)
                .select(Projections.constructor(
                        BookOrderPaymentInfoRespones.class,
                        bookOrder.user.name,
                        bookOrder.user.email,
                        bookOrder.user.phoneNumber,
                        bookOrder.number,
                        bookOrder.orderStatus.id
                ))
                .where(bookOrder.number.eq(orderNumber))
                .fetchOne();

        Long count = from(orderDetail)
                .where(orderDetail.bookOrder.number.eq(orderNumber))
                .fetchCount();

        StringBuilder orderName = new StringBuilder().append(from(orderDetail)
                .select(orderDetail.book.name)
                .where(orderDetail.bookOrder.number.eq(orderNumber))
                .fetchFirst());

        if (count > 1) {
            count -= 1;
            orderName.append("외 ").append(count).append("건");
        }
        bookOrderInfoPayResponse.updateOrderName(orderName.toString());

        return Optional.of(
                bookOrderInfoPayResponse
        );
    }

    @Override
    public Long getUserBookOrderCount(Long userId) {

        return from(bookOrder)
                .where(bookOrder.user.id.eq(userId))
                .fetchCount();
    }


    @Override
    public List<BookOrder> getBookOrderByOutDate() {
        return from(bookOrder)
                .where(bookOrder.outDate.eq(LocalDate.now().minusDays(2)))
                .where(bookOrder.orderStatus.id.eq(BookOrderStatusName.DELIVERING.toString()))
                .fetch();
    }
}
