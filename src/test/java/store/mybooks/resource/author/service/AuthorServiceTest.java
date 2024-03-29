package store.mybooks.resource.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.author.dto.request.AuthorCreateRequest;
import store.mybooks.resource.author.dto.request.AuthorModifyRequest;
import store.mybooks.resource.author.dto.response.AuthorCreateResponse;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.author.dto.response.AuthorModifyResponse;
import store.mybooks.resource.author.entity.Author;
import store.mybooks.resource.author.exception.AuthorNotExistException;
import store.mybooks.resource.author.mapper.AuthorMapper;
import store.mybooks.resource.author.repository.AuthorRepository;

/**
 * packageName    : store.mybooks.resource.author.service
 * fileName       : AuthorServiceTest
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorMapper authorMapper;
    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private static final Integer id = 1;
    private static final String name = "author";
    private static final String content = "author_content";

    @BeforeEach
    void setUp() {
        author = new Author(id, name, content, LocalDate.now());
    }

    @Test
    @DisplayName("전체 저자 조회(리스트)")
    void whenFindAllAuthors_thenReturnAllAuthorsGetResponseList() {
        List<AuthorGetResponse> authorGetResponseList =
                Arrays.asList(new AuthorGetResponse(id, name, content),
                        new AuthorGetResponse(2, "author2", "author_content2"));

        when(authorRepository.getAllAuthors()).thenReturn(authorGetResponseList);
        assertThat(authorService.getAllAuthors()).isEqualTo(authorGetResponseList);

        verify(authorRepository, times(1)).getAllAuthors();
    }

    @Test
    @DisplayName("전체 저자 조회(페이징)")
    void givenAuthorListAndPageable_whenFindAllAuthors_thenReturnPageAuthorsGetResponseList() {
        Pageable pageable = PageRequest.of(0, 2);
        List<AuthorGetResponse> authorGetResponseList =
                Arrays.asList(new AuthorGetResponse(id, name, content),
                        new AuthorGetResponse(2, "author2", "author_content2"));

        Page<AuthorGetResponse> pageGetResponse =
                new PageImpl<>(authorGetResponseList, pageable, authorGetResponseList.size());
        when(authorRepository.getAllPagedAuthors(pageable)).thenReturn(pageGetResponse);
        assertThat(authorService.getPagedAuthors(pageable)).isEqualTo(pageGetResponse);

        verify(authorRepository, times(1)).getAllPagedAuthors(pageable);
    }

    @Test
    @DisplayName("저자 조회")
    void givenAuthorId_whenFindAuthor_thenReturnAuthorsGetResponse() {
        AuthorGetResponse getResponse = new AuthorGetResponse(author.getId(), author.getName(), author.getContent());


        when(authorRepository.getAuthorInfo(id)).thenReturn(getResponse);
        assertThat(authorService.getAuthor(id)).isEqualTo(getResponse);

        verify(authorRepository, times(1)).getAuthorInfo(id);
    }

    @Test
    @DisplayName("저자 추가")
    void givenAuthorNameAndContent_whenCreateAuthor_thenSaveAuthorAndReturnAuthorCreateResponse() {
        AuthorCreateRequest createRequest = new AuthorCreateRequest(name, content);
        AuthorCreateResponse createResponse = new AuthorCreateResponse();
        createResponse.setName(createRequest.getName());
        createResponse.setContent(createRequest.getContent());

        given(authorRepository.save(any())).willReturn(author);
        when(authorMapper.createResponse(any(Author.class))).thenReturn(createResponse);

        authorService.createAuthor(createRequest);

        assertThat(createResponse.getName()).isEqualTo(author.getName());
        assertThat(createResponse.getContent()).isEqualTo(author.getContent());

        verify(authorRepository, times(1)).save(any(Author.class));
        verify(authorMapper, times(1)).createResponse(any(Author.class));
    }

    @Test
    @DisplayName("저자 수정")
    void givenAuthorIdAndAuthorModifyRequest_whenModifyAuthor_thenModifyAuthorAndReturnAuthorModifyResponse() {
        AuthorModifyRequest modifyRequest = new AuthorModifyRequest("changeName", "changeContent");
        AuthorModifyResponse modifyResponse = new AuthorModifyResponse();
        modifyResponse.setChangedName(modifyRequest.getChangeName());
        modifyResponse.setChangedContent(modifyRequest.getChangeContent());

        given(authorRepository.findById(id)).willReturn(Optional.of(author));
        when(authorMapper.modifyResponse(any(Author.class))).thenReturn(modifyResponse);

        authorService.modifyAuthor(id, modifyRequest);

        assertThat(modifyResponse.getChangedName()).isEqualTo(modifyRequest.getChangeName());
        assertThat(modifyResponse.getChangedContent()).isEqualTo(modifyRequest.getChangeContent());

        verify(authorRepository, times(1)).findById(id);
        verify(authorMapper, times(1)).modifyResponse(any(Author.class));
    }

    @Test
    @DisplayName("저자 삭제(저자 존재)")
    void givenAuthorId_whenDeleteAuthor_thenDeleteAuthorAndReturnAuthorDeleteResponse() {
        given(authorRepository.existsById(id)).willReturn(true);

        doNothing().when(authorRepository).deleteById(id);

        authorService.deleteAuthor(id);

        verify(authorRepository, times(1)).existsById(id);
        verify(authorRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("저자 삭제(저자 미존재)")
    void givenNotExistedAuthorId_whenDeleteAuthor_thenThrowAuthorNotExistException() {
        given(authorRepository.existsById(id)).willReturn(false);

        assertThrows(AuthorNotExistException.class, () -> authorService.deleteAuthor(id));

        verify(authorRepository, times(1)).existsById(id);
        verify(authorRepository, times(0)).deleteById(id);
    }
}