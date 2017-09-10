package emarco.datacenter;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.onlab.packet.IpAddress;
import org.onosproject.incubator.net.virtual.TenantId;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

@Component(immediate = true)
@Service(value = TenantsMapProvider.class)
public class TenantsMapProvider implements TenantsMapService {

    private final Logger log = getLogger(getClass());

    // Tenants file path
    private static final String DEFAULT_TENANTS_FILE = "./tenants.csv";

    @Property(name = "tenantsFile", value = DEFAULT_TENANTS_FILE,
            label = "Enable record metrics for reactive forwarding")
    private String tenantsFile = DEFAULT_TENANTS_FILE;

    // Tenants Map:
    // We're using a ConcurrentHashMap since it's the fastest Collection in get operations
    private final Map<IpAddress, TenantId> hostTenantMap = new ConcurrentHashMap<>();

    @Override
    public Map<IpAddress, TenantId> getTenants() {
        return hostTenantMap;
    }

    /**
     * Read tenants from file and update cache
     */
    @Override
    public void updateTenants() {
        updateTenants(tenantsFile);
    }

    @Override
    public void updateTenants(String inputTenantsFile) {
        log.info("UpdateTenants: triggered.");
        // Clear up all entries in the map
        hostTenantMap.clear();

        java.nio.file.Path currentRelativePath = Paths.get("");
        String spath = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + spath);

        // Read tenants file
        if (inputTenantsFile == null) inputTenantsFile = tenantsFile;
        java.nio.file.Path path = Paths.get(inputTenantsFile);
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(s -> {

                String words[] = s.split(",");
                TenantId tenant = TenantId.tenantId(words[0]);

                for (int i = 1; i < words.length; i++) {
                    hostTenantMap.put(IpAddress.valueOf(words[i]), tenant);
                }

            });
        } catch (IOException ex) {
            log.error("UpdateTenants: " + ex.toString());
        }

        log.info("UpdateTenants Map: " + hostTenantMap.toString());

        log.info("UpdateTenants: done!");
    }

    @Override
    public boolean canHostsCommunicate(IpAddress h1, IpAddress h2) {
        return (hostTenantMap.get(h1) == hostTenantMap.get(h2));
    }
}