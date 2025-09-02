package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.PublisherDTO;
import com.rookies4.myspringbootlab.entity.Publisher;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.exception.ErrorCode;
import com.rookies4.myspringbootlab.repository.BookRepository;
import com.rookies4.myspringbootlab.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    //모든 출판사를 조회하며, 각 출판사의 도서 수를 포함
    public List<PublisherDTO.SimpleResponse> getAllPublishers(){
        return publisherRepository.findAll()
                .stream()
                .map(publisher ->
                        PublisherDTO.SimpleResponse.fromEntityWithCount(
                                publisher,
                                (long) publisher.getBooks().size()
                        )
                )
                .toList();
    }
    //ID로 특정 출판사를 조회하며, 해당 출판사의 모든 도서 정보를 포함
    public PublisherDTO.Response getPublisherById(Long id){
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Pulisher", "id", id));
        return PublisherDTO.Response.fromEntity(publisher);
    }
    //이름으로 특정 출판사를 조회
    public PublisherDTO.Response getPublisherByName(String name){
        Publisher publisher = publisherRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Pulisher", "name", name));
        return PublisherDTO.Response.fromEntity(publisher);
    }
    //새로운 출판사를 생성합니다. 이름 중복을 검증합니다
    @Transactional
    public PublisherDTO.Response createPublisher(PublisherDTO.Request request){
        // Validate publisher Name is not already in use
        if(publisherRepository.existsByName(request.getName())){
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE,
                    request.getName());
        }
        // Create publisher entity
        Publisher publisher = Publisher.builder()
                .name(request.getName())
                .establishedDate(request.getEstablishedDate())
                .address(request.getAddress())
                .build();
        // Save and return the publisher
        Publisher savedPublisher = publisherRepository.save(publisher);
        return PublisherDTO.Response.fromEntity(savedPublisher);

    }
    //기존 출판사 정보를 수정합니다. 이름 중복(자신 제외)을 검증합니다.
    @Transactional
    public PublisherDTO.Response updatePublisher(Long id, PublisherDTO.Request request){
        // Find the publisher
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Publisher", "id", id));
        // Check if another publisher already has the name
        if(!publisher.getName().equals(request.getName()) &&
        publisherRepository.existsByName(request.getName())){
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE,
                    request.getName());
        }
        // Update publisher info
        publisher.setName(request.getName());

        // Save and return updated publisher
        Publisher updatedPublisher = publisherRepository.save(publisher);
        return PublisherDTO.Response.fromEntity(updatedPublisher);
    }
    //출판사를 삭제합니다. 해당 출판사에 도서가 있는 경우 삭제를 거부합니다.
    @Transactional
    public void deletePublisher(Long id){
        if (!publisherRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                    "Publisher", "id", id);
        }
        // Check if publisher has books
        Long bookCount = bookRepository.countByPublisherId(id);
        if (bookCount > 0) {
            throw new BusinessException(ErrorCode.PUBLISHER_HAS_BOOKS,
                    id, bookCount);
        }
        publisherRepository.deleteById(id);
    }
}
