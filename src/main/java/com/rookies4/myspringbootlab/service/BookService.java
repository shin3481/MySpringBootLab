package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.entity.BookDetail;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.exception.ErrorCode;
import com.rookies4.myspringbootlab.repository.BookRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    //등록
    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request){
        //Isbn이 중복되면 BusinessException 발생 시키고 종료
        if(bookRepository.existsByIsbn(request.getIsbn())){
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE,request.getIsbn());
        }
        // DTO -> Entity 변환
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();
        if(request.getDetailRequest() != null){
            BookDetail bookDetailEntity = BookDetail.builder()
                    .description(request.getDetailRequest().getDescription())
                    .language(request.getDetailRequest().getLanguage())
                    .pageCount(request.getDetailRequest().getPageCount())
                    .publisher(request.getDetailRequest().getPublisher())
                    .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                    .edition(request.getDetailRequest().getEdition())
                    .book(book)
                    .build();
            book.setBookDetail(bookDetailEntity);

        }
        Book savedBook = bookRepository.save(book);
        //Entity => DTO로 변환후 리턴됨
        return BookDTO.Response.fromEntity(savedBook);
    }
    //목록조회
    public List<BookDTO.Response> getAllBooks(){
        return bookRepository.findAll()
                        .stream()
                        .map(BookDTO.Response::fromEntity)
                        .toList();

    }
    //Id로 Book 조회하기
    public BookDTO.Response getBookById(Long id){
        Book bookEntity = getBookExist(id);
        return BookDTO.Response.fromEntity(bookEntity);
    }
    //Isbn로 Book 조회하기
    public BookDTO.Response getBookByIsbn(String isbn){
        Book bookEntity = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,"Book","Isbn",isbn));
        return BookDTO.Response.fromEntity(bookEntity);
    }
    //Author로 Book 조회하기
    public List<BookDTO.Response> getBooksByAuthor(String author){
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                        .stream()
                        .map(BookDTO.Response::fromEntity)
                        .toList();

    }
    //Title로 Book 조회하기
    public List<BookDTO.Response> getBooksByTitle(String title){
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }
    //Book 전체 수정
    @Transactional
    public  BookDTO.Response updateBook(Long id, BookDTO.Request request){
        Book existbook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,"Book","Id",id));

        // 업데이트할 필드들 반영
        existbook.setTitle(request.getTitle());
        existbook.setAuthor(request.getAuthor());
        existbook.setPrice(request.getPrice());
        existbook.setPublishDate(request.getPublishDate());

        return BookDTO.Response.fromEntity(existbook);
    }
    // ------------------------------
    // Book + BookDetail PATCH (통합)
    // -----
    @Transactional
    public BookDTO.Response patchBook(Long id, BookDTO.PatchRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,"Book","Id",id));

        // ISBN 중복 체크
        if (request.getIsbn() != null && !book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        // Book 기본 정보 PATCH
        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getIsbn() != null) book.setIsbn(request.getIsbn());
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getPublishDate() != null) book.setPublishDate(request.getPublishDate());

        // BookDetail PATCH
        if (request.getDetailRequest() != null) {
            BookDetail bookDetail = book.getBookDetail();
            if (bookDetail == null) {
                bookDetail = new BookDetail();
                bookDetail.setBook(book);
                book.setBookDetail(bookDetail);
            }
            updateBookDetail(bookDetail, request.getDetailRequest());
        }

        Book updatedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(updatedBook);
    }

    // ------------------------------
    // BookDetail PATCH 전용
    // ------------------------------
    @Transactional
    public BookDTO.Response patchBookDetail(Long id, BookDTO.BookDetailPatchRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,"Book","Id",id));

        BookDetail bookDetail = book.getBookDetail();
        if (bookDetail == null) {
            bookDetail = new BookDetail();
            bookDetail.setBook(book);
            book.setBookDetail(bookDetail);
        }

        updateBookDetail(bookDetail, request);

        Book updatedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(updatedBook);
    }

    // ------------------------------
    // BookDetail PATCH 공통 처리
    // ------------------------------
    private void updateBookDetail(BookDetail bookDetail, BookDTO.BookDetailPatchRequest request) {
        bookDetail.setDescription(safeString(request.getDescription(), bookDetail.getDescription()));
        bookDetail.setLanguage(safeString(request.getLanguage(), bookDetail.getLanguage()));
        bookDetail.setPublisher(safeString(request.getPublisher(), bookDetail.getPublisher()));
        bookDetail.setCoverImageUrl(safeString(request.getCoverImageUrl(), bookDetail.getCoverImageUrl()));
        bookDetail.setEdition(safeString(request.getEdition(), bookDetail.getEdition()));
        bookDetail.setPageCount(safeInteger(request.getPageCount(), bookDetail.getPageCount()));
    }
    private String safeString(String requestValue, String existingValue) {
        return requestValue != null ? requestValue : (existingValue != null ? existingValue : "");
    }

    private Integer safeInteger(Integer requestValue, Integer existingValue) {
        return requestValue != null ? requestValue : (existingValue != null ? existingValue : 0);
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
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,"Book","Id",id));
    }
}
