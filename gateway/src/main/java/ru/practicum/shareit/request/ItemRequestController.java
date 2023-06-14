package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author MR.k0F31N
 */

@Controller
@RequestMapping("/requests")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createNewRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @Valid @RequestBody ItemRequestRequestDto request) {
        log.info("Create new item request {}, user id={}", request, userId);
        return itemRequestClient.createNewRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByOwner(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get all collection request from owner id={}, witch answer, Page param. from ={}, size={}", userId, from, size);
        return itemRequestClient.getOwner(from, size, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllFromUser(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get all collection request from other users id={}, witch answer, Page param. from ={}, size={}", userId, from, size);
        return itemRequestClient.getOtherUser(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable long requestId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get request by id={} witch answer, user id ={}", requestId, userId);
        return itemRequestClient.getById(requestId, userId);
    }
}
