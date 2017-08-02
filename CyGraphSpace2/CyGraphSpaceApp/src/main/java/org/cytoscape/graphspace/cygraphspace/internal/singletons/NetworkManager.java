package org.cytoscape.graphspace.cygraphspace.internal.singletons;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.graphspace.javaclient.model.GSGraph;

public enum NetworkManager {
    INSTANCE;
    
    private final Map<Long, GSGraph> networkInfoTable;
    private final Map<Long,UUID> networkIdTable; // store the network ids for collections from NDEx
    
    NetworkManager() { 
    	networkInfoTable = new TreeMap<>();
    	networkIdTable = new TreeMap<>();	}
 
    public GSGraph getCXInfoHolder(Long subNetworkId) {
    	return this.networkInfoTable.get(subNetworkId);
    }
    
    public void setCXInfoHolder(Long subNetworkId, GSGraph graph) {
    	this.networkInfoTable.put(subNetworkId, graph);
    }
    
    public void addNetworkUUID (Long subNetworkId, UUID networkId) {
    	networkIdTable.put(subNetworkId, networkId);
    }
    
    public UUID getNdexNetworkId(Long subNetworkId) {
    	return networkIdTable.get(subNetworkId);
    }
    
    public void deleteCyNetworkEntry(Long subNetworkId) {
    	this.networkInfoTable.remove(subNetworkId);
    	this.networkIdTable.remove(subNetworkId);
    }
}