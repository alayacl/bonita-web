/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.organization;

import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.identity.GroupCriterion;
import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.organization.GroupDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasFiles;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.api.APIHasUpdate;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Nicolas Tith
 * 
 */
public class APIGroup extends ConsoleAPI<GroupItem> implements
        APIHasAdd<GroupItem>,
        APIHasGet<GroupItem>,
        APIHasDelete,
        APIHasSearch<GroupItem>,
        APIHasUpdate<GroupItem>,
        APIHasFiles {

    private static final String GROUPS_ICON_FOLDER_PATH = "/" + WebBonitaConstants.GROUPS_ICONS_FOLDER_NAME;

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(GroupDefinition.TOKEN);
    }

    @Override
    public GroupItem add(final GroupItem item) {
        return new GroupDatastore(getEngineSession()).add(item);
    }

    @Override
    public GroupItem update(final APIID id, final Map<String, String> item) {
        return new GroupDatastore(getEngineSession()).update(id, item);
    }

    @Override
    public GroupItem get(final APIID id) {
        return new GroupDatastore(getEngineSession()).get(id);
    }

    @Override
    public String defineDefaultSearchOrder() {
        return GroupCriterion.NAME_ASC.toString();
    }

    @Override
    public ItemSearchResult<GroupItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        return new GroupDatastore(getEngineSession()).search(page, resultsByPage, search, orders, filters);
    }

    @Override
    public void delete(final List<APIID> ids) {
        new GroupDatastore(getEngineSession()).delete(ids);
    }

    @Override
    protected void fillDeploys(final GroupItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final GroupItem item, final List<String> counters) {
        if (counters.contains(GroupItem.COUNTER_NUMBER_OF_USERS)) {
            item.setAttribute(GroupItem.COUNTER_NUMBER_OF_USERS,
                    new GroupDatastore(getEngineSession()).getNumberOfUsers(item.getId())
                    );
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // APIHasFiles
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getUploadPath(final String attributeName) {
        if (GroupItem.ATTRIBUTE_ICON.equals(attributeName)) {
            return WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId()).getConsoleGroupIconsFolder().getPath();
        }
        return null;
    }

    @Override
    public String getSavedPathPrefix(final String attributeName) {
        if (GroupItem.ATTRIBUTE_ICON.equals(attributeName)) {
            return GROUPS_ICON_FOLDER_PATH;
        }
        return null;
    }

}
