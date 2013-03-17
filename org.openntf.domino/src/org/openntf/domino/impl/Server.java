/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.impl;

import java.util.Collection;
import java.util.List;

import lotus.notes.addins.ServerAccess;
import lotus.notes.addins.ServerInfo;

import org.openntf.domino.Database;

public class Server implements org.openntf.domino.Server {

	@Override
	public boolean checkServerAccess(String userName, Collection<ServerAccess> serverAccessLevel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkServerAccess(String userName, ServerAccess serverAccessLevel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAbbreviatedName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCanonicalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<org.openntf.domino.Server> getClusterMembers(boolean includeCurrentServer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClusterName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Database getDatabaseByFilePath(String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Database getDatabaseByFilePath(String filePath, boolean failOver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Database getDatabaseByReplicaId(String replicaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Database getDatabaseByReplicaId(String replicaId, boolean failOver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlatform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getServerDocField(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getServerDocField(String fieldName, String unknown) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getServerInfo(ServerInfo serverInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getUserNamesList(Collection<String> userNames) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getUserNamesList(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCluster() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLocal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSocial() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshDesign(Database db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesign(Database db, boolean refresh) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesign(String dbFilePath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesign(String dbFilePath, boolean refresh) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesignFromLocal(Database db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesignFromLocal(Database db, boolean refresh) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesignFromLocal(String dbFilePath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesignFromLocal(String dbFilePath, boolean refresh) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesignFromServer(Database db, String serverName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesignFromServer(Database db, String serverName, boolean refresh) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesignFromServer(String dbFilePath, String serverName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshDesignFromServer(String dbFilePath, String serverName, boolean refresh) {
		// TODO Auto-generated method stub

	}

	@Override
	public String sendConsoleCommand(String command) {
		// TODO Auto-generated method stub
		return null;
	}

}
