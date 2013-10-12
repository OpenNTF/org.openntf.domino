<?xml version="1.0" ?>
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