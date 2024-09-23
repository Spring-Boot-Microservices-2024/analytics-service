package org.naukma.analytics.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalyticsEvent {
    private AnalyticsEventType type;
}
