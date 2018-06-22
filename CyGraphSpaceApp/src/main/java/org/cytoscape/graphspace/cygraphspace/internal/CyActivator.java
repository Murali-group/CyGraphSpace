package org.cytoscape.graphspace.cygraphspace.internal;

import java.io.File;
import java.util.Properties;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.graphspace.cygraphspace.internal.gui.CyGraphSpaceResultPanel;
import org.cytoscape.graphspace.cygraphspace.internal.gui.GetGraphsPanel;
import org.cytoscape.graphspace.cygraphspace.internal.gui.PostGraphToolBarComponent;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.util.MessageConfig;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.task.write.ExportNetworkTaskFactory;
import org.cytoscape.task.write.ExportNetworkViewTaskFactory;
import org.cytoscape.task.write.ExportVizmapTaskFactory;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

/**
 * Used to register services used by CyGraphSpaceApp
 * @author rishabh
 *
 */
public class CyActivator extends AbstractCyActivator {

	@SuppressWarnings("rawtypes")
	@Override
	public void start(BundleContext context) throws Exception {
		CyApplicationManager applicationManager = getService(context, CyApplicationManager.class);

		AbstractCyAction action = null;
		Properties properties = null;
		BundleContext bc = context;

		//getting cytoscape services
		CyApplicationConfiguration config = getService(context,CyApplicationConfiguration.class);
		CySwingAppAdapter appAdapter = getService(context, CySwingAppAdapter.class);
		CySwingApplication desktop = getService(bc,CySwingApplication.class);
		LoadNetworkFileTaskFactory loadNetworkFileTaskFactory = getService(bc, LoadNetworkFileTaskFactory.class);
		ExportNetworkTaskFactory exportNetworkTaskFactory = getService(bc, ExportNetworkTaskFactory.class);
		ExportNetworkViewTaskFactory exportNetworkViewTaskFactory = getService(bc, ExportNetworkViewTaskFactory.class);
		LoadVizmapFileTaskFactory loadVizmapFileTaskFactory = getService(bc, LoadVizmapFileTaskFactory.class);
		ExportVizmapTaskFactory exportVizmapTaskFactory = getService(bc, ExportVizmapTaskFactory.class);
		TaskManager taskManager = getService(context, DialogTaskManager.class);
		final CyNetworkFactory cyNetworkFactory = getService(bc, CyNetworkFactory.class);
		final CyNetworkManager cyNetworkManager = getService(bc, CyNetworkManager.class);
		final CyRootNetworkManager cyRootNetworkManager = getService(bc, CyRootNetworkManager.class);

		//Register these with the CyObjectManager singleton.
		CyObjectManager manager = CyObjectManager.INSTANCE;
		File configDir = config.getAppConfigurationDirectoryLocation(CyActivator.class);
		configDir.mkdirs();

		//setting services to manager singleton
		manager.setCyApplicationManager(applicationManager);
		manager.setConfigDir(configDir);
		manager.setCySwingAppAdapter(appAdapter);
		manager.setCySwingApplition(desktop);
		manager.setLoadNetworkFileTaskFactory(loadNetworkFileTaskFactory);
		manager.setExportNetworkTaskFactory(exportNetworkTaskFactory);
		manager.setExportNetworkViewTaskFactory(exportNetworkViewTaskFactory);
		manager.setLoadVizmapTaskFactory(loadVizmapFileTaskFactory);
		manager.setExportVizmapTaskFactory(exportVizmapTaskFactory);
		manager.setCyNetworkFactory(cyNetworkFactory);
		manager.setCyNetworkManager(cyNetworkManager);
		manager.setCyRootNetworkManager(cyRootNetworkManager);

		//register result panel
		CyGraphSpaceResultPanel resultPanel = new CyGraphSpaceResultPanel("CyGraphSpace taskbar");
		registerAllServices(context, resultPanel, new Properties());

	    //registering Toolbar component
        PostGraphToolBarComponent toolBarComponent = new PostGraphToolBarComponent(resultPanel);
        registerAllServices(bc, toolBarComponent, new Properties());

        //register Post Graph Action
        action = new CyGraphSpaceMenuAction(MessageConfig.MenuActionTitle, resultPanel);
        properties = new Properties();
        registerService(context, action, CyAction.class, properties);
        
		//registering openBrowser for opening graph in GraphSpace
		OpenBrowser openBrowser = getService(context, OpenBrowser.class);
		GetGraphsPanel getGraphsPanel = new GetGraphsPanel(taskManager, openBrowser);
		registerAllServices(context, getGraphsPanel, new Properties());
	}
}
