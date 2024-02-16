package store.mybooks.resource.publisher.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
import store.mybooks.resource.publisher.dto.response.PublisherDeleteResponse;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.dto.response.PublisherModifyResponse;
import store.mybooks.resource.publisher.service.PublisherService;

/**
 * packageName    : store.mybooks.resource.publisher.controller
 * fileName       : PublisherRestController
 * author         : newjaehun
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        newjaehun       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherRestController {
    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<PublisherGetResponse>> getAllPublishers() {
        List<PublisherGetResponse> publishers = publisherService.getAllPublisher();
        return new ResponseEntity<>(publishers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherGetResponse> getPublisher(@PathVariable("id") Integer publisherId) {
        PublisherGetResponse getResponse = publisherService.getPublisher(publisherId);
        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PublisherCreateResponse> createPublisher(
            @RequestBody PublisherCreateRequest createRequest) {
        PublisherCreateResponse createPublisher = publisherService.createPublisher(createRequest);
        return new ResponseEntity<>(createPublisher, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PublisherModifyResponse> modifyPublisher(@RequestBody PublisherModifyRequest modifyRequest) {
        PublisherModifyResponse modifyPublisher = publisherService.modifyPublisher(modifyRequest);
        return new ResponseEntity<>(modifyPublisher, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<PublisherDeleteResponse> deletePublisher(@PathVariable("id") Integer publisherId) {
        PublisherDeleteResponse deleteResponse = publisherService.deletePublisher(publisherId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
