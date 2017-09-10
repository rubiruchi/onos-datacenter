/*
 * Copyright 2014 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package emarco.datacenter.cli;

import emarco.datacenter.ReactiveForwarding;
import emarco.datacenter.TenantsMapProvider;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;

/**
 *
 */
@Command(scope = "emarco.datacenter", name = "tenantsmap", description = "Updates the Tenants Map")
public class TenantsMapCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "filePath", description = "Path of the Tenants file", required = false, multiValued = false)
    private String filePath = null;

    protected TenantsMapProvider tenantsMapService;

    protected ReactiveForwarding reactiveForwarding;

    @Override
    protected void execute() {
        tenantsMapService = AbstractShellCommand.get(TenantsMapProvider.class);
        tenantsMapService.updateTenants(filePath);

        tenantsMapService.getTenants().forEach((ip, tenant) -> {
            print("Host %s belongs to Tenant %s", ip, tenant);
        });

        reactiveForwarding = get(ReactiveForwarding.class);
        print("Clearing up past flow rules.");
        reactiveForwarding.cleanAppFlowRules();
    }
}
