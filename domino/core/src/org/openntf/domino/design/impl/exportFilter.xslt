<?xml version="1.0"?>
<!--

Copyright 2013 Cameron Gregor
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License

-->
<xsl:stylesheet version="1.0" xmlns="http://www.lotus.com/dxl" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n="http://www.lotus.com/dxl" xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect" xmlns:xslt="http://xml.apache.org/xslt">
  <!-- Indent the result tree for more consistency when doing a diff - and it looks much nicer :) -->
  <xsl:output method="xml" indent="yes" xslt:indent-amount="4"/>
  <xsl:output encoding="UTF-8"/>
<!-- Strip whitespace so that when we remove elements it does not leave ugly blank gaps 
  <xsl:strip-space elements="*"/>-->
<!-- 
  Start: Removal templates
    
    Each of the following templates are designed to match an element or attribute that 
    we do not want in the result tree.

-->
  <!-- Filter for Documents. We just copy the noteinfo/@unid and all items in alphabetical order -->
  <xsl:template match="//n:document">
    <xsl:copy>
      <xsl:attribute name="form">
        <xsl:value-of select="@form"/>
      </xsl:attribute>
      <xsl:element name="noteinfo">
        <xsl:attribute name="unid">
          <xsl:value-of select="n:noteinfo/@unid"/>
        </xsl:attribute>
      </xsl:element>
      <xsl:apply-templates select="n:item">
        <xsl:sort select="name()"/>
        <xsl:sort select="@*"/>
      </xsl:apply-templates>
    </xsl:copy>
  </xsl:template>
  <!-- Designelements will start here -->
  <!-- We Don't want the NoteInfo element. We don't care who updated or signed the element  -->
  <xsl:template match="noteinfo|updatedby|wassignedby|revisions"/>
  <!-- For binary DXL elements replicaid and version are stored on a <note> element -->
  <xsl:template match="//@replicaid"/>
  <xsl:template match="//@version"/>
  <xsl:template match="//@designerversion"/>
  <xsl:template match="//@fromtemplate"/>
  <xsl:template match="//@maintenanceversion"/>
<!-- 
         The following templates cover the replicaid, version and designerversion
         attributes on the Standard DXL elements.
    -->
  <!-- END Standard DXL replicaid, version, designerversion templates -->
<!-- 
         For Agent Non-Binary DXL 
         For both LotusScript and Java agents
      
         For Java Agents You may also wish to look at some extra ones like javaproject->codepath or
         item->$JavaCompilerSource item->$JavaComplierTarget

    -->
  <xsl:template match="//n:agent/n:rundata"/>
  <xsl:template match="//n:agent/n:designchange"/>
  <xsl:template match="//n:javaproject/@codepath"/>
  <!-- not 100% but I don't like the sound of These! Not in the DTD in help anyway -->
  <xsl:template match="//n:folder/@formatnoteid"/>
  <xsl:template match="//n:view/@formatnoteid"/>
<!-- 
        For the Database Properties Non-Binary DXL.
        Most of these attributes/elements are guaranteed to be different on different developer copies
    -->
  <xsl:template match="//n:database/@path"/>
  <xsl:template match="//n:database/n:databaseinfo/@dbid"/>
  <xsl:template match="//n:database/n:databaseinfo/@percentused"/>
  <xsl:template match="//n:database/n:databaseinfo/@numberofdocuments"/>
  <xsl:template match="//n:database/n:databaseinfo/@diskspace"/>
  <xsl:template match="//n:database/n:databaseinfo/@odsversion"/>
  <xsl:template match="//n:database/n:databaseinfo/n:datamodified"/>
  <xsl:template match="//n:database/n:databaseinfo/n:designmodified"/>
  <xsl:template match="//n:database/n:fulltextsettings"/>
<!-- 
        Ignore the database ACL
        Note: You may or may not want to do this! 
        In my case I have no need to keep acl in source repo.
    -->
  <xsl:template match="//n:database/n:acl"/>
<!-- 
         Remove any items that begin with $ and end with _O
         for example
         <item name="$yourfield_O" ....></item>

         These Items are Script Object items, they are not source code!
         you freshly check out a repo version of the design element, but at
         least you won't get merge conflicts all the time

         These items will come back to the Design Element after a recompile, they
         just won't end up in your repository, which is good news, because they are like .class files.
     -->
  <xsl:template match="//n:item">
    <xsl:if test="not(starts-with(@name,'$') and substring(@name,string-length(@name)-1,2) = '_O')">
      <xsl:call-template name="identity"/>
    </xsl:if>
  </xsl:template>
  <!-- Ignore the DesignerVersion Item  and this random FileModDT one -->
  <xsl:template match="//n:item[@name='$DesignerVersion']"/>
  <xsl:template match="//n:item[@name='$$ScriptName']"/>
  <xsl:template match="//n:item[@name='$ScriptLib_error']"/>
  <xsl:template match="//n:item[@name='$Class']"/>
  <xsl:template match="//n:item[@name='$Comment']"/>
  <xsl:template match="//n:item[@name='TmpViewDesignCollation']"/>
  <xsl:template match="//n:item/@sign"/>
  <xsl:template match="//n:item[@name='$CIAOOwner']"/>
  <xsl:template match="//n:item[@name='$CIAOTime']"/>
  <xsl:template match="//n:item[@name='$CIAOCheckOutTime']"/>
  <xsl:template match="//n:imageresource/n:item[@name='$FileModDT']"/>
  <xsl:template match="//n:imageresource/n:item[@name='$EditFilePath']"/>
<!-- 
         For any node not specified in one of the above templates, 
         simply copy it to the result tree.
         This template is also named so it can be called by call-template
    -->
  <xsl:template match="node() | @*" name="identity">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
