package store.mybooks.resource.cart_item.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.cart_item.entity.CartItem;

/**
 * packageName    : store.mybooks.resource.cart_item.repository
 * fileName       : CartItemRepository
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCart_Id(Long cartId);

    List<CartItem> findCartItemsByCart_Id(Long cartId);

    boolean existsCartItemByCart_IdAndBook_Id(Long cartId, Long BookId);

    void deleteCartItemByCart_IdAndBook_Id(Long cartId, Long BookId);
}
