        package com.spring.jwt.Bike.Entity;
        import com.fasterxml.jackson.annotation.JsonBackReference;
        import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        @Entity
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Table(name = "bikeImage")
        @Builder
        public class BikeImage {

            @Id
            @Column(name = "bike_image_id" ,nullable = false)
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private int imageId;



            @Column(name = "imageLink",nullable = false)
            private String image_link;


            @Column(name = "public_id",nullable = false)
            private String publicId;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "bike_id",nullable = false)
            @JsonBackReference
            private Bike bike;
        }

