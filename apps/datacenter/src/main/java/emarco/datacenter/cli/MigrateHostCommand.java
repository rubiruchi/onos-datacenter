package emarco.datacenter.cli;

import emarco.datacenter.ReactiveForwarding;
import emarco.datacenter.TenantsMapProvider;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onlab.packet.IpAddress;
import org.onosproject.cli.AbstractShellCommand;

/**
 *
 */
@Command(scope = "emarco.datacenter", name = "migratehost", description = "Redirects IP traffic from source Host to destination Host.")
public class MigrateHostCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "srcIP", description = "Host to be migrated", required = true, multiValued = false)
    private String sSrcIP = null;

    @Argument(index = 1, name = "dstIP", description = "Destination host for the migration", required = true, multiValued = false)
    private String sDstIP = null;

    protected ReactiveForwarding reactiveForwarding;

    protected TenantsMapProvider tenantsMapProvider;

    @Override
    protected void execute() {
        reactiveForwarding = AbstractShellCommand.get(ReactiveForwarding.class);
        tenantsMapProvider = AbstractShellCommand.get(TenantsMapProvider.class);

        IpAddress srcIP = IpAddress.valueOf(sSrcIP);
        IpAddress dstIP = IpAddress.valueOf(sDstIP);

        if (srcIP.version() != dstIP.version()) {
            print("ERROR: You cannot mix different IP versions!");
            return;
        }
        if (!tenantsMapProvider.canHostsCommunicate(srcIP, dstIP)) {
            print("ERROR: The specified Hosts do not belong to the same Tenant. Migration failed.");
            return;
        }

        reactiveForwarding.migrate(srcIP, dstIP);

        /*flowRuleService.removeFlowRulesById();

        tenantsMapService.getTenants().forEach((ip, tenant) -> {
            print("Host %s belongs to Tenant %s", ip, tenant);
        });*/

        //HostId.hostId()
    }




}
