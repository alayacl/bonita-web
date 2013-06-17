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
package org.bonitasoft.web.rest.server.model.builder.profile;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileItem;

/**
 * @author Colin PUY
 * 
 */
public class ProfileItemBuilder {

    private long id = 1L;
    private String name = "aName";
    private String description = "aDescription";
    private String icon = "anIcon";

    public static ProfileItemBuilder aProfileItem() {
        return new ProfileItemBuilder();
    }

    public ProfileItem build() {
        ProfileItem item = new ProfileItem();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setIcon(icon);
        return item;
    }

    public ProfileItemBuilder fromEngineItem(Profile profile) {
        id = profile.getId();
        name = profile.getName();
        description = profile.getDescription();
        icon = profile.getIconPath();
        return this;
    }

    public ProfileItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProfileItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
}
