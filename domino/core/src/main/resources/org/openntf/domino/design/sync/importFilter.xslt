<?xml version="1.0"?>
<!--

Copyright 2013 Cameron Gregor, 2015 FOCONIS AG
(Main idea is based on "dora")

Licensed under the Apache License, Version 2.0 (the "License"); 

you may not use this file except in compliance with the License. 
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, 
software distributed under the License is distributed on an 
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific 
language governing permissions and limitations under the License

-->
<xsl:stylesheet version="1.0" xmlns="http://www.lotus.com/dxl"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n="http://www.lotus.com/dxl"
	xmlns:redirect="http://xml.apache.org/xalan/redirect"
	extension-element-prefixes="redirect" xmlns:xslt="http://xml.apache.org/xslt">
	<!-- Don't indent the result tree -->
	<xsl:output method="xml" indent="no" />
	<xsl:output encoding="UTF-8" />
	
	<!-- Strip whitespace so that when we remove elements it does not leave	ugly blank gaps 
		but not for certain problematic elements-->
	<xsl:preserve-space	elements="javascript|item[@name='$FileData']|rawitemdata|filedata" />
	<xsl:strip-space elements="*" />

	<xsl:template match="javascript[not(text())]">
		<javascript>// nop</javascript>
	</xsl:template>

	<xsl:template match="node() | @*" name="identity">
		<xsl:copy>
			<xsl:apply-templates select="node() | @*" />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
