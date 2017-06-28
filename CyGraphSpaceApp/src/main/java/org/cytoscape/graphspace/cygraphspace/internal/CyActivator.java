package org.cytoscape.graphspace.cygraphspace.internal;

import java.io.File;
import java.util.Properties;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetworkTableManager;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        CyApplicationManager applicationManager = getService(context, CyApplicationManager.class);
        
        AbstractCyAction action = null;
        Properties properties = null;
        BundleContext bc = context;

        action = new AuthenticationMenuAction("Sign in/Change host", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        action = new GetGraphMenuAction("Import Graphs from GraphSpace", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        action = new PostGraphMenuAction("Export Graphs to GraphSpace", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        action = new UpdateGraphMenuAction("Update Graph", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
//      final CyNetworkViewWriterFactory cytoscapeJsWriterFactory = getService(bc, CyNetworkViewWriterFactory.class,
//				"(id=cytoscapejsNetworkWriterFactory)");
//		final InputStreamTaskFactory cytoscapeJsReaderFactory = getService(bc, InputStreamTaskFactory.class,
//				"(id=cytoscapejsNetworkReaderFactory)");
		
        //Get Cytocape objects needed by this app using the bundle context.
        CyApplicationConfiguration config = getService(context,CyApplicationConfiguration.class);
        CySwingAppAdapter appAdapter = getService(context, CySwingAppAdapter.class);
        final CyNetworkTableManager networkTableManager = getService(context, CyNetworkTableManager.class);
        
        
        //Register these with the CyObjectManager singleton.
        CyObjectManager manager = CyObjectManager.INSTANCE;
        File configDir = config.getAppConfigurationDirectoryLocation(CyActivator.class);
        configDir.mkdirs();
        manager.setConfigDir(configDir);
        manager.setCySwingAppAdapter(appAdapter);
        manager.setNetworkTableManager(networkTableManager);
        
        registerService(bc,new GraphSpaceNetworkAboutToBeDestroyedListener(), NetworkAboutToBeDestroyedListener.class, new Properties());   
    }
}