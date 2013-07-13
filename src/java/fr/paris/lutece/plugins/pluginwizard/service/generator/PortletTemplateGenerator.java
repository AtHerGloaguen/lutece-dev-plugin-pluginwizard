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

import fr.paris.lutece.plugins.pluginwizard.business.model.PluginModel;
import fr.paris.lutece.plugins.pluginwizard.business.model.Portlet;
import static fr.paris.lutece.plugins.pluginwizard.service.generator.Markers.*;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * The generator produced the templates necessary for the handling of portlets
 */
public class PortletTemplateGenerator implements Generator
{
    private static final String TEMPLATE_PORTLET_HTML_TEMPLATE = "/skin/plugins/pluginwizard/templates/pluginwizard_portlet_template_files.html";
    private static final String EXT_HTML = ".html";

    /**
     * {@inheritDoc }
     */
    @Override
    public Map generate( PluginModel pm )
    {
        HashMap map = new HashMap(  );
        String strBasePath = "plugin-{plugin_name}/webapp/WEB-INF/templates/admin/plugins/{plugin_name}/portlet/";
        strBasePath = strBasePath.replace( "{plugin_name}", pm.getPluginName(  ) );

        for ( Portlet portlet : pm.getPortlets() )
        {
            for ( int i = 1; i < 4; i++ )
            {
                String strPortlet = portlet.getPluginPortletTypeName(  );
                int nIndex = strPortlet.lastIndexOf( "_" );

                String strPortletFile = getPortletTemplateName( strPortlet.substring( 0, nIndex ).toLowerCase(  ), i );

                String strPath = strBasePath + strPortletFile;

                String strSourceCode = getPortletHtmlTemplate( portlet, pm.getPluginName(  ), i );
                strSourceCode = strSourceCode.replace( "&lt;", "<" );
                strSourceCode = strSourceCode.replace( "&gt;", ">" );
                strSourceCode = strSourceCode.replace( "@@", "#" );
                map.put( strPath, strSourceCode );
            }
        }

        return map;
    }

    /**
     * Chooses the name of the template
     *
     * @param strPortletName The name of the portlet
     * @param nTemplate The type of template
     * @return The name of the tempate
     */
    private String getPortletTemplateName( String strPortletName, int nTemplate )
    {
        String strReturn;

        switch ( nTemplate )
        {
            case 1:
                strReturn = "combo_feed_" + strPortletName + EXT_HTML;

                break;

            case 2:
                strReturn = "modify_portlet_" + strPortletName + EXT_HTML;

                break;

            case 3:
                strReturn = "create_portlet_" + strPortletName + EXT_HTML;

                break;

            default:
                strReturn = "combo_feed_" + strPortletName + EXT_HTML;

                break;
        }

        return strReturn;
    }

    /**
    * Produces text content of the html template for a portlet
    * @param portlet The instance of a portlet
    * @param strPluginName The plugin name
    * @param nPortletTemplateType The type of portlet
    * @return The content of the html template
    */
    private String getPortletHtmlTemplate( Portlet portlet, String strPluginName, int nPortletTemplateType )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_I18N_BRACKETS_OPEN, "@@i18n{" );
        model.put( MARK_I18N_BRACKETS_CLOSE, "}" );
        model.put( MARK_BRACKETS_OPEN, "${" );
        model.put( MARK_BRACKETS_CLOSE, "}" );
        model.put( MARK_MACRO, "@" );
        model.put( MARK_PORTLET, portlet );
        model.put( MARK_PLUGIN_NAME, strPluginName );
        model.put( MARK_PORTLET_TEMPLATE_TYPE, nPortletTemplateType + "" );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PORTLET_HTML_TEMPLATE, Locale.getDefault(  ),
                model );

        return template.getHtml(  );
    }
}
