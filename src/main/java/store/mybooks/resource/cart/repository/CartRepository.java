package store.mybooks.resource.cart.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.cart.dto.CartDto;
import store.mybooks.resource.cart.entity.Cart;

/**
 * packageName    : store.mybooks.resource.cart.repository
 * fileName       : CartRepository
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Exists cart by user id boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    Boolean existsCartByUserId(Long userId);

    /**
     * Find cart by user id optional.
     *
     * @param userId the user id
     * @return the optional
     */
    Optional<CartDto> findCartByUserId(Long userId);
}
