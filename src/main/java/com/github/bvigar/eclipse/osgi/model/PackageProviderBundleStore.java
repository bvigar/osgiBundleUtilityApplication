package com.github.bvigar.eclipse.osgi.model;

import java.util.Collections;
import java.util.Set;

/**
 * Model for connecting OSGi packages to their respective providing bundle(s). 
 * @author Trent Vigar
 */
public class PackageProviderBundleStore {

    private final String packageName;
    private final Set<String> providingBundles;

    /**
     * Constructs a new {@link PackageProviderBundleStore}. 
     * @param packageName The OSGi package name, cannot be null or empty.
     * @param providingBundles A set of OSGi bundles which provide (export) the provided packageName, cannot be null or empty. 
     * @throws IllegalArgumentException If parameter constraints are not met. 
     */
    public PackageProviderBundleStore(final String packageName, final Set<String> providingBundles) {
    	if (packageName == null || packageName.trim().isEmpty()) {
    		throw new IllegalArgumentException("packageName cannot be null or empty.");
    	}
    	if (providingBundles == null || providingBundles.isEmpty()) {
    		throw new IllegalArgumentException("providingBundles cannot be null or empty.");
    	}
        this.packageName = packageName;
        this.providingBundles = providingBundles;
    }

    /**
     * @return The OSGi package name belonging to this model, guaranteed not null or empty. 
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @return The set of either 1 or more OSGi bundle names which export the provided package, guaranteed not null or empty. 
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

    /**
     * {@inheritDoc}
     */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result = prime
				* result
				+ ((providingBundles == null) ? 0 : providingBundles.hashCode());
		return result;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PackageProviderBundleStore other = (PackageProviderBundleStore) obj;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (providingBundles == null) {
			if (other.providingBundles != null)
				return false;
		} else if (!providingBundles.equals(other.providingBundles))
			return false;
		return true;
	}
}
