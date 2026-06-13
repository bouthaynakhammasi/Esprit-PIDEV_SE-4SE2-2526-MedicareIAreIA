package com.aziz.demosec.repository;

import com.aziz.demosec.Entities.Review; // Adjust if your Review entity is in another package
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.patient p WHERE r.doctor.id = :doctorId ORDER BY r.createdAt DESC")
    List<Review> findByDoctorIdWithPatient(@Param("doctorId") Long doctorId);

    // hethy bch ywali yaaml calcule direct fbase w tafficher juste resultat
    // Calcul direct : Nombre total d'avis et Moyenne
    @Query("SELECT COUNT(r), AVG(r.rating) FROM Review r WHERE r.doctor.id = :doctorId")
    Object[] getReviewStats(@Param("doctorId") Long doctorId);

    // Calcul direct : Répartition des notes (GROUP BY)
    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.doctor.id = :doctorId GROUP BY r.rating ORDER BY r.rating DESC")
    List<Object[]> getRatingBreakdown(@Param("doctorId") Long doctorId);
}
