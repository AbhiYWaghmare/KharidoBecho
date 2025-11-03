package com.spring.jwt.Mobile.entity;

import com.spring.jwt.entity.Buyer;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.Mobile.entity.Mobile; // adjust exact package if needed
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "mobile_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobileRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mobile_id", nullable = false)
    private Mobile mobile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    /**
     * JSON column storing an array of ConversationMessage objects.
     * Use columnDefinition = "json" on MySQL (or use TEXT/LONGTEXT if your DB doesn't support json typed column).
     */
//    @Lob
    @Column(name = "conversation", columnDefinition = "JSON")
    private String conversation; // JSON array string

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        if (this.status == null) this.status = RequestStatus.PENDING;
        if (this.conversation == null) this.conversation = "[]";
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

//    public enum RequestStatus {
//        PENDING,
//        IN_NEGOTIATION,
//        ACCEPTED,
//        REJECTED,
//        COMPLETED
//    }
}
