/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.profile;

import static org.bonitasoft.web.rest.model.portal.profile.AbstractMemberItem.ATTRIBUTE_GROUP_ID;
import static org.bonitasoft.web.rest.model.portal.profile.AbstractMemberItem.ATTRIBUTE_ROLE_ID;
import static org.bonitasoft.web.rest.model.portal.profile.AbstractMemberItem.ATTRIBUTE_USER_ID;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.portal.profile.AbstractMemberItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.organization.GroupDatastore;
import org.bonitasoft.web.rest.server.datastore.organization.RoleDatastore;
import org.bonitasoft.web.rest.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIAttributesException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class AbstractAPIMember<T extends AbstractMemberItem> extends ConsoleAPI<T> implements
        APIHasAdd<T>,
        APIHasSearch<T>,
        APIHasDelete
{

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected void checkAttributes(final APIID userId, final APIID roleId, final APIID groupId) {
        checkUserAttributeIsAloneOrNull(userId, roleId, groupId);
        checkAnAttributeIsSet(userId, roleId, groupId);
    }

    private void checkUserAttributeIsAloneOrNull(final APIID userId, final APIID roleId, final APIID groupId) {
        if (userId != null && (roleId != null || groupId != null)) {
            throw new APIAttributesException(Arrays.asList(ATTRIBUTE_USER_ID, ATTRIBUTE_ROLE_ID, ATTRIBUTE_GROUP_ID), "User attribute must be alone or null");
        }
    }

    private void checkAnAttributeIsSet(final APIID userId, final APIID roleId, final APIID groupId) {
        if (userId == null && roleId == null && groupId == null) {
            throw new APIAttributesException(Arrays.asList(ATTRIBUTE_USER_ID, ATTRIBUTE_ROLE_ID, ATTRIBUTE_GROUP_ID), "At least one attribute must be set");
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CRUDS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public T add(final T item) {

        checkAttributes(item.getUserId(), item.getRoleId(), item.getGroupId());

        return super.add(item);
    }

    @Override
    public void delete(final List<APIID> ids) {
        // FIXME : uncomment when id in engine is deleted
        // for (APIID apiid : ids) {
        // checkAttributes(apiid.getPartAsAPIID(ATTRIBUTE_USER_ID), apiid.getPartAsAPIID(ATTRIBUTE_ROLE_ID), apiid.getPartAsAPIID(ATTRIBUTE_GROUP_ID));
        // }
        super.delete(ids);
    }

    @Override
    public ItemSearchResult<T> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        checkUserAttributeIsAloneOrNull(
                APIID.makeAPIID(filters.get(ATTRIBUTE_USER_ID)),
                APIID.makeAPIID(filters.get(ATTRIBUTE_ROLE_ID)),
                APIID.makeAPIID(filters.get(ATTRIBUTE_GROUP_ID)));
        return super.search(page, resultsByPage, search, orders, filters);
    }

    @Override
    protected void fillDeploys(final T item, final List<String> deploys) {
        if (isDeployable(ATTRIBUTE_USER_ID, deploys, item)) {
            item.setDeploy(ATTRIBUTE_USER_ID, new UserDatastore(getEngineSession()).get(item.getUserId()));
        }
        if (isDeployable(ATTRIBUTE_ROLE_ID, deploys, item)) {
            item.setDeploy(ATTRIBUTE_ROLE_ID, new RoleDatastore(getEngineSession()).get(item.getRoleId()));
        }
        if (isDeployable(ATTRIBUTE_GROUP_ID, deploys, item)) {
            item.setDeploy(ATTRIBUTE_GROUP_ID, new GroupDatastore(getEngineSession()).get(item.getGroupId()));
        }
    }

}
