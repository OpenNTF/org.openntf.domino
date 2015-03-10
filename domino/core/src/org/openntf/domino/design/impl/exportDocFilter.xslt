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

	<!-- Indent the result tree for more consistency when doing a diff - and it looks much nicer :) -->
	<xsl:output method="xml" indent="yes" xslt:indent-amount="4" />
	<xsl:output encoding="UTF-8" />

	<!-- Strip whitespace so that when we remove elements it does not leave	ugly blank gaps -->
	<xsl:strip-space elements="*" />

	<!-- Start: Removal templates Each of the following templates are designed 
		to match an element or attribute that we do not want in the result tree. -->
	<!-- Filter for Documents. delete FOCONIS history stuff -->
	<xsl:template match="//n:document/n:item[starts-with(@n:name,'$FocHistory')]" />

	<!-- Filter for Documents. We just copy the noteinfo/@unid and all items in alphabetical order -->
	<xsl:template match="//n:document">
		<xsl:copy>
			<xsl:attribute name="form">
       			<xsl:value-of select="@form" />
      		</xsl:attribute>
			<xsl:element name="noteinfo">
				<xsl:attribute name="unid">
          			<xsl:value-of select="n:noteinfo/@unid" />
        		</xsl:attribute>
			</xsl:element>
			<xsl:apply-templates select="n:item">
				<xsl:sort select="name()" />
				<xsl:sort select="@*" />
			</xsl:apply-templates>
		</xsl:copy>
	</xsl:template>
	
</xsl:stylesheet>
