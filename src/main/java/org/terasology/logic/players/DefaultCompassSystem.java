// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.logic.players;

import org.terasology.compass.rendering.nui.layers.CompassHUDElement;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.geom.Rect2f;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.nui.databinding.ReadOnlyBinding;

@RegisterSystem
public class DefaultCompassSystem extends BaseComponentSystem {

    public static final String HUD_ELEMENT_COMPASS_ID = "compass";
    private CompassHUDElement compassElement;

    @In
    private NUIManager nuiManager;

    @In
    private LocalPlayer localPlayer;

    @Override
    public void initialise() {
        Rect2f rc = Rect2f.createFromMinAndSize(0, 0, 1, 1);
        compassElement = nuiManager.getHUD().addHUDElement(HUD_ELEMENT_COMPASS_ID, CompassHUDElement.class, rc);
        compassElement.bindTargetEntity(new ReadOnlyBinding<EntityRef>() {
            @Override
            public EntityRef get() {
                return localPlayer.getCharacterEntity();
            }
        });
    }

    public EntityRef characterEntity() {
        return localPlayer.getCharacterEntity();
    }
}
