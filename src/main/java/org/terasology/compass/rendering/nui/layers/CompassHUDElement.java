// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.compass.rendering.nui.layers;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.rendering.nui.layers.hud.CoreHudWidget;
import org.terasology.nui.databinding.ReadOnlyBinding;

public class CompassHUDElement extends CoreHudWidget {
    private static final String COMPAS_WINDOW_WIDGET_ID = "compassWindow";
    private CompassWindow compassWindow;

    @Override
    public void initialise() {
        compassWindow = find(COMPAS_WINDOW_WIDGET_ID, CompassWindow.class);
    }

    public void bindTargetEntity(ReadOnlyBinding<EntityRef> binding) {
        compassWindow.bindTargetEntity(binding);
    }
}
