package store.mybooks.resource.payment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.payment.entity.Payment;
import store.mybooks.resource.payment.entity.QPayment;

/**
 * packageName    : store.mybooks.resource.payment.repository<br>
 * fileName       : PaymentRepositoryCutomImpl<br>
 * author         : minsu11<br>
 * date           : 3/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/20/24        minsu11       최초 생성<br>
 */
public class PaymentRepositoryImpl extends QuerydslRepositorySupport implements PaymentRepositoryCustom {
    private final QPayment payment = QPayment.payment;

    public PaymentRepositoryImpl() {
        super(Payment.class);
    }

    @Override
    public Optional<Payment> findByOrderNumber(String orderNumber) {

        return Optional.ofNullable(
                from(payment)
                        .where(payment.bookOrder.number.eq(orderNumber)).fetchOne()
        );
    }

    @Override
    public boolean existPaymentByOrderNumber(String orderNumber) {

        return from(payment)
                .where(payment.bookOrder.number.eq(orderNumber))
                .fetchFirst() != null;
    }
}