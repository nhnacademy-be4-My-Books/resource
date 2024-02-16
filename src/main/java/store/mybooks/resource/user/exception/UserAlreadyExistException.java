package store.mybooks.resource.user.exception;

/**
 * packageName    : store.mybooks.resource.user.exception
 * fileName       : UserAlreadyExistException
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
public class UserAlreadyExistException extends RuntimeException{

    // Custom Exception 이름이 곧 어떤 Exception임을 나타냄
    public UserAlreadyExistException() {
        super("이미 존재하는 유저");
    }
}