/*
*/

package org.onosproject.net.apps;

import java.util.Map;
import org.onlab.packet.IpAddress;


/**
 * TenantsMapService
 * Keeps track of the tenant each host belongs to.
 */
public interface TenantsMapService<T> {

    /**
     * Get the endpoints of the host-to-host intents that were installed.
     *
     * @return maps of source to destination
     */

    public Map<IpAddress, T> getTenants();

}
