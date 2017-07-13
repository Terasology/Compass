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
package org.terasology.compass;


import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;
import org.terasology.rendering.nui.layers.hud.CoreHudWidget;
import org.terasology.rendering.nui.widgets.UILoadBar;
import org.terasology.rendering.nui.databinding.ReadOnlyBinding;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.characters.CharacterMoveInputEvent;
import org.terasology.rendering.nui.widgets.UIText;


public class CompassWindow extends CoreHudWidget{
    UIText compass;
    @In
    LocalPlayer localPlayer;

    @Override
    public void initialise(){
        compass = find("compass", UIText.class);
        compass.bindVisible(new ReadOnlyBinding<Boolean>() {
            @Override
            public Boolean get() {
                EntityRef character = localPlayer.getCharacterEntity();
                return character != null;
            }
        });
    }

    @ReceiveEvent
    public void characterMoved(CharacterMoveInputEvent event){
        compass.setText(Float.toString(event.getYaw()));
    }

}
