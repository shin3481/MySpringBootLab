package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    //등록
    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request){
        //Isbn이 중복되면 BusinessException 발생 시키고 종료
        bookRepository.findByIsbn(request.getIsbn())
                .ifPresent(book -> {throw new BusinessException("User with this Isbn already Exist", HttpStatus.CONFLICT);
                });
        //DTO => Entity로 변환
        Book book = request.toEntity();
        Book savedBook = bookRepository.save(book);
        //Entity => DTO로 변환후 리턴됨
        return BookDTO.BookResponse.from(savedBook);
    }
    //목록조회
    public List<BookDTO.BookResponse> getAllBooks(){
        return bookRepository.findAll()
                        .stream()
                        .map(BookDTO.BookResponse::from)
                        .toList();

    }
    //Id로 Book 조회하기
    public BookDTO.BookResponse getBookById(Long id){
        Book bookEntity = getBookExist(id);
        return BookDTO.BookResponse.from(bookEntity);
    }
    //Isbn로 Book 조회하기
    public BookDTO.BookResponse getBookByIsbn(String isbn){
        Book bookEntity = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book not found with ISBN: " + isbn, HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(bookEntity);
    }
    //Author로 Book 조회하기
    public ResponseEntity<List<BookDTO.BookResponse>> getBooksByAuthor(String author){
        return ResponseEntity.ok(
                bookRepository.findByAuthor(author)
                        .stream()
                        .map(BookDTO.BookResponse::from)
                        .toList()
        );

    }
    //Book 수정
    @Transactional
    public  BookDTO.BookResponse updateBook(Long id, BookDTO.BookCreateRequest request){
        Book existbook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));

        // 업데이트할 필드들 반영
        existbook.setTitle(request.getTitle());
        existbook.setAuthor(request.getAuthor());
        existbook.setPrice(request.getPrice());
        existbook.setPublishDate(request.getPublishDate());

        return BookDTO.BookResponse.from(existbook);
    }
    //Book 삭제
    @Transactional
    public void deleteBook(Long id){
        Book bookEntity = getBookExist(id);
        bookRepository.delete(bookEntity);
    }
    //내부 Helper Method
    private Book getBookExist(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }
}
