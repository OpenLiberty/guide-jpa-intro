# Integration to Instana with mpmetrics

This is a custom demo on top of jpa guide to showcase how it is possible to pull business metrics from database into Instana via Prometheus scraping

## The code that provides the integration

```
package io.openliberty.guides.event.resources;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.opentracing.Traced;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@ApplicationScoped
public class EventMetrics {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    private final Random rand = new Random();

    static final Logger LOG = Logger.getLogger(EventMetrics.class.getName());

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        LOG.log(Level.INFO, "Init EventMetrics for mpMetrics in ApplicationScoped @Observes");
    }

    @Gauge(unit = MetricUnits.NONE, name = "duitnowtransfer", absolute = true, description = "Number of duitnow transfer")
    public int getDuitNowTransfer() {
        /**
         * Randomized count just to demo simulation of duit now transfer
         */
        int low = 10;
        int high = 100;
        return rand.nextInt(high - low) + low;
    }

    @Gauge(unit = MetricUnits.NONE, name = "currenteventcount", absolute = true, description = "Number of events")
    @Traced
    @Counted(name = "eventmethodinvocationcount", absolute = true, description = "event method invocation count")
    public int getEventsCount() {
        int test = -1;
        try {
            Query q = em.createNativeQuery("select count(*) from event");
            test = ((Integer) q.getSingleResult()).intValue();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "unable to retrieve count from database", e);
        }
        return test;
    }
}
```

https://github.com/OpenLiberty/guide-jpa-intro/blob/2dead0ad3203516842a36708f029e966e2d5dc1c/finish/backendServices/src/main/java/io/openliberty/guides/event/resources/EventMetrics.java#L1-L57

## Screenshots of Instana 

<img width="1624" alt="image" src="https://user-images.githubusercontent.com/532945/234555038-001e52c7-dee1-4608-a195-cb6dc4a14333.png">





