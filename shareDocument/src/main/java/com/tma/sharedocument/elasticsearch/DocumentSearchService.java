/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.elasticsearch;

import co.elastic.clients.elasticsearch._types.FieldValue;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.mapper.DocumentMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author ADMIN
 */
@Service
@RequiredArgsConstructor
public class DocumentSearchService {

    private final DocumentSearchRepository documentRepo;
    private final ElasticsearchOperations elasticsearchOperations;
    private final DocumentMapper documentMapper;

    public void index(DocumentSearch document) {
        documentRepo.save(document);
    }

    public void delete(Long id) {
        documentRepo.deleteById(id);
    }

    public Page<DocumentResponseDto> searchDocuments(String keyword, Long categoryId, List<Long> tagIds, Pageable pageable) {

        Criteria criteria = null;
        if (StringUtils.hasText(keyword)) {
            criteria = new Criteria("title").matches(keyword);
        }

        if (categoryId != null) {
            Criteria catCriteria = new Criteria("categoryId").is(categoryId);
            criteria = (criteria == null) ? catCriteria : criteria.and(catCriteria);
        }

        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                Criteria tagCriteria = new Criteria("tagIds").is(tagId);
                criteria = (criteria == null) ? tagCriteria : criteria.and(tagCriteria);
            }
        }

        Query query;
        if (criteria != null) {
            query = new CriteriaQuery(criteria).setPageable(pageable);
        } else {
            query = Query.findAll().setPageable(pageable);
        }

        SearchHits<DocumentSearch> searchHits = elasticsearchOperations.search(query, DocumentSearch.class);

        List<DocumentResponseDto> content = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(documentMapper::toDtoFromSearch)
                .toList();

        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }
}
