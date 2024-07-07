package com.projet3.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name="rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    private Double surface;


    private Double price;


    private String picture;


    @Column(length = 2000)
    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "rental")
    private List<Message> messages;
}
