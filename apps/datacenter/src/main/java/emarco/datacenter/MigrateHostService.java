/*
*/

package emarco.datacenter;

import java.util.Map;

import org.onlab.packet.IpAddress;

/**
 * TenantsMapService
 * Keeps track of the tenant each host belongs to.
 */
public interface MigrateHostService {

    Map<IpAddress, IpAddress> getMigrationMap();

    IpAddress hasMigrated(IpAddress source);

    boolean migrate(IpAddress srcIP, IpAddress dstIP);

    void clearMap();
}
