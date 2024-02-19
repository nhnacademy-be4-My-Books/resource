package store.mybooks.resource.publisher.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
import store.mybooks.resource.publisher.dto.response.PublisherDeleteResponse;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.dto.response.PublisherModifyResponse;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.publisher.exception.PublisherAlreadyExistException;
import store.mybooks.resource.publisher.exception.PublisherNotExistException;
import store.mybooks.resource.publisher.repository.PublisherRepository;

/**
 * packageName    : store.mybooks.resource.publisher.service
 * fileName       : PublisherServiceTest
 * author         : newjaehun
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        newjaehun       최초 생성
 */
@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {
    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    @Test
    @DisplayName("전체 출판사 조회")
    void givenPublisherList_whenFindAllPublishers_thenReturnAllPublishersGetResponseList() {
        List<PublisherGetResponse> publisherGetResponseList = new ArrayList<>();
        publisherGetResponseList.add( new PublisherGetResponse() {
                    @Override
                    public Integer getId() {
                        return 1;
                    }

                    @Override
                    public String getName() {
                        return "publisher1";
                    }
                }
        );
        publisherGetResponseList.add( new PublisherGetResponse() {
                    @Override
                    public Integer getId() {
                        return 2;
                    }

                    @Override
                    public String getName() {
                        return "publisher2";
                    }
                }
        );
        when(publisherRepository.findAllBy()).thenReturn(publisherGetResponseList);
        assertThat(publisherService.getAllPublisher()).isEqualTo(publisherGetResponseList);
    }


    @Test
    @DisplayName("출판사 등록")
    void givenPublisherCreateRequest_whenCreatePublisher_thenSavePublisherAndReturnPublisherCreateResponse() {
        String name = "publisherName";
        PublisherCreateRequest request = new PublisherCreateRequest(name);
        when(publisherRepository.existsByName(request.getName())).thenReturn(false);

        Publisher resultPublisher = new Publisher(1, name, LocalDate.now());

        when(publisherRepository.save(any(Publisher.class))).thenReturn(resultPublisher);

        PublisherCreateResponse response = publisherService.createPublisher(request);

        assertThat(response.getName()).isEqualTo(name);

    }

    @Test
    @DisplayName("이미 존재하는 출판사 이름을 등록")
    void givenPublisherCreateRequest_whenAlreadyExistPublisherNameCreate_thenThrowPublisherAlreadyExistException() {
        PublisherCreateRequest request = new PublisherCreateRequest("publisherName");
        when(publisherRepository.existsByName(request.getName())).thenReturn(true);
        assertThrows(PublisherAlreadyExistException.class, () -> publisherService.createPublisher(request));
    }

    @Test
    @DisplayName("출판사 수정")
    void givenPublisherIdAndPublisherModifyRequest_whenModifyPublisher_thenModifyPublisherAndReturnPublisherModifyResponse() {
        Integer publisherId = 1;
        Publisher publisher = new Publisher(publisherId, "publisherName", LocalDate.now());

        PublisherModifyRequest modifyRequest= new PublisherModifyRequest("publisherNameChange");

        when(publisherRepository.findById(eq(publisherId))).thenReturn(Optional.of(publisher));
        when(publisherRepository.existsByName(modifyRequest.getChangeName())).thenReturn(false);

        PublisherModifyResponse response = publisherService.modifyPublisher(publisherId, modifyRequest);
        assertThat(response.getName()).isEqualTo(modifyRequest.getChangeName());
    }

    @Test
    @DisplayName("존재하지 않는 출판사 수정")
    void givenPublisherId_whenNotExistPublisherModify_thenThrowPublisherNotExistException() {
        Integer publisherId = 1;
        PublisherModifyRequest modifyRequest= new PublisherModifyRequest("publisherNameChange");

        when(publisherRepository.findById(eq(publisherId))).thenReturn(Optional.empty());

        assertThrows(PublisherNotExistException.class, () -> publisherService.modifyPublisher(publisherId, modifyRequest));
    }
    @Test
    @DisplayName("이미 존재하는 출판사 이름으로 수정")
    void givenPublisherIdAndPublisherModifyRequest_whenAlreadyExistPublisherNameModify_thenThrowPublisherAlreadyExistException() {
        Integer publisherId = 1;
        Publisher publisher = new Publisher(publisherId, "publisherName", LocalDate.now());

        PublisherModifyRequest modifyRequest= new PublisherModifyRequest("publisherNameChange");

        when(publisherRepository.findById(eq(publisherId))).thenReturn(Optional.of(publisher));

        when(publisherRepository.existsByName(modifyRequest.getChangeName())).thenReturn(true);

        assertThrows(PublisherAlreadyExistException.class, () -> publisherService.modifyPublisher(publisherId, modifyRequest));
    }


    @Test
    @DisplayName("출판사 삭제")
    void givenPublisherId_whenDeletePublisher_thenDeletePublisherAndReturnPublisherDeleteResponse() {
        Integer publisherId = 1;
        Publisher publisher = new Publisher(publisherId, "publisherName", LocalDate.now());

        when(publisherRepository.findById(eq(publisherId))).thenReturn(Optional.of(publisher));
        doNothing().when(publisherRepository).deleteById(publisherId);

        PublisherDeleteResponse response = publisherService.deletePublisher(publisherId);
        assertThat(response.getName()).isEqualTo(publisher.getName());
        verify(publisherRepository, times(1)).deleteById(publisherId);
    }

    @Test
    @DisplayName("존재하지 않는 출판사 삭제")
    void givenPublisherId_whenNotExistPublisherDelete_thenThrowPublisherNotExistException() {
        Integer publisherId = 1;
        when(publisherRepository.findById(eq(publisherId))).thenReturn(Optional.empty());
        assertThrows(PublisherNotExistException.class, () -> publisherService.deletePublisher(publisherId));
    }
}