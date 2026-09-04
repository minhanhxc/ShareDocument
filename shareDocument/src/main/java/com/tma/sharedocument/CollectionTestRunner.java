///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.tma.sharedocument;
//
//import com.tma.sharedocument.dto.CollectionDetailResponseDto;
//import com.tma.sharedocument.dto.CollectionRequestDto;
//import com.tma.sharedocument.dto.CollectionResponseDto;
//import com.tma.sharedocument.dto.DocumentDetailResponseDto;
//import com.tma.sharedocument.dto.DocumentResponseDto;
//import com.tma.sharedocument.pojo.Collection;
//import com.tma.sharedocument.service.CollectionService;
//import com.tma.sharedocument.service.DocumentService;
//import java.util.List;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Component;
//
///**
// *
// * @author ADMIN
// */
//@Component
//public class CollectionTestRunner implements CommandLineRunner {
//
//    private final CollectionService collectionService;
//
//    public CollectionTestRunner(CollectionService collectionService) {
//        this.collectionService = collectionService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("\n============= BẮT ĐẦU TEST TOÀN DIỆN MODULE COLLECTION =============");
//
//        // THAY ĐỔI 2 BIẾN NÀY CHO KHỚP VỚI DATABASE CỦA BẠN
//        String username = "mannt"; 
//        Long documentId = 2L;      
//        
//        Long newCollectionId = null;
//
//        // ==========================================
//        // [1] TẠO MỚI MỤC YÊU THÍCH (CREATE)
//        // ==========================================
//        try {
//            System.out.println("\n[1] Test Tạo Collection...");
//            CollectionRequestDto dto = new CollectionRequestDto();
//            dto.setName("Thư mục Test Tích Hợp Đồ Án"); // Giả sử bạn dùng @Data của Lombok
//            
//            CollectionResponseDto createdCollection = collectionService.createCollection(dto, username);
//            newCollectionId = createdCollection.getId();
//            System.out.println("-> THÀNH CÔNG: Đã tạo thư mục với ID = " + newCollectionId);
//        } catch (Exception e) {
//            System.out.println("-> LỖI: " + e.getMessage());
//            return; // Dừng test nếu không tạo được thư mục
//        }
//
//        // ==========================================
//        // [2] THÊM TÀI LIỆU VÀO MỤC YÊU THÍCH (ADD)
//        // ==========================================
//        try {
//            System.out.println("\n[2] Test Thêm Document vào Collection...");
//            collectionService.addDocument(newCollectionId, documentId, username);
//            System.out.println("-> THÀNH CÔNG: Đã thêm tài liệu vào thư mục!");
//        } catch (Exception e) {
//            System.out.println("-> LỖI: " + e.getMessage());
//        }
//
//        // ==========================================
//        // [3] CỐ TÌNH THÊM TRÙNG TÀI LIỆU (NEGATIVE TEST)
//        // ==========================================
//        try {
//            System.out.println("\n[3] Test Thêm TRÙNG Document...");
//            collectionService.addDocument(newCollectionId, documentId, username);
//            System.out.println("-> LỖI BUG: Lẽ ra phải chặn trùng lặp nhưng lại chạy thành công!");
//        } catch (Exception e) {
//            System.out.println("-> THÀNH CÔNG chặn lỗi: " + e.getMessage());
//        }
//
//        // ==========================================
//        // [4] LẤY DANH SÁCH MỤC YÊU THÍCH (LIST)
//        // ==========================================
//        try {
//            System.out.println("\n[4] Test Lấy Danh sách Collection...");
//            List<CollectionResponseDto> list = collectionService.listCollection(username);
//            System.out.println("-> THÀNH CÔNG: Tìm thấy " + list.size() + " thư mục của user.");
//            list.forEach(c -> 
//                System.out.println("   + Thư mục ID: " + c.getId() + " | Tên: " + c.getName())
//            );
//        } catch (Exception e) {
//            System.out.println("-> LỖI: " + e.getMessage());
//        }
//
//        // ==========================================
//        // [5] LẤY CHI TIẾT MỤC YÊU THÍCH (DETAIL)
//        // ==========================================
//        try {
//            System.out.println("\n[5] Test Lấy Chi tiết Collection...");
//            CollectionDetailResponseDto detail = collectionService.detailCollection(newCollectionId, username);
//            System.out.println("-> THÀNH CÔNG: Thông tin Thư mục: " + detail.getName());
//            
//            if (detail.getDocuments() != null && !detail.getDocuments().isEmpty()) {
//                System.out.println("-> Các tài liệu bên trong:");
//                detail.getDocuments().forEach(doc -> 
//                    System.out.println("   - Doc ID: " + doc.getId() + " | Tiêu đề: " + doc.getTitle())
//                );
//            } else {
//                System.out.println("-> Thư mục trống.");
//            }
//        } catch (Exception e) {
//            System.out.println("-> LỖI: " + e.getMessage());
//        }
//
//        // ==========================================
//        // [6] GỠ TÀI LIỆU KHỎI MỤC YÊU THÍCH (REMOVE)
//        // ==========================================
//        try {
//            System.out.println("\n[6] Test Gỡ Document khỏi Collection...");
//            collectionService.removeDocument(newCollectionId, documentId, username);
//            System.out.println("-> THÀNH CÔNG: Đã gỡ tài liệu khỏi thư mục!");
//        } catch (Exception e) {
//            System.out.println("-> LỖI: " + e.getMessage());
//        }
//
//        // ==========================================
//        // [7] XÓA MỤC YÊU THÍCH (DELETE)
//        // ==========================================
//        try {
//            System.out.println("\n[7] Test Xóa Collection...");
//            collectionService.deleteCollection(newCollectionId, username);
//            System.out.println("-> THÀNH CÔNG: Đã xóa thư mục vĩnh viễn!");
//        } catch (Exception e) {
//            System.out.println("-> LỖI: " + e.getMessage());
//        }
//
//        System.out.println("\n============= KẾT THÚC TEST =============");
//    }
//}
