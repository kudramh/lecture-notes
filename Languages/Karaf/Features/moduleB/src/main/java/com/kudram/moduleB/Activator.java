package com.kudram.moduleB;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) {
        System.out.println("Apache Karaf starting moduleA bundle");
    }

    public void stop(BundleContext context) {
        System.out.println("Apache Karaf stopping moduleA bundle");
    }

}