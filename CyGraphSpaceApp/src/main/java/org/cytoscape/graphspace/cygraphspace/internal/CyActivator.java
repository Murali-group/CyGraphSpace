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
    /**
     * This method is where everything for the NDEx Cytoscape App begins. If your App is properly installed, Cytoscape 
     * will call this method upon startup. Here you can do things like tell Cytoscape what GUI menu items that ought to 
     * be displayed to the user. It is also where your constructors that need references to Cytoscape objects 
     * controlling core Cytoscape functionality are retrieved.
     * 
     * @param context BundleContext is an OSGi type that is used to do stuff like retrieve references to objects 
     * registered with Cytoscape (under the hood, Cytoscape depends on OSGi to manage modularity) and also do things
     * like register menu items with Cytoscape. To avoid trouble, you should probably avoid low-level use of the 
     * BundleContext directly and just pass it as a parameter into methods provided by CyActivator when it is needed.
     */
    public void start(BundleContext context) throws Exception {
        // This application manager, like many other Cytsocape-related objects, is registered using the OSGi framework.
        // This is a common alternative to using a singleton. 
        CyApplicationManager applicationManager = getService(context, CyApplicationManager.class);
        
        AbstractCyAction action = null;
        Properties properties = null;
              
        // Unlike what you may be expecting if you are a Java Swing developer, instead of creating your own menu items,
        // you will often want to (and need to) delegate creating such menu items to Cytoscape instead. Looking at this
        // example, you may be curious where the "Select Server" menu item will go. You can find that out by looking in
        // the SelectServerMenuAction constructor. It is a common pattern in Cytoscape to seperate the name of the 
        // menu item from where it is located and that may be initially confusing if you are expecting these things to
        // be together. Finally, notice the last line in this code group. When we call "registerAllServices" we tell
        // Cytoscape about our menu item and Cytoscape puts it in the appropriate location upon start-up. If you are 
        // wondering why registerAllServices is plural, I don't have any idea either.
        action = new AuthenticationMenuAction("Sign in/Change host", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);

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
        
        
        
        // copied from cxio-impl
        BundleContext bc = context;
    /*    final StreamUtil streamUtil = getService(bc, StreamUtil.class);
        final CyLayoutAlgorithmManager layoutManager = getService(bc, CyLayoutAlgorithmManager.class);

        final CytoscapeCxFileFilter cx_filter = new CytoscapeCxFileFilter(new String[] { "cx" },
                                                                          new String[] { "application/json" },
                                                                           "CX JSON",
                                                                          DataCategory.NETWORK,
                                                                          streamUtil);

        // Writer:
        final VisualMappingManager visual_mapping_manager = getService(bc, VisualMappingManager.class);
        final CyApplicationManager application_manager = getService(bc, CyApplicationManager.class);
        final CyNetworkViewManager networkview_manager = getService(bc, CyNetworkViewManager.class);
        final CyNetworkManager network_manager = getService(bc, CyNetworkManager.class);
        final CyGroupManager group_manager = getService(bc, CyGroupManager.class);
        final CyNetworkTableManager table_manager = getService(bc, CyNetworkTableManager.class);
        final CyNetworkViewFactory network_view_factory = getService(bc, CyNetworkViewFactory.class);

        final CxNetworkWriterFactory network_writer_factory = new CxNetworkWriterFactory(cx_filter,
                                                                                         visual_mapping_manager,
                                                                                         application_manager,
                                                                                         networkview_manager,
                                                                                         network_manager,
                                                                                         group_manager,
                                                                                         table_manager);

        final Properties cx_writer_factory_properties = new Properties();

        cx_writer_factory_properties.put(ID, "cxNetworkWriterFactory");

        registerAllServices(bc, network_writer_factory, cx_writer_factory_properties);

        // Reader:
        final CyNetworkFactory network_factory = getService(bc, CyNetworkFactory.class);
        final CyRootNetworkManager root_network_manager = getService(bc, CyRootNetworkManager.class);
        final RenderingEngineManager rendering_engine_manager = getService(bc, RenderingEngineManager.class);
        final VisualStyleFactory visual_style_factory = getService(bc, VisualStyleFactory.class);
        final CyGroupFactory group_factory = getService(bc, CyGroupFactory.class);
        final CytoscapeCxFileFilter cxfilter = new CytoscapeCxFileFilter(new String[] { "cx" },
                                                                                   new String[] { "application/json" },
                                                                                  "CX JSON",
                                                                                   DataCategory.NETWORK,
                                                                                   streamUtil);

        final VisualMappingFunctionFactory vmfFactoryC = getService(bc,
                                                                    VisualMappingFunctionFactory.class,
                                                                    "(mapping.type=continuous)");
        final VisualMappingFunctionFactory vmfFactoryD = getService(bc,
                                                                    VisualMappingFunctionFactory.class,
                                                                    "(mapping.type=discrete)");
        final VisualMappingFunctionFactory vmfFactoryP = getService(bc,
                                                                    VisualMappingFunctionFactory.class,
                                                                    "(mapping.type=passthrough)");

        final CytoscapeCxNetworkReaderFactory cx_reader_factory = new CytoscapeCxNetworkReaderFactory(cxfilter,
                                                                                                      application_manager,
                                                                                                      network_factory,
                                                                                                      network_manager,
                                                                                                      root_network_manager,
                                                                                                      visual_mapping_manager,
                                                                                                      visual_style_factory,
                                                                                                      group_factory,
                                                                                                      rendering_engine_manager,
                                                                                                      network_view_factory,
                                                                                                      vmfFactoryC,
                                                                                                      vmfFactoryD,
                                                                                                      vmfFactoryP,
                                                                                                      layoutManager

        );
        final Properties reader_factory_properties = new Properties();

        // This is the unique identifier for this reader. 3rd party developer
        // can use this service by using this ID.
        reader_factory_properties.put(ID, "cytoscapeCxNetworkReaderFactory");
        registerService(bc, cx_reader_factory, InputStreamTaskFactory.class, reader_factory_properties);
*/
        //
        registerService(bc,new GraphSpaceNetworkAboutToBeDestroyedListener(), NetworkAboutToBeDestroyedListener.class, new Properties());
        
    }

}
