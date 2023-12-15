package com.example.springboot;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationMetrics {
    private static final StatsDClient statsd = new NonBlockingStatsDClient("csye6225", "localhost", 8125);
    public void addCount(String metricName) {
        statsd.incrementCounter(metricName);
//        statsd.recordGaugeValue(metricName, 100);
//        statsd.recordExecutionTime(metricName, 25);
//        statsd.recordSetEvent(metricName, "one");
    }

}
