package org.nebulostore.kademlia.interfaces.rest;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.JSONP;
import org.nebulostore.kademlia.KademliaException;
import org.nebulostore.kademlia.KademliaRouting;
import org.nebulostore.kademlia.Key;
import org.nebulostore.kademlia.NodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("find_nodes/{key}")
public final class FindNodesResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(FindNodesResource.class);
	private final KademliaRouting kademlia_;
	
	public FindNodesResource(KademliaRouting kademlia) {
		kademlia_ = kademlia;
	}
	
	@GET
	@JSONP
	@Produces(MediaType.APPLICATION_JSON) 
	public NodeInfoCollectionBean findNodes(@PathParam("key") String paramKey) {
		LOGGER.info("findNodes({})", paramKey);
		Key key = new Key(Integer.parseInt(paramKey));
        Collection<NodeInfo> nodeInfos = null;
        try {
			nodeInfos = kademlia_.findClosestNodes(key);
		} catch (InterruptedException | KademliaException e) {
			LOGGER.error(String.format("findNodes(%s)", key), e);
			return null;
		}
        String[] parsedNodeInfos = new String[nodeInfos.size()];
        int i = 0;
        for (NodeInfo nodeInfo: nodeInfos) {
        	parsedNodeInfos[i] = nodeInfo.toString();
        	++i;
        }
        NodeInfoCollectionBean bean = new NodeInfoCollectionBean();
        bean.setNodeInfo(parsedNodeInfos);
        return bean;
	}
}
