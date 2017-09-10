# Data Center Management using ONOS

Based on this [tutorial](https://www.dropbox.com/sh/57jf7ki6a1e9s0r/AAAeB2kmHkT7rs1BCiutP2ENa/assignment2?dl=0).

### Objectives:

- Multitenancy
- Host migration

### Requirements:

- ONOS v1.10.4
- Mininet v2.2.2

### Build:

```
git clone https://github.com/sdnwiselab/ONOS_Datacenter_app.git
cd ONOS_Datacenter_app/datacenter
mvn clean install
```

### Install:

ONOS must be running, then:

```
onos-app <ONOS IP> install! <PATH_TO_REPO_FOLDER>/datacenter/target/datacenter-<VERSION>.oar
```

### Commands:

Create the virtual datacenter in Mininet. In a terminal:

```
cd <PATH_TO_REPO_FOLDER>/scripts && sudo python clos_topo_builder.py -c <NO_OF_CORE_SWITCHES> -f <FANOUT>
```
In this case we are going to use 2 as `NO_OF_CORE_SWITCHES` and 2 as `FANOUT`.

Then, from the ONOS command line:
```
app deactivate org.onosproject.fwd
tenantsmap <PATH_TO_REPO_FOLDER>/datacenter/src/tenants.csv
```
To deactivate normal routing, load the file containing the list of the tenants and activate multitenancy. Then:
```
migratehost sourceIP destinationIP
```
To migrate an host.

### About:

Tested with ONOS 1.10.4

Forked from [eMarco](https://github.com/eMarco/ONOS_Datacenter_app)