package org.cytoscape.graphspace.cygraphspace.internal;

import java.io.File;
import java.util.Properties;

import javax.swing.JToolBar;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNetworkTableManager;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.property.CyProperty;
import org.cytoscape.property.CyProperty.SavePolicy;
import org.cytoscape.graphspace.cygraphspace.internal.gui.GetGraphsPanel;
import org.cytoscape.graphspace.cygraphspace.internal.gui.PostGraphToolBarComponent;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.task.write.ExportNetworkTaskFactory;
import org.cytoscape.task.write.ExportVizmapTaskFactory;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;
import org.cytoscape.graphspace.cygraphspace.internal.util.PersistentProperties;
import org.cytoscape.graphspace.cygraphspace.internal.util.PropertyReader;
import org.cytoscape.io.BasicCyFileFilter;
import org.cytoscape.io.DataCategory;
//import org.cytoscape.io.internal.read.json.CytoscapeJsNetworkReaderFactory;
import org.cytoscape.property.SimpleCyProperty;
import org.cytoscape.graphspace.cygraphspace.internal.io.read.json.CustomCytoscapeJsNetworkReaderFactory;
import org.cytoscape.graphspace.cygraphspace.internal.io.read.json.CustomCytoscapejsFileFilter;
import org.cytoscape.io.read.InputStreamTaskFactory;
import org.cytoscape.graphspace.cygraphspace.internal.io.read.json.CustomCytoscapeJsNetworkReader;
import org.cytoscape.io.util.StreamUtil;

import static org.cytoscape.work.ServiceProperties.IN_TOOL_BAR;

public class CyActivator extends AbstractCyActivator {
	
	public static String cyGraphSpaceHost;
	public static String cyGraphSpaceUsername;
	public static String cyGraphSpacePassword;
	
