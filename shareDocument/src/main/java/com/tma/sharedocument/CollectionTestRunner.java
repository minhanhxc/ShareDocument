/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument;

import com.tma.sharedocument.dto.CollectionRequestDto;
import com.tma.sharedocument.dto.DocumentDetailResponseDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.pojo.Collection;
import com.tma.sharedocument.service.CollectionService;
import com.tma.sharedocument.service.DocumentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 *
 * @author ADMIN
 */
@Component
public class CollectionTestRunner implements CommandLineRunner {

    private final CollectionService collectionService;

    public CollectionTestRunner(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n============= BẮT ĐẦU TEST MODULE COLLECTION =============");

        String username = "mannt"; // User hợp lệ có trong DB
        Long documentId = 2L;      // Document hợp lệ có trong DB
        Long newCollectionId = null;

        // --- KỊCH BẢN 1: TẠO MỚI ---
        try {
            System.out.println("\n[1] Test Tạo Collection...");
            CollectionRequestDto dto = new CollectionRequestDto("Tài liệu ôn thi cuối kỳ");
            Collection newCollection = collectionService.createCollection(dto, username);
            newCollectionId = newCollection.getId();
            System.out.println("-> THÀNH CÔNG: Đã tạo thư mục với ID = " + newCollectionId);
        } catch (Exception e) {
            System.out.println("-> LỖI: " + e.getMessage());
        }

        // --- KỊCH BẢN 2: THÊM TÀI LIỆU ---
        try {
            System.out.println("\n[2] Test Thêm Document vào Collection...");
            collectionService.addDocument(newCollectionId, documentId, username);
            System.out.println("-> THÀNH CÔNG: Đã thêm tài liệu vào thư mục!");
        } catch (Exception e) {
            System.out.println("-> LỖI: " + e.getMessage());
        }

        // --- KỊCH BẢN 3: THÊM TÀI LIỆU (CỐ TÌNH LÀM TRÙNG) ---
        try {
            System.out.println("\n[3] Test Thêm TRÙNG Document...");
            collectionService.addDocument(newCollectionId, documentId, username);
            System.out.println("-> LỖI: Lẽ ra phải chặn trùng lặp nhưng lại chạy thành công!");
        } catch (Exception e) {
            System.out.println("-> THÀNH CÔNG chặn lỗi: " + e.getMessage());
        }

        // --- KỊCH BẢN 4: GỠ TÀI LIỆU ---
        try {
            System.out.println("\n[4] Test Gỡ Document khỏi Collection...");
            collectionService.removeDocument(newCollectionId, documentId, username);
            System.out.println("-> THÀNH CÔNG: Đã gỡ tài liệu khỏi thư mục!");
        } catch (Exception e) {
            System.out.println("-> LỖI: " + e.getMessage());
        }

        // --- KỊCH BẢN 5: XÓA COLLECTION ---
        try {
            System.out.println("\n[5] Test Xóa Collection...");
            collectionService.deleteCollection(newCollectionId, username);
            System.out.println("-> THÀNH CÔNG: Đã xóa thư mục vĩnh viễn!");
        } catch (Exception e) {
            System.out.println("-> LỖI: " + e.getMessage());
        }

        System.out.println("\n============= KẾT THÚC TEST =============");
    }
}
