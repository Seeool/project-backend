package com.example.projectbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;
    @ManyToOne
    private Member member;
    @ManyToOne
    private Product product;
    private String name;
    private String phone;
    private String address;
    private int Qty;
}
