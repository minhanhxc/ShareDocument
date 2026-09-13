/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.elasticsearch;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Document(indexName = "documents")
@Data
public class DocumentSearch {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Keyword)
    private String thumbnail;

    @Field(type = FieldType.Long)
    private Long uploaderId;

    @Field(type = FieldType.Keyword)
    private String uploaderName;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "standard"),
            otherFields = {
                @InnerField(suffix = "keyword", type = FieldType.Keyword)}
    )
    private String categoryName;

    @Field(type = FieldType.Long)
    private List<Long> tagIds;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "standard"),
            otherFields = {
                @InnerField(suffix = "keyword", type = FieldType.Keyword)}
    )
    private List<String> tagNames;

    @Field(type = FieldType.Long)
    private Long totalView;

    @Field(type = FieldType.Long)
    private Long totalLike;

    @Field(type = FieldType.Date, pattern = "uuuu-MM-dd")
    private LocalDate createdAt;
}
