package com.github.bvigar.eclipse.osgi.model;

import java.util.Collections;
import java.util.Set;

/**
 * Description.
 * @author Trent Vigar
 */
public class PackageProviderBundleStore {

    private final String packageName;
    private final Set<String> providingBundles;

    /**
     * @param packageName
     * @param providingBundles
     */
    public PackageProviderBundleStore(final String packageName, final Set<String> providingBundles) {
        this.packageName = packageName;
        this.providingBundles = providingBundles;
    }

    /**
     * @return
     */
    public String getPackageName() {
        return new String(packageName);
    }

    /**
     * @return
     */
    public Set<String> getProvidingBundles() {
        return Collections.<String> unmodifiableSet(providingBundles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append("Package : "); //$NON-NLS-1$
        stringRepresentation.append(packageName);
        stringRepresentation.append("\nProvided by : "); //$NON-NLS-1$
        for (final String bundle : providingBundles) {
            stringRepresentation.append("              "); //$NON-NLS-1$
            stringRepresentation.append(bundle);
            stringRepresentation.append("\n"); //$NON-NLS-1$
        }
        stringRepresentation.append("\n"); //$NON-NLS-1$
        return stringRepresentation.toString();
    }
}
