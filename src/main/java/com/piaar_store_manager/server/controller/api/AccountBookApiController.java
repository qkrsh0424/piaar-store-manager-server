package com.piaar_store_manager.server.controller.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.model.account_book.dto.AccountBookDefDto;
import com.piaar_store_manager.server.service.account_book.AccountBookService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account-book")
@RequiredArgsConstructor
@RequiredLogin
public class AccountBookApiController {

    private final AccountBookService accountBookService;

    /**
     * Search all list api for account book.
     * <p>
     * Have many search condition options, their all in query parameter.
     * <p>
     * <b>GET : API URL => /api/v1/account-book/list</b>
     *
     * @param query : Map[accountBookType, bankType, startDate, endDate, currPage]
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see AccountBookService#searchList
     * @see AccountBookService#searchPagenation
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList(@RequestParam Map<String, Object> query) {

        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(accountBookService.searchList(query));
        message.setPagenation(accountBookService.searchPagenation(query));

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create list api for account book.
     * <p>
     * <b>POST : API URL => /api/v1/account-book/list</b>
     *
     * @param accountBookDefDtos : List
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see AccountBookDefDto
     * @see AccountBookService#createList
     */
    @PostMapping("/list")
    public ResponseEntity<?> createList(@RequestBody List<AccountBookDefDto> accountBookDefDtos) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        accountBookService.createList(accountBookDefDtos);

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Get sum of income for account book.
     * <p>
     * Have many search condition options, their all in query parameter.
     * <p>
     * <b>GET : API URL => /api/v1/account-book/sum/income</b>
     *
     * @param query : Map[accountBookType, bankType, startDate, endDate]
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see AccountBookService#getSumIncome
     */
    @GetMapping("/sum/income")
    public ResponseEntity<?> calcSumIncome(@RequestParam Map<String, Object> query) {
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(accountBookService.getSumIncome(query));

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Get sum of expenditure for account book.
     * <p>
     * Have many search condition options, their all in query parameter.
     * <p>
     * <b>GET : API URL => /api/v1/account-book/sum/expenditure</b>
     *
     * @param query : Map[accountBookType, bankType, startDate, endDate]
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see AccountBookService#getSumExpenditure
     */
    @GetMapping("/sum/expenditure")
    public ResponseEntity<?> calcSumExpenditure(@RequestParam Map<String, Object> query) {
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(accountBookService.getSumExpenditure(query));

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one for account book.
     * <p>
     * <b>Permission level => Manager</b>
     * <p>
     * <b>DELETE : API URL => /api/v1/account-book/one</b>
     *
     * @param id : UUID
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see AccountBookService#destroyOne
     */
    @PermissionRole
    @DeleteMapping("/one")
    public ResponseEntity<?> destroyOne(@RequestParam("id") UUID id) {
        Message message = new Message();

        try {
            accountBookService.destroyOne(id);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (Exception e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch( Delete or Remove ) expenditure-type for account book
     * <p>
     * <b>Permission level => Manager</b>
     * <p>
     * <b>PATCH : API URL => /api/v1/account-book/expenditure-type/{accountBookId}</b>
     *
     * @param accountBookId
     * @param expenditureTypeId
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see AccountBookService#patchOne
     */
    @PatchMapping("/{accountBookId}")
    public ResponseEntity<?> patchOne(@PathVariable(value = "accountBookId") UUID accountBookId, @RequestBody AccountBookDefDto accountBookDefDto) {
        Message message = new Message();
        accountBookService.patchOne(accountBookId, accountBookDefDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        return new ResponseEntity<>(message, message.getStatus());
    }
}
