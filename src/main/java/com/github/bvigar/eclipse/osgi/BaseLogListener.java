package com.github.bvigar.eclipse.osgi;

import java.io.PrintStream;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Description.
 * @author Trent Vigar
 */
public class BaseLogListener implements BundleActivator, LogListener {

    private final PrintStream printStream;

    public BaseLogListener() {
        printStream = System.err;
    }

    /**
     * {@inheritDoc}
     */
    public void logged(final LogEntry logEntry) {
        printStream.println(logEntry.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    public void start(final BundleContext context) throws Exception {
        final ServiceTracker logReaderTracker = new ServiceTracker(context, LogReaderService.class, null);
        logReaderTracker.open();
        final LogReaderService[] logReaders = (LogReaderService[]) logReaderTracker
                .getServices(new LogReaderService[] {});
        if (logReaders != null) {
            for (final LogReaderService logReader : logReaders) {
                logReader.addLogListener(this);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop(final BundleContext arg0) throws Exception {
        // no-op
    }
}
