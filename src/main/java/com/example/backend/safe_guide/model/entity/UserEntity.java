package com.example.backend.safe_guide.model.entity;

import com.example.backend.safe_guide.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;


@Setter
@Getter
@Entity
@Table(name = "\"user\"")
@SQLDelete(sql = "UPDATE \"user\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token", unique = true)
    private Integer idToken = null;

    @Column(name = "user_id", unique = true)
    private String userId;

    private String password;
    @Column(name = "user_name", unique = true)
    private String userName;
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
    private String birth;
    private String gender;
    private String address;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;


    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static UserEntity of(String userId, String encodedPwd, String userName,
                                String phoneNumber, String birth, String gender, String address) {
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);
        entity.setPassword(encodedPwd);
        entity.setUserName(userName);
        entity.setPhoneNumber(phoneNumber);
        entity.setBirth(birth);
        entity.setGender(gender);
        entity.setAddress(address);
        return entity;
    }
}
