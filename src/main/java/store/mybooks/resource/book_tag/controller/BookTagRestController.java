package store.mybooks.resource.book_tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.book_tag.dto.request.BookTagCreateRequest;
import store.mybooks.resource.book_tag.service.BookTagService;

/**
 * packageName    : store.mybooks.resource.book_tag.controller
 * fileName       : BookTagRestController
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-tag")
public class BookTagRestController {
    private final BookTagService bookTagService;

    /**
     * methodName : createBookTag <br>
     * author : damho-lee <br>
     * description : BookTag 생성.<br>
     *
     * @param bookTagCreateRequest BookTagCreateRequest
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Void> createBookTag(@RequestBody BookTagCreateRequest bookTagCreateRequest) {
        bookTagService.createBookTag(bookTagCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /**
     * methodName : deleteBookTag <br>
     * author : damho-lee <br>
     * description : BookId 로 BookTag 삭제.<br>
     *
     * @param bookId long
     * @return ResponseEntity
     */
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBookTag(@PathVariable("bookId") Long bookId) {
        bookTagService.deleteBookTag(bookId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}