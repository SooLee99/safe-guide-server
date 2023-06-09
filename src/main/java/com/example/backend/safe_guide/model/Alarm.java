package com.example.backend.safe_guide.model;

import com.example.backend.safe_guide.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {
    private Integer idToken = null;

    private AlarmType alarmType;

    private AlarmArgs args;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public String getAlarmText() {
        return alarmType.getAlarmText();
    }

    public static Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(
                entity.getIdToken(),
                entity.getAlarmType(),
                entity.getArgs(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}
