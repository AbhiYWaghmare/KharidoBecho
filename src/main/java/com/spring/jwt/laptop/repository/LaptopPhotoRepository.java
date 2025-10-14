    package com.spring.jwt.laptop.repository;

    import com.spring.jwt.laptop.entity.LaptopPhotos;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.List;

<<<<<<< HEAD
    public interface LaptopPhotoRepository extends JpaRepository<LaptopPhotos,Integer> {
        List<LaptopPhotos> findByLaptopId(int laptopId);
    }
=======
public interface LaptopPhotoRepository extends JpaRepository<LaptopPhotos,Long> {
}
>>>>>>> 673a8c976eeb96dac393d83c9c42b7c93658a550
