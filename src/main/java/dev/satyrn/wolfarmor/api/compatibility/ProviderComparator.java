package dev.satyrn.wolfarmor.api.compatibility;

import java.util.Comparator;

public class ProviderComparator implements Comparator<Provider.ProviderType> {
    /**
     * Compares two providers by priority
     * @param o1 The first provider
     * @param o2 The second provider
     * @return &lt;0 if o1 is lower priority, 0 if o1 is equal, and &gt;0 if o2 is higher priority
     */
    @Override
    public int compare(Provider.ProviderType o1, Provider.ProviderType o2) {
        final short o1Priority = o1.getPriority();
        final short o2Priority = o2.getPriority();
        return Short.compare(o1Priority, o2Priority);
    }
}
