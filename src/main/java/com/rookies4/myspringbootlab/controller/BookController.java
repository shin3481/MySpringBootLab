package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import com.rookies4.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    
    // 모든 도서 조회
    @GetMapping
    public ResponseEntity<List<BookDTO.BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    
    // ID로 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> getBookById(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        //map(Function) T -> R
        return optionalBook.map(book -> ResponseEntity.ok(book))
                //.map(ResponseEntity::ok)
                //.orElse(ResponseEntity.notFound().build()); //Body가 없고, 404 status code만 반환됨
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }
    
    // ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }
    
    // 저자명으로 도서 조회
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {

        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }
    // 제목으로 도서 조회
    @GetMapping("/title/{title}")
    public List<Book> getBooksByTitle(@PathVariable String title) {

        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }
    
    // 도서 등록
    @PostMapping
    public ResponseEntity<BookDTO.BookResponse> createBook(@Valid @RequestBody BookDTO.BookCreateRequest request) {
        Book savedBook = bookService
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED); //CREATED 201
    }
    
    // 도서 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        //가격만 변경
        existBook.setPrice(bookDetail.getPrice());

        Book updatedBook = bookRepository.save(existBook);
        return ResponseEntity.ok(updatedBook);
    }
    
    // 도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        //매칭돠는 Book 이 없으면
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); //body는 없고, 404 status code만 반환
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build(); ////body는 없고, 204 status code만 반환
    }
}