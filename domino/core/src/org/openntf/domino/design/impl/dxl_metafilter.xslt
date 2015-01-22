<xsl:stylesheet version="1.0" xmlns="http://www.lotus.com/dxl"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n="http://www.lotus.com/dxl">
	<!-- It is not a bad idea to force UTF-8 output -->
	<xsl:output encoding="UTF-8" />


	<!-- skip all <filedata> tags in fileresources -->
	<xsl:template match="//fileresource/filedata"/>
	<xsl:template match="//stylesheetresource/filedata"/>
	
	<!-- clean up image resources -->
	<xsl:template match="//imageresource/gif"/>
	<xsl:template match="//imageresource/png"/>
	<xsl:template match="//imageresource/jpeg"/>		
	<xsl:template match="//imageresource/item[@name='$FileSize']"/>
	
	
	<xsl:template match="//note/item[@name='$FileData']"/>
	<xsl:template match="//note/item[@name='$FileSize']"/>
	
	<!-- ClassIndexItem/ClassSize/ClassData contains the compiled java code -->
	<xsl:template match="//note/item[@name='$ClassIndexItem']"/>
	<xsl:template match="//note/item[starts-with(@name,'$ClassSize')]"/>
	<xsl:template match="//note/item[starts-with(@name,'$ClassData')]"/>

	<!-- LotusScript libraries are exported in raw form, because the LSS-file is easier to extract -->
	<xsl:template match="//note/item[@name='$ScriptLib']"/>
	<xsl:template match="//note/item[@name='$ScriptLib_O']"/>
	<xsl:template match="//note/item[@name='$JavaScriptLibrary']"/>

	<!-- The .xsp-config file in CustomControls -->
	<xsl:template match="//note/item[@name='$ConfigSize']"/>
	<xsl:template match="//note/item[@name='$ConfigData']"/>

	<!-- copy all nodes that do not match a rule above -->
	<xsl:template match="node() | @*" name="identity">
		<xsl:copy>
			<xsl:apply-templates select="node() | @*" />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