    @SuppressWarnings("rawtypes")
	@Override
    public void start(BundleContext context) throws Exception {
    	 
//        PropertyReader propertyReader = new PropertyReader("cygraphspace", "properties");
//        Properties propertyReaderProperties = new Properties();
//        propertyReaderProperties.setProperty("cyPropertyName", "cygraphspace.properties");
//        registerAllServices(context, propertyReader, propertyReaderProperties);
//
//        CyProperty<Properties> cyProperties = getService(context, CyProperty.class, "(cyPropertyName=cygraphspace.properties)");
//        Properties loginProperties = cyProperties.getProperties();
//        System.out.println(loginProperties.getProperty("cygraphspace.host"));
//        System.out.println(loginProperties.getProperty("cygraphspace.username"));
//        System.out.println(loginProperties.getProperty("cygraphspace.password"));
//        if (loginProperties.getProperty("cygraphspace.username").isEmpty()) {
//        	loginProperties.setProperty("cygraphspace.username", "rishu.sethi2525@gmail.com");
//        }
//        if (loginProperties.getProperty("cygraphspace.password").isEmpty()) {
//        	loginProperties.setProperty("cygraphspace.password", "123456789");
//        }
    	Properties props = new Properties();
        @SuppressWarnings("unchecked")
		CyProperty<Properties> service = new SimpleCyProperty("cygraphspace", props, Properties.class,
            SavePolicy.SESSION_FILE);
        Properties serviceProps = new Properties();
        serviceProps.setProperty("cyPropertyName", service.getName());
        registerAllServices(context, service, serviceProps);
        
        CyApplicationManager applicationManager = getService(context, CyApplicationManager.class);
        
        AbstractCyAction action = null;
        Properties properties = null;
        BundleContext bc = context;
//        action = new AuthenticationMenuAction("Log in/Change host", applicationManager);
//        properties = new Properties();
//        registerAllServices(context, action, properties);
//        
//        action = new GetGraphMenuAction("Import Graphs", applicationManager);
//        properties = new Properties();
//        registerAllServices(context, action, properties);
        
        action = new PostGraphMenuAction("Network to GraphSpace", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
//        CyTableFactory tableFactory = getService(bc,CyTableFactory.class);
//        
//		MapTableToNetworkTablesTaskFactory mapTableToNetworkTablesTaskFactory = getService(bc,MapTableToNetworkTablesTaskFactory.class);
//		CreateTableTaskFactory createTableTaskFactory = new CreateTableTaskFactory(tableFactory,mapTableToNetworkTablesTaskFactory);
//		
//		Properties createTableTaskFactoryProps = new Properties();
//		createTableTaskFactoryProps.setProperty("preferredMenu","Apps.CyGraphSpace");
//		createTableTaskFactoryProps.setProperty("title","Create Table");
//		registerService(bc,createTableTaskFactory,TaskFactory.class, createTableTaskFactoryProps);
		
//        action = new UpdateGraphMenuAction("Update Graph", applicationManager);
//        properties = new Properties();
//        registerAllServices(context, action, properties);
		
        //Get Cytocape objects needed by this app using the bundle context.
        CyApplicationConfiguration config = getService(context,CyApplicationConfiguration.class);
        CySwingAppAdapter appAdapter = getService(context, CySwingAppAdapter.class);
        CyNetworkTableManager networkTableManager = getService(context, CyNetworkTableManager.class);
        CyTableManager tableManager = getService(context, CyTableManager.class);
        
//        final TaskManager taskMgr = getService(context, DialogTaskManager.class);
//	    final GetGraphsClient guiClient = new GetGraphsClient( taskMgr, client, openBrowser, gpmlReaderFactory);
//	    registerAllServices(context, guiClient, new Properties());
          
//        ServiceTracker cytoscapeJsWriterFactory = null;
//		ServiceTracker cytoscapeJsReaderFactory = null;
//		//CyNetworkViewWriterFactory cxWriterFactory = null;
        
        //Register these with the CyObjectManager singleton.
        CyObjectManager manager = CyObjectManager.INSTANCE;
        File configDir = config.getAppConfigurationDirectoryLocation(CyActivator.class);
        configDir.mkdirs();
        manager.setConfigDir(configDir);
        manager.setCySwingAppAdapter(appAdapter);
        manager.setNetworkTableManager(networkTableManager);
        manager.setTableManager(tableManager);
//        manager.setCyProperties(cyProperties);
        CySwingApplication desktop = getService(bc,CySwingApplication.class);
        manager.setCySwingApplition(desktop);
//        desktop.getJToolBar().add(new PostGraphToolBarComponent());
//        action = new PostGraphToolBarAction("Network to GraphSpace", applicationManager);
//        properties = new Properties();
//        registerAllServices(context, action, properties);
        PostGraphToolBarComponent toolBarComponent = new PostGraphToolBarComponent();
        registerAllServices(bc, toolBarComponent, new Properties());
//        toolBarComponent.getComponent().setVisible(true);
//        desktop.getJToolBar().add(toolBarComponent);
//        desktop.getJToolBar().updateUI();
//        desktop.getJToolBar().setVisible(true);
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
		LoadVizmapFileTaskFactory loadVizmapFileTaskFactory = getService(bc, LoadVizmapFileTaskFactory.class);
		manager.setLoadVizmapTaskFactory(loadVizmapFileTaskFactory);
		ExportVizmapTaskFactory exportVizmapTaskFactory = getService(bc, ExportVizmapTaskFactory.class);
		manager.setExportVizmapTaskFactory(exportVizmapTaskFactory);
//      registerService(bc,new GraphSpaceNetworkAboutToBeDestroyedListener(), NetworkAboutToBeDestroyedListener.class, new Properties());  
		
//		CySwingApplication cytoscapeDesktopService = getService(bc,CySwingApplication.class);
		
//		MyGraphsTablePanel myGraphsTablePanel = new MyGraphsTablePanel();
//		MyGraphsTablePanelAction myGraphsTablePanelAction = new MyGraphsTablePanelAction(cytoscapeDesktopService,myGraphsTablePanel);
//		
//		registerService(bc,myGraphsTablePanel,CytoPanelComponent.class, new Properties());
//		registerService(bc,myGraphsTablePanelAction,CyAction.class, new Properties());
//		
//		PublicGraphsTablePanel publicGraphsTablePanel = new PublicGraphsTablePanel();
//		PublicGraphsTablePanelAction publicGraphsTablePanelAction = new PublicGraphsTablePanelAction(cytoscapeDesktopService, publicGraphsTablePanel);
//		
//		registerService(bc,publicGraphsTablePanel,CytoPanelComponent.class, new Properties());
//		registerService(bc,publicGraphsTablePanelAction,CyAction.class, new Properties());
		
//		PersistentProperties persistentProperties = new PersistentProperties("CyGraphSpace", "CyGraphSpace.props");
//		Properties propsReaderServiceProps = new Properties();
//		manager.setPeristentProperties();
//		propsReaderServiceProps.setProperty("cyPropertyName", “myApp.props");
//		registerAllServices(context, propsReader, propsReaderServiceProps);
		@SuppressWarnings("rawtypes")
		TaskManager taskManager = getService(context, DialogTaskManager.class);
		manager.setTaskManager(taskManager);
		OpenBrowser openBrowser = getService(context, OpenBrowser.class);
	    GetGraphsPanel getGraphsPanel = new GetGraphsPanel(taskManager, openBrowser);
	    registerAllServices(context, getGraphsPanel, new Properties());
	    
	    final StreamUtil streamUtil = getService(bc, StreamUtil.class);
        final CyNetworkFactory cyNetworkFactory = getService(bc, CyNetworkFactory.class);
		final CyNetworkManager cyNetworkManager = getService(bc, CyNetworkManager.class);
		final CyRootNetworkManager cyRootNetworkManager = getService(bc, CyRootNetworkManager.class);
		manager.setCyNetworkFactory(cyNetworkFactory);
		manager.setCyNetworkManager(cyNetworkManager);
		manager.setCyRootNetworkManager(cyRootNetworkManager);
//		final CyNetworkViewManager viewManager = getService(bc, CyNetworkViewManager.class);
//		final VisualMappingManager vmm = getService(bc, VisualMappingManager.class);
        // ///////////////// Readers ////////////////////////////
//        
// 		BasicCyFileFilter cytoscapejsReaderFilter = new BasicCyFileFilter(new String[] { "cyjs", "json" },
// 				new String[] { "application/json" }, "Cytoscape.js JSON", DataCategory.NETWORK, streamUtil);
// 		CustomCytoscapeJsNetworkReaderFactory jsReaderFactory = new CustomCytoscapeJsNetworkReaderFactory(
// 				cytoscapejsReaderFilter, applicationManager, cyNetworkFactory, cyNetworkManager, cyRootNetworkManager);
// 		final Properties cytoscapeJsNetworkReaderFactoryProps = new Properties();
// 		registerService(bc, jsReaderFactory, InputStreamTaskFactory.class, cytoscapeJsNetworkReaderFactoryProps);
// 		manager.setCytoscapeJsNetworkReaderFactory(jsReaderFactory);
 		
 		
    }
}