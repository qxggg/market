package org.example.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.entity.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartakeOrderAggregate {
    private String userId;
    private Long activityId;
    private ActivityAccountEntity activityAccount;
    private ActivityMonthCountEntity activityMonthCount;
    private ActivityDayCountEntity activityDayCount;
    private UserRaffleOrderEntity userRaffleOrder;
    private boolean isExistDayCount = true;
    private boolean isExistMonthCount = true;
}
