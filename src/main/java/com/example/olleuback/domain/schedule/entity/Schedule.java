package com.example.olleuback.domain.schedule.entity;

import com.example.olleuback.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy = "schedule")
    private List<Participate> participates = new ArrayList<>();

    public void addParticipate(Participate participate) {
        this.participates.add(participate);
    }

    public static Schedule ofCreate(String title, String locationName, Double latitude, Double longitude,
                    LocalDateTime meetingDate, String description, User user) {
        Schedule schedule = new Schedule();
        schedule.title = title;
        schedule.locationName = locationName;
        schedule.latitude = latitude;
        schedule.longitude = longitude;
        schedule.meetingDate = meetingDate;
        schedule.description = description;
        schedule.user = user;
        return schedule;
    }
}
