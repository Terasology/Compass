/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.compass.ui;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.location.LocationComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.Quat4f;
import org.terasology.network.ServerInfoMessage;
import org.terasology.registry.In;
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.databinding.ReadOnlyBinding;
import org.terasology.rendering.nui.layers.hud.CoreHudWidget;
import org.terasology.rendering.nui.widgets.UIText;

import java.util.concurrent.Future;


public class CompassWindow extends CoreHudWidget {
    //private UIText compass;
    private static final Logger logger = LoggerFactory.getLogger(CompassWindow.class);

    @In
    private LocalPlayer localPlayer;

    @Override
    public void initialise() {
        UIText compass = find("test", UIText.class);
        compass.bindVisible(new ReadOnlyBinding<Boolean>() {
            @Override
            public Boolean get() {
                EntityRef character = localPlayer.getCharacterEntity();
                logger.info("This is shit" + (character != null));
                return character != null;
            }
        });
        compass.bindText(new ReadOnlyBinding<String>() {
             @Override
             public String get() {
                 EntityRef character = localPlayer.getCharacterEntity();
                 Quat4f rotation = character.getComponent(LocationComponent.class).getWorldRotation();
                 return "" + rotation.getYaw()*180/TeraMath.PI;
             }
         }
        );
    }
}
