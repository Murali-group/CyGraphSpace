package org.cytoscape.graphspace.cygraphspace.internal;

import java.io.File;
import java.util.Properties;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkTableManager;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.graphspace.cygraphspace.internal.gui.MyGraphsTablePanel;
import org.cytoscape.graphspace.cygraphspace.internal.gui.MyGraphsTablePanelAction;
import org.cytoscape.graphspace.cygraphspace.internal.gui.PublicGraphsTablePanel;
import org.cytoscape.graphspace.cygraphspace.internal.gui.PublicGraphsTablePanelAction;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.task.edit.MapTableToNetworkTablesTaskFactory;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.write.ExportNetworkTaskFactory;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {
	
	public void activateLogout(){
		
	}
    @Override
    public void start(BundleContext context) throws Exception {
        CyApplicationManager applicationManager = getService(context, CyApplicationManager.class);
        
        AbstractCyAction action = null;
        Properties properties = null;
        BundleContext bc = context;

        action = new AuthenticationMenuAction("Log in/Change host", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        action = new GetGraphMenuAction("Import Graphs", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        action = new PostGraphMenuAction("Post/Update Graph", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        CyTableFactory tableFactory = getService(bc,CyTableFactory.class);
        
		MapTableToNetworkTablesTaskFactory mapTableToNetworkTablesTaskFactory = getService(bc,MapTableToNetworkTablesTaskFactory.class);
		CreateTableTaskFactory createTableTaskFactory = new CreateTableTaskFactory(tableFactory,mapTableToNetworkTablesTaskFactory);
		
		Properties createTableTaskFactoryProps = new Properties();
		createTableTaskFactoryProps.setProperty("preferredMenu","Apps.CyGraphSpace");
		createTableTaskFactoryProps.setProperty("title","Create Table");
		registerService(bc,createTableTaskFactory,TaskFactory.class, createTableTaskFactoryProps);
		
        
//        action = new UpdateGraphMenuAction("Update Graph", applicationManager);
//        properties = new Properties();
//        registerAllServices(context, action, properties);
		
        //Get Cytocape objects needed by this app using the bundle context.
        CyApplicationConfiguration config = getService(context,CyApplicationConfiguration.class);
        CySwingAppAdapter appAdapter = getService(context, CySwingAppAdapter.class);
        CyNetworkTableManager networkTableManager = getService(context, CyNetworkTableManager.class);
        CyTableManager tableManager = getService(context, CyTableManager.class);
        
//        ServiceTracker cytoscapeJsWriterFactory = null;
//		ServiceTracker cytoscapeJsReaderFactory = null;
//		//CyNetworkViewWriterFactory cxWriterFactory = null;
//
//		cytoscapeJsWriterFactory = new ServiceTracker(bc, bc.createFilter("(&(objectClass=org.cytoscape.io.write.CyNetworkViewWriterFactory)(id=cytoscapejsNetworkWriterFactory))"), null);
//		cytoscapeJsWriterFactory.open();
//		cytoscapeJsReaderFactory = new ServiceTracker(bc, bc.createFilter("(&(objectClass=org.cytoscape.io.read.InputStreamTaskFactory)(id=cytoscapejsNetworkReaderFactory))"), null);
//		cytoscapeJsReaderFactory.open();
        
        //Register these with the CyObjectManager singleton.
        CyObjectManager manager = CyObjectManager.INSTANCE;
        File configDir = config.getAppConfigurationDirectoryLocation(CyActivator.class);
        configDir.mkdirs();
        manager.setConfigDir(configDir);
        manager.setCySwingAppAdapter(appAdapter);
        manager.setNetworkTableManager(networkTableManager);
        manager.setTableManager(tableManager);
        
//        CyNetworkViewWriterFactory cytoscapeJsWriterFactory = getService(bc, CyNetworkViewWriterFactory.class,
//				"(id=cytoscapejsNetworkWriterFactory)");
//        manager.setCytoscapeJsWriterFactory(cytoscapeJsWriterFactory);
//		InputStreamTaskFactory cytoscapeJsReaderFactory = getService(bc, InputStreamTaskFactory.class,
//				"(id=cytoscapejsNetworkReaderFactory)");
//		manager.setCytoscapeJsReaderFactory(cytoscapeJsReaderFactory);
		LoadNetworkFileTaskFactory loadNetworkFileTaskFactory = getService(bc, LoadNetworkFileTaskFactory.class);
		manager.setLoadNetworkFileTaskFactory(loadNetworkFileTaskFactory);
		ExportNetworkTaskFactory exportNetworkTaskFactory = getService(bc, ExportNetworkTaskFactory.class);
		manager.setExportNetworkTaskFactory(exportNetworkTaskFactory);
//        registerService(bc,new GraphSpaceNetworkAboutToBeDestroyedListener(), NetworkAboutToBeDestroyedListener.class, new Properties());  
		
		CySwingApplication cytoscapeDesktopService = getService(bc,CySwingApplication.class);
		
		MyGraphsTablePanel myGraphsTablePanel = new MyGraphsTablePanel();
		MyGraphsTablePanelAction myGraphsTablePanelAction = new MyGraphsTablePanelAction(cytoscapeDesktopService,myGraphsTablePanel);
		
		registerService(bc,myGraphsTablePanel,CytoPanelComponent.class, new Properties());
		registerService(bc,myGraphsTablePanelAction,CyAction.class, new Properties());
		
		PublicGraphsTablePanel publicGraphsTablePanel = new PublicGraphsTablePanel();
		PublicGraphsTablePanelAction publicGraphsTablePanelAction = new PublicGraphsTablePanelAction(cytoscapeDesktopService, publicGraphsTablePanel);
		
		registerService(bc,publicGraphsTablePanel,CytoPanelComponent.class, new Properties());
		registerService(bc,publicGraphsTablePanelAction,CyAction.class, new Properties());
    }
}