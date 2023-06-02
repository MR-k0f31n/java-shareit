package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto createNewItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid
    @RequestBody ItemRequestInputDto request) {
        return service.createNewItemRequest(request, userId);
    }

    @GetMapping
    public List<ItemRequestWithAnswerDto> getAllByUserWitchAnswer(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getAllItemRequestByUserIdWithAnswer(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithAnswerDto> getAllRequestCreateOtherUser(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                                       @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getAllRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithAnswerDto getById(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getAllItemRequestFromOtherUsers(requestId, userId);
    }
}
