package store.mybooks.resource.user_grade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.user_grade.dto.request.UserGradeCreateRequest;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeDeleteResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;
import store.mybooks.resource.user_grade.service.UserGradeService;

/**
 * packageName    : store.mybooks.resource.user_grade.controller
 * fileName       : UserGradeRestController
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/grades")
public class UserGradeRestController {

    private final UserGradeService userGradeService;

    @PostMapping
    public ResponseEntity<UserGradeCreateResponse> createUser(
            @RequestBody UserGradeCreateRequest createRequest) {


        UserGradeCreateResponse createResponse = userGradeService.createUserGrade(createRequest);

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserGradeDeleteResponse> deleteUser(@PathVariable(name = "id") String id) {

        UserGradeDeleteResponse deleteResponse = userGradeService.deleteUserGrade(Integer.parseInt(id));

        return new ResponseEntity<>(deleteResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGradeGetResponse> findUserByEmail(@PathVariable(name = "id") String id) {

        UserGradeGetResponse getResponse = userGradeService.findUserGradeById(Integer.parseInt(id));

        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }


}