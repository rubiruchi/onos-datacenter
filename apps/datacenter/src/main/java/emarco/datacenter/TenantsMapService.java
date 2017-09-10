/*
*/

package emarco.datacenter;

import java.util.Map;

import org.onlab.packet.IpAddress;
import org.onosproject.incubator.net.virtual.TenantId;


/**
 * TenantsMapService
 * Keeps track of the tenant each host belongs to.
 */
public interface TenantsMapService {

    /**
     * Get the endpoints of the host-to-host intents that were installed.
     *
     * @return maps of source to destination
     */

    Map<IpAddress, TenantId> getTenants();

    void updateTenants();

    void updateTenants(String filePath);

    boolean canHostsCommunicate(IpAddress h1, IpAddress h2);

}
