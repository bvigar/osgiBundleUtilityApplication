package com.github.bvigar.eclipse.osgi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import com.github.bvigar.eclipse.osgi.model.PackageProviderBundleStore;

/**
 * Description.
 * @author Trent Vigar
 */
public class OsgiBundleUtilityApplication implements BundleActivator {

    private LogService logservice;
    private BundleActivator[] activators;
    private final Map<String, Set<String>> bundlesByPackage = new HashMap<String, Set<String>>();

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void start(final BundleContext context) throws Exception {
        activators = getBundleActivators(context);
        for (int i = 0; i < activators.length; i++) {
            if (activators[i] != null) {
                activators[i].start(context);
            }
        }

        final ServiceTracker logServiceTracker = new ServiceTracker(context, org.osgi.service.log.LogService.class,
                null);
        logServiceTracker.open();
        logservice = (LogService) logServiceTracker.getService();

        if (logservice != null) {
            //            final Bundle systemBundle = Platform.getBundle("system.bundle"); //$NON-NLS-1$
            // final FrameworkWiring frameworkWiring = systemBundle.adapt(FrameworkWiring.class);
            // final Collection<Bundle> frameworkBundles = frameworkWiring.getDependencyClosure(Collections
            // .<Bundle> singletonList(systemBundle));
            final List<Bundle> frameworkBundles = Arrays.<Bundle> asList(context.getBundles());
            for (final Bundle bundle : frameworkBundles) {
                final Dictionary<String, String> headers = bundle.getHeaders();
                if (headers.get("Export-Package") != null) { //$NON-NLS-1$
                    final String manifestExportPackageEntry = headers.get("Export-Package"); //$NON-NLS-1$
                    // if there are multiple entries, separate them into a list, otherwise, just put the only entry
                    // in the list
                    final List<String> exportedPackages = manifestExportPackageEntry.contains(",") ? extractManifestEntryValues(manifestExportPackageEntry) : Arrays.<String> asList(new String[] { manifestExportPackageEntry }); //$NON-NLS-1$
                    for (final String exportedPackage : exportedPackages) {
                        if (bundlesByPackage.get(exportedPackage) == null) {
                            final Set<String> bundlesProvidingPackage = new HashSet<String>();
                            bundlesProvidingPackage.add(bundle.getSymbolicName());
                            bundlesByPackage.put(exportedPackage, bundlesProvidingPackage);
                            continue;
                        }
                        bundlesByPackage.get(exportedPackage).add(bundle.getSymbolicName());
                    }
                }
            }

            // now show each package and the bundles that export it
            for (final Map.Entry<String, Set<String>> bundlesExportingPackage : bundlesByPackage.entrySet()) {
                logservice.log(LogService.LOG_INFO, new PackageProviderBundleStore(bundlesExportingPackage.getKey(),
                        bundlesExportingPackage.getValue()).toString());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop(final BundleContext arg0) throws Exception {
        // nothing to do here
    }

    private BundleActivator[] getBundleActivators(final BundleContext context) throws Exception {
        final Dictionary<String, String> headers = context.getBundle().getHeaders();
        final String activatorsString = headers.get("Additional-Bundle-Activators"); //$NON-NLS-1$
        List<String> activatorsList = null;
        if (activatorsString != null) {
            activatorsList = extractManifestEntryValues(activatorsString);

            final List<BundleActivator> returnValue = new ArrayList<BundleActivator>(activatorsList.size());
            for (final String activatorEntry : activatorsList) {
                returnValue.add((BundleActivator) Class.forName(activatorEntry).newInstance());
            }
            return returnValue.toArray(new BundleActivator[returnValue.size()]);
        }
        return null;
    }

    private List<String> extractManifestEntryValues(final String manifestEntry) {
        final List<String> manifestEntryValues = new ArrayList<String>();
        final StringTokenizer st = new StringTokenizer(manifestEntry, ","); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            manifestEntryValues.add(st.nextToken());
        }
        return manifestEntryValues;
    }
}
