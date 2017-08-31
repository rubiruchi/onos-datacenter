#!/usr/bin/python

from mininet.topo import Topo
from mininet.net import Mininet
from mininet.cli import CLI
from mininet.node import CPULimitedHost
from mininet.link import TCLink
from mininet.util import irange,dumpNodeConnections
from mininet.log import setLogLevel
from mininet.node import RemoteController

import argparse
import sys
import time
import logging

class ClosTopo(Topo):

    def __init__(self, fanout, cores, **opts):
        Topo.__init__(self, **opts)

        # TODO aggiungi il codice mancante (agli studenti potrei aggiungere dello pseudo codice per aiutarli)

        # START
        root = {}
        aggr = {}
        edge = {}
        host = {}

        # number of aggregation switches
        a_n = cores * fanout

        # number of edge switches
        e_n = a_n * fanout #cores * fanout^2

        # number of hosts
        h_n = e_n  * fanout #cores * fanout^3


        # Create core switches
        for i in xrange(0, cores):
            root[i] = self.addSwitch("root" + str(i), dpid=self.get_DPID(i))

        # Create aggregation switches
        for i in xrange(0, a_n):
            aggr[i] = self.addSwitch('aggr' + str(i), dpid=self.get_DPID(1+cores+i))

            # and links to parents
            for j in xrange(0, cores):
                self.addLink(aggr[i], root[j])

        # Create edge switches
        for i in xrange(0, e_n):
            edge[i] = self.addSwitch('edge' + str(i), dpid=self.get_DPID(10+a_n+i))

            # and links to parents
            for j in xrange(a_n):
                self.addLink(edge[i], aggr[j])

        # Create hosts switches
        for i in xrange(0, h_n):
            host[i] = self.addHost('h' + str(i))

            # and links to parents
            #for j in xrange(e_n):
            self.addLink(host[i], edge[int(i / fanout)])


        # END

    # Need to manual assign the DPID to avoid conflicts
    @staticmethod
    def get_DPID(id):
        return "1234" + str(id).rjust(12, "0")


def setup_clos_topo(fanout=2, cores=1):
    assert(fanout>0)
    assert(cores>0)
    topo = ClosTopo(fanout, cores)
    net = Mininet(topo=topo, controller=lambda name: RemoteController('c0', "127.0.0.1"), autoSetMacs=True, link=TCLink)
    net.start()
    time.sleep(20)
    net.pingAll()
    CLI(net)
    net.stop()

    # Per terminare mininet usa Ctrl-C e poi esegui sudo mn -c  per ripulire il sistema dai network adapter creati


def main(argv):
    parser = argparse.ArgumentParser(description="Parse input information for mininet Clos network")
    parser.add_argument('--num_of_core_switches', '-c', dest='cores', type=int, help='number of core switches')
    parser.add_argument('--fanout', '-f', dest='fanout', type=int, help='network fanout')
    args = parser.parse_args(argv)
    setLogLevel('info')
    setup_clos_topo(args.fanout, args.cores)


if __name__ == '__main__':
    main(sys.argv[1:])
