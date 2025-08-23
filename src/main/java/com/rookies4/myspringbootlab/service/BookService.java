package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookDetailRepository;
import com.rookies4.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    public List<BookDTO.BookResponse> getAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(BookDTO.BookResponse::from)
                .toList();
    }
    public BookDTO.BookResponse getBookById(Long id){
        get
    }

    public BookDTO.BookResponse getBookByIsbn(String isbn){

    }

    public List<BookDTO.BookResponse> getBooksByAuthor(String author){

    }
    public List<BookDTO.BookResponse> getBooksByTitle(String title){

    }
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request){
        bookRepository.findByIsbn(request.getIsbn())
                .ifPresent(entity->{
                    throw new BusinessException("Book with this Isbn already Exist",HttpStatus.CONFLICT);

                });

        Book entity = request.toEntity();
        Book savedEntity = bookRepository.save(entity);
        return new BookDTO.BookResponse(savedEntity);
    }
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request){
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        existBook.setTitle(request.getTitle());
        return new BookDTO.BookResponse(existBook);
    }
    public void deleteBook(Long id){
        Book bookEntity = getBookExist(id);
        bookRepository.delete(bookEntity);
    }
    //내부 Helper Method
    private Book getBookExist(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
    }
}
