export ONOS_ROOT=/home/onos/Repo/onos/ 
cd $ONOS_ROOT
git checkout 1.10.3
$ONOS_ROOT/tools/build/onos-buck build onos --show-output
source $ONOS_ROOT/tools/dev/bash_profile
# $ONOS_ROOT/tools/build/onos-buck test
$ONOS_ROOT/tools/build/onos-buck run onos-local -- clean debug

