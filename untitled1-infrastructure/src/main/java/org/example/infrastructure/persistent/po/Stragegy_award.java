package org.example.infrastructure.persistent.po;

import java.math.BigDecimal;
import java.util.Date;

public class Stragegy_award {
    long id;
    long strategy_id;
    int award_id;
    String award_title;
    String award_subtitle;
    int award_count;
    int award_count_surplus;
    BigDecimal award_rate;
    String rule_models;
    int sort;
    Date create_time;
    Date update_time;
}
