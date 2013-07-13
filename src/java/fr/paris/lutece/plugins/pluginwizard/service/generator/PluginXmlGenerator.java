/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.pluginwizard.service.generator;

import fr.paris.lutece.plugins.pluginwizard.business.model.BusinessClass;
import fr.paris.lutece.plugins.pluginwizard.business.model.Feature;
import fr.paris.lutece.plugins.pluginwizard.business.model.PluginModel;
import fr.paris.lutece.plugins.pluginwizard.service.ModelService;
import static fr.paris.lutece.plugins.pluginwizard.service.generator.Markers.*;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 *
 * Class produces the xml file describing the generated plugin
 *
 */
public class PluginXmlGenerator implements Generator
{
    private static final String TEMPLATE_PLUGIN_XML_TEMPLATE = "/skin/plugins/pluginwizard/templates/pluginwizard_xml_template.html";

    /**
     * {@inheritDoc }
     */
    @Override
    public Map generate( PluginModel pm )
    {
        HashMap map = new HashMap(  );
        String strBasePath = "plugin-{plugin_name}/webapp/WEB-INF/plugins/";
        strBasePath = strBasePath.replace( "{plugin_name}", pm.getPluginName(  ) );

        strBasePath = strBasePath + pm.getPluginName(  ).toLowerCase(  ) + ".xml";

        String strSourceCode = getPluginXmlCode( pm );
        map.put( strBasePath, strSourceCode );

        return map;
    }

    /**
    * Returns the text content of the plugin xml file
    * @param nPluginId The id of the plugin
    * @param plugin The plugin
    * @return The plugin xml file content of the plugin
    */
    private String getPluginXmlCode( PluginModel pm )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        for( Feature feature : pm.getFeatures() )
        {
            List<BusinessClass> listBusiness = ModelService.getBusinessClassesByFeature( pm.getIdPlugin(), feature.getId() );
            if( ( listBusiness != null ) && (listBusiness.size() > 0 ))
            {
                BusinessClass businessClass = listBusiness.get( 0 );
                feature.setJspName( "Manage" + businessClass.getBusinessClassCapsFirst() + "s.jsp" );
            }
            else
            {
                feature.setJspName( feature.getPluginFeatureName() + ".jsp" );
            }
        }
        model.put( MARK_PLUGIN, pm );


        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PLUGIN_XML_TEMPLATE, Locale.getDefault(  ),
                model );

        return template.getHtml(  );
    }
}
