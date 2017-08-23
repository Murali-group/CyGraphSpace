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

        action = new PostGraphMenuAction("Network to GraphSpace", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);

        CyApplicationConfiguration config = getService(context,CyApplicationConfiguration.class);
        CySwingAppAdapter appAdapter = getService(context, CySwingAppAdapter.class);
        CyNetworkTableManager networkTableManager = getService(context, CyNetworkTableManager.class);
        CyTableManager tableManager = getService(context, CyTableManager.class);

        //Register these with the CyObjectManager singleton.
        CyObjectManager manager = CyObjectManager.INSTANCE;
        File configDir = config.getAppConfigurationDirectoryLocation(CyActivator.class);
        configDir.mkdirs();
        manager.setConfigDir(configDir);
        manager.setCySwingAppAdapter(appAdapter);
        manager.setNetworkTableManager(networkTableManager);
        manager.setTableManager(tableManager);

        CySwingApplication desktop = getService(bc,CySwingApplication.class);
        manager.setCySwingApplition(desktop);

        PostGraphToolBarComponent toolBarComponent = new PostGraphToolBarComponent();
        registerAllServices(bc, toolBarComponent, new Properties());

		LoadNetworkFileTaskFactory loadNetworkFileTaskFactory = getService(bc, LoadNetworkFileTaskFactory.class);
		manager.setLoadNetworkFileTaskFactory(loadNetworkFileTaskFactory);
		ExportNetworkTaskFactory exportNetworkTaskFactory = getService(bc, ExportNetworkTaskFactory.class);
		manager.setExportNetworkTaskFactory(exportNetworkTaskFactory);
		LoadVizmapFileTaskFactory loadVizmapFileTaskFactory = getService(bc, LoadVizmapFileTaskFactory.class);
		manager.setLoadVizmapTaskFactory(loadVizmapFileTaskFactory);
		ExportVizmapTaskFactory exportVizmapTaskFactory = getService(bc, ExportVizmapTaskFactory.class);
		manager.setExportVizmapTaskFactory(exportVizmapTaskFactory);

		@SuppressWarnings("rawtypes")
		TaskManager taskManager = getService(context, DialogTaskManager.class);
		manager.setTaskManager(taskManager);
		OpenBrowser openBrowser = getService(context, OpenBrowser.class);
	    GetGraphsPanel getGraphsPanel = new GetGraphsPanel(taskManager, openBrowser);
	    registerAllServices(context, getGraphsPanel, new Properties());

        final CyNetworkFactory cyNetworkFactory = getService(bc, CyNetworkFactory.class);
		final CyNetworkManager cyNetworkManager = getService(bc, CyNetworkManager.class);
		final CyRootNetworkManager cyRootNetworkManager = getService(bc, CyRootNetworkManager.class);
		manager.setCyNetworkFactory(cyNetworkFactory);
		manager.setCyNetworkManager(cyNetworkManager);
		manager.setCyRootNetworkManager(cyRootNetworkManager);
    }
}
