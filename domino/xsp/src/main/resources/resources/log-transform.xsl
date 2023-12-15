<?xml version="1.0" ?>
<!--

    Copyright © 2013-2023 The OpenNTF Domino API Team

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
           <table id="eventTable" border="1" cellspacing="1" cellpadding="5" width="90%">
            <tr style="background:#ACD6FF;">
              
              <th width="15%"><a id="Time" style="color:#0000FF; text-decoration:none;">Time</a></th>
              <th width="10%"><a id="Severity" style="color:#0000FF; text-decoration:none;">Severity</a></th>
              <th width="45%"><a id="Message" style="color:#0000FF; text-decoration:none;">Message</a></th>
              <th width="15%"><a id="SubSystem" style="color:#0000FF; text-decoration:none;">SubSystem</a></th>
              </tr>          
            <xsl:for-each select="CommonBaseEvents/CommonBaseEvent">
              <tr>                            
		    <xsl:choose>
                  <xsl:when test="string(@creationTime)">
                    <td><xsl:value-of select="@creationTime"/></td>
                  </xsl:when>
                  <xsl:otherwise>
                    <td>&#xA0;</td>
                  </xsl:otherwise>
                </xsl:choose>
             <xsl:choose>
                  <xsl:when test="string(extendedDataElements[@name='CommonBaseEventLogRecord:level']/children[@name='CommonBaseEventLogRecord:name']/values)">
                    <td><xsl:value-of select="extendedDataElements[@name='CommonBaseEventLogRecord:level']/children[@name='CommonBaseEventLogRecord:name']/values"/></td>
                  </xsl:when>
                  <xsl:otherwise>
                    <td>&#xA0;</td>
                  </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                  <xsl:when test="string(@msg)">
                    <td><xsl:value-of select="@msg"/></td>
                  </xsl:when>
                  <xsl:otherwise>
                    <td>&#xA0;</td>
                  </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                  <xsl:when test="string(sourceComponentId/@subComponent)">
                    <td><xsl:value-of select="sourceComponentId/@subComponent"/></td>
                  </xsl:when>
                  <xsl:otherwise>
                    <td>&#xA0;</td>
                  </xsl:otherwise>
             </xsl:choose> 
              </tr>
            </xsl:for-each>
          </table>
  </xsl:template>
</xsl:stylesheet>