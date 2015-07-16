package com.github.bvigar.eclipse.osgi.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Trent Vigar
 */
public class PackageProviderBundleStoreTest {

	private final String testPackage = "testPackage1";
	private Set<String> testProviderBundles;
	
	@Before
	public void setup() {
		testProviderBundles = new HashSet<String>();
		testProviderBundles.add("testBundle1");
		testProviderBundles.add("testBundle2");
		testProviderBundles.add("testBundle3");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPackageProviderBundleStore_nullPackage() {
		new PackageProviderBundleStore((String) null, testProviderBundles);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPackageProviderBundleStore_emptyPackage() {
		new PackageProviderBundleStore("   ", testProviderBundles);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPackageProviderBundleStore_nullProviderBundles() {
		new PackageProviderBundleStore(testPackage, (Set<String>) null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPackageProviderBundleStore_emptyProviderBundles() {
		new PackageProviderBundleStore(testPackage, Collections.<String> emptySet());
	}
}
