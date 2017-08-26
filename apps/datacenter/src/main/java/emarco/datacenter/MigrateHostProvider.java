package emarco.datacenter;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.onlab.packet.*;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component(immediate = true)
@Service(value = MigrateHostProvider.class)
public class MigrateHostProvider implements MigrateHostService {

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected TenantsMapProvider tenantsMapService;

    // We're using ConcurrentHashMaps since it's the fastest Collection in get operations

    // Keep track of all migrated hosts
    private final Map<IpAddress, IpAddress> hostMigrationMap = new ConcurrentHashMap<>();

    // Keep track of all destination hosts
    private final Map<IpAddress, IpAddress> destinationHosts = new ConcurrentHashMap<>();

    @Override
    public IpAddress hasMigrated(IpAddress ip) {
        return hostMigrationMap.get(ip);
    }

    @Override
    public IpAddress isMigrated(IpAddress ip) {
        return destinationHosts.get(ip);
    }

    @Override
    public boolean migrate(IpAddress srcIP, IpAddress dstIP) {

        // Check if hosts belong to the same Tenant
        if (!tenantsMapService.canHostsCommunicate(srcIP, dstIP) ||
                (srcIP.version() != dstIP.version())) return false;

        hostMigrationMap.put(srcIP, dstIP);
        hostMigrationMap.put(dstIP, srcIP);

        return true;
    }

    @Override
    public void clearMap() {
        hostMigrationMap.clear();
        destinationHosts.clear();
    }
}
