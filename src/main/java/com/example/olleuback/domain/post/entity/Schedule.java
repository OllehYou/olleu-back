package com.example.olleuback.domain.post.entity;

import com.example.olleuback.domain.participate.entity.Participate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "location_name")
    private String locationName;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "meeting_date")
    private LocalDateTime meetingDate;
    @Lob
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "schedule")
    private List<Participate> participates = new ArrayList<>();
}
