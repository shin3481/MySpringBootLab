package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.controller.dto.PublisherDTO;
import com.rookies4.myspringbootlab.entity.Publisher;
import com.rookies4.myspringbootlab.service.BookService;
import com.rookies4.myspringbootlab.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<PublisherDTO.Response> createPublisher(
            @RequestBody @Valid PublisherDTO.Request request) {
        PublisherDTO.Response createdPublisher = publisherService.createPublisher(request);
        return ResponseEntity.ok(createdPublisher);
    }

    @GetMapping
    public ResponseEntity<List<PublisherDTO.SimpleResponse>> getAllPublishers() {
        List<PublisherDTO.SimpleResponse> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok(publishers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> getPublisherById(@PathVariable Long id) {
        PublisherDTO.Response publisher = publisherService.getPublisherById(id);
        return ResponseEntity.ok(publisher);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<PublisherDTO.Response> getPublisherByName(@PathVariable String name) {
        PublisherDTO.Response publisher = publisherService.getPublisherByName(name);
        return ResponseEntity.ok(publisher);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO.Response>> getBooksByPublisherId(@PathVariable Long id) {
        List<BookDTO.Response> books = bookService.getBooksByPublisherId(id);
        return ResponseEntity.ok(books);
    }

}
