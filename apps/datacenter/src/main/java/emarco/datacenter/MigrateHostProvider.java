package emarco.datacenter;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onlab.packet.*;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by onos on 25/08/17.
 */
public class MigrateHostProvider implements MigrateHostService {

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected TenantsMapProvider tenantsMapService;

    protected ReactiveForwarding reactiveForwarding;

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

/*
        TrafficSelector.Builder selectorBuilder = DefaultTrafficSelector.builder();

        if (srcIP.isIp4()) {
                Ip4Prefix matchIpv4SrcPrefix =
                        Ip4Prefix.valueOf(srcIP.getIp4Address(),
                                Ip4Prefix.MAX_MASK_LENGTH);
                Ip4Prefix matchIpv4DstPrefix =
                        Ip4Prefix.valueOf(dstIP.getIp4Address(),
                                Ip4Prefix.MAX_MASK_LENGTH);

                selectorBuilder.matchEthType(Ethernet.TYPE_IPV4)
                        .matchIPSrc(matchIpv4SrcPrefix)
                        .matchIPDst(matchIpv4DstPrefix);
        } else if (srcIP.isIp6()) {
            Ip6Prefix matchIpv6SrcPrefix =
                    Ip6Prefix.valueOf(srcIP.getIp6Address(),
                            Ip6Prefix.MAX_MASK_LENGTH);
            Ip6Prefix matchIpv6DstPrefix =
                    Ip6Prefix.valueOf(dstIP.getIp6Address(),
                            Ip6Prefix.MAX_MASK_LENGTH);

            selectorBuilder.matchEthType(Ethernet.TYPE_IPV6)
                    .matchIPSrc(matchIpv6SrcPrefix)
                    .matchIPDst(matchIpv6DstPrefix);
        }
        else {
            // This should never happen
            return false;
        }

        TrafficTreatment drop = DefaultTrafficTreatment.builder()
        .setIpDst().build();

        flowObjectiveService.forward(context.inPacket().receivedFrom().deviceId(), DefaultForwardingObjective.builder()
        .withSelector(selectorBuilder.build())
        .withTreatment(drop)
        .withPriority(flowPriority)
        .withFlag(ForwardingObjective.Flag.VERSATILE)
        .fromApp(appId)
        //.makeTemporary(flowTimeout)
        .add()
        );
*/
        return true;
    }

    @Override
    public void clearMap() {
        hostMigrationMap.clear();
    }
}
