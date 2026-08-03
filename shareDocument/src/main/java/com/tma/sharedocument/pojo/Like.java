/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.pojo;

/**
 *
 * @author Minh Anh
 */
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "like", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "document_id"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
