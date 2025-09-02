package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookDetailController {
    private final BookService bookService;

    // 도서 등록
    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(@Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response response = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 모든 도서 조회
    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks() {

        return ResponseEntity.ok(bookService.getAllBooks());
    }
    
    // ID로 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        BookDTO.Response bookById = bookService.getBookById(id);
        return ResponseEntity.ok(bookById);

    }
    
    // ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(@PathVariable String isbn) {
        BookDTO.Response bookByIsbn = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(bookByIsbn);
    }
    
    // 저자명으로 도서 조회
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.Response>> getBooksByAuthor(@RequestParam String author) {
        List<BookDTO.Response> booksByAuthor = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok(booksByAuthor);
    }
    //제목으로 도서조회
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.Response>> getBooksByTitle(@RequestParam String title){
        List<BookDTO.Response> booksByTitle = bookService.getBooksByTitle(title);
        return ResponseEntity.ok(booksByTitle);
    }

    // 도서 정보 전체수정
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO.Request request) {
        return ResponseEntity.ok(bookService.updateBook(id,request));
    }
    // 도서 정보 일부 수정 (Book 자체 일부 수정)
    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.Response> patchBook(
            @PathVariable Long id,
            @RequestBody BookDTO.PatchRequest request) {


        return ResponseEntity.ok(bookService.patchBook(id, request));
    }
    // 도서 상세 정보 일부 수정 (BookDetail 일부 수정)
    @PatchMapping("/{id}/detail")
    public ResponseEntity<BookDTO.Response> patchBookDetail(@PathVariable Long id, @RequestBody BookDTO.BookDetailPatchRequest  request){
        BookDTO.Response updateDetail = bookService.patchBookDetail(id, request);
        return ResponseEntity.ok(updateDetail);
    }
    
    // 도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}