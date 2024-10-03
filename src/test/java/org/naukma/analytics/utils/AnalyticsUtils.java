package org.naukma.analytics.utils;

import org.naukma.analytics.analytics.AnalyticsDto;
import org.naukma.analytics.analytics.AnalyticsEntity;

import java.time.LocalDate;
import java.util.Date;

public class AnalyticsUtils {

    public static AnalyticsEntity getRandomAnalyticsEntity(LocalDate date) {
        return new AnalyticsEntity(Common.getRandomLong(1, 100000), date, Common.getRandomLong(1, 100000), Common.getRandomLong(1, 100000), Common.getRandomLong(1, 100000), Common.getRandomLong(1, 100000));
    }

    public static AnalyticsDto getRandomAnalyticsDto() {
        return new AnalyticsDto(Common.getRandomLong(1, 100000),new Date(), Common.getRandomLong(1, 100000), Common.getRandomLong(1, 100000), Common.getRandomLong(1, 100000), Common.getRandomLong(1, 100000));
    }
}
