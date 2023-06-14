package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ItemRequestDto createNewItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @Valid @RequestBody ItemRequestInputDto request) {
        return service.createNewItemRequest(request, userId);
    }

    @GetMapping
    public List<ItemRequestWithAnswerDto> getAllByUserWitchAnswer(@RequestParam Integer from,
                                                                  @RequestParam Integer size,
                                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        final Pageable pageable = PageRequest.of(from / size, size);
        return service.getAllItemRequestByUserIdWithAnswer(userId, pageable);
    }

    @GetMapping("/all")
    public List<ItemRequestWithAnswerDto> getAllRequestCreateOtherUser(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                                       @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        final Pageable pageable = PageRequest.of(from / size, size);
        return service.getAllRequest(userId, pageable);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithAnswerDto getById(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getAllItemRequestFromOtherUsers(requestId, userId);
    }
}
