package org.openntf.domino.design;

import java.util.Date;

/**
 * Used as Design-based access to shared field $TemplateBuild. This can be used to define template version settings, which are then
 * displayed
 *
 * @author Paul Withers
 * @since 4.1.0
 *
 */
public interface TemplateBuildSharedFieldNote extends DesignBase {

	/**
	 * @return the template name stored in the Design note's $TemplateBuildName Item
	 */
	public String getTemplateBuildName();

	/**
	 * @param templateBuildName
	 *            to be stored in the Design note's $TemplateBuildName Item
	 */
	public void setTemplateBuildName(String templateBuildName);

	/**
	 * @return template build version stored in the Design note's $TemplateBuild Item
	 */
	public String getTemplateBuildVersion();

	/**
	 * @param templateBuildVersion
	 *            to be stored in the Design note's $TemplateBuild Item
	 */
	public void setTemplateBuildVersion(String templateBuildVersion);

	/**
	 * @return template build date stored (as DateTime object) in the Design note's $TemplateBuildDate Item
	 */
	public Date getTemplateBuildDate();

	/**
	 * @param date
	 *            to be stored (as DateTime object) in the Design note's $TemplateBuildDate Item
	 */
	public void setTemplateBuildDate(Date date);

}
