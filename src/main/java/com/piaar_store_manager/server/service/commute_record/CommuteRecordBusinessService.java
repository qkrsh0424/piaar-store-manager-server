package com.piaar_store_manager.server.service.commute_record;

import com.piaar_store_manager.server.exception.InvalidUserException;
import com.piaar_store_manager.server.exception.NotMatchedParamsException;
import com.piaar_store_manager.server.model.commute_record.CommuteRecordInterface;
import com.piaar_store_manager.server.model.commute_record.dto.CommuteRecordGetDto;
import com.piaar_store_manager.server.model.commute_record.entity.CommuteRecordEntity;
import com.piaar_store_manager.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommuteRecordBusinessService {
    private final UserService userService;
    private final CommuteRecordService commuteRecordService;

    @Autowired
    public CommuteRecordBusinessService(
            UserService userService,
            CommuteRecordService commuteRecordService
    ) {
        this.commuteRecordService = commuteRecordService;
        this.userService = userService;
    }

    public Object searchTodayRecordStrict() {
        if (!userService.isUserLogin()) {
            throw new InvalidUserException("로그인 상태를 체크해 주세요.");
        }
        UUID userId = userService.getUserId();

        Calendar startDateCal = Calendar.getInstance();
        Calendar endDateCal = Calendar.getInstance();

        startDateCal.add(Calendar.DATE, -1);

        Date startDate = startDateCal.getTime();
        Date endDate = endDateCal.getTime();

        List<CommuteRecordEntity> commuteRecordEntities = commuteRecordService.searchByUserIdAndDateRange(userId, startDate, endDate);
        List<CommuteRecordGetDto> commuteRecordGetDtos = commuteRecordEntities.stream().map(CommuteRecordGetDto::toDto).collect(Collectors.toList());
        return commuteRecordGetDtos.stream().findFirst().orElse(null);
    }

    public void setWorkStart(Map<String, Object> params) {
        if (!userService.isUserLogin()) {
            throw new InvalidUserException("로그인 상태를 체크해 주세요.");
        }

        Object idObj = params.get("id");
        UUID id = null;

        try {
            id = UUID.fromString(idObj.toString());
        } catch (IllegalStateException | NullPointerException e) {
            throw new NotMatchedParamsException("요청 데이터가 잘못되었습니다.");
        }

        Date currDate = Calendar.getInstance().getTime();

        CommuteRecordEntity commuteRecordEntity = commuteRecordService.searchById(id)
                .map(entity -> {
                    entity.setWorkStartDate(currDate);
                    entity.setStatus(CommuteRecordInterface.STATUS_NORMAL);
                    return entity;
                })
                .orElseThrow(() -> new NotMatchedParamsException("요청 데이터가 잘못되었습니다."));
        commuteRecordService.createAndModify(commuteRecordEntity);
    }

    public void setWorkEnd(Map<String, Object> params) {
        if (!userService.isUserLogin()) {
            throw new InvalidUserException("로그인 상태를 체크해 주세요.");
        }

        Object idObj = params.get("id");
        UUID id = null;

        try {
            id = UUID.fromString(idObj.toString());
        } catch (IllegalStateException | NullPointerException e) {
            throw new NotMatchedParamsException("요청 데이터가 잘못되었습니다.");
        }

        Date currDate = Calendar.getInstance().getTime();

        CommuteRecordEntity commuteRecordEntity = commuteRecordService.searchById(id)
                .map(entity -> {
                    entity.setWorkEndDate(currDate);
                    return entity;
                })
                .orElseThrow(() -> new NotMatchedParamsException("요청 데이터가 잘못되었습니다."));
        commuteRecordService.createAndModify(commuteRecordEntity);
    }
}
