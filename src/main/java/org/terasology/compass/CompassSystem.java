package org.terasology.compass;

import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;


@RegisterSystem(RegisterMode.AUTHORITY)
public class CompassSystem {
    @In
    private LocalPlayer localPlayer;
}
