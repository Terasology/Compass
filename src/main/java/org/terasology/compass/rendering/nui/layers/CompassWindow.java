// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.compass.rendering.nui.layers;


import org.joml.primitives.Rectanglei;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.JomlUtil;
import org.terasology.math.geom.Quat4f;
import org.joml.Vector2i;
import org.terasology.math.geom.Vector3f;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.assets.mesh.Mesh;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.nui.Border;
import org.terasology.nui.Canvas;
import org.terasology.nui.CoreWidget;
import org.terasology.nui.databinding.Binding;
import org.terasology.nui.databinding.DefaultBinding;
import org.terasology.rendering.nui.CanvasUtility;
import org.terasology.utilities.Assets;


public class CompassWindow extends CoreWidget {
    //private UIText compass;
    private static final Logger logger = LoggerFactory.getLogger(CompassWindow.class);
    private Binding<EntityRef> targetEntityBinding = new DefaultBinding<>(EntityRef.NULL);

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        Border border = canvas.getCurrentStyle().getBackgroundBorder();
        Vector2i size = getPreferredContentSize();
        int width = size.x + border.getTotalWidth();
        int height = size.y + border.getTotalHeight();
        return new Vector2i(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        EntityRef entity = getTargetEntity();
        LocationComponent locationComponent = entity.getComponent(LocationComponent.class);
        if (locationComponent == null) {
            return;
        }
        drawNeedle(canvas, locationComponent);
    }

    private void drawNeedle(Canvas canvas, LocationComponent locationComponent) {
        // draw compass-needle
        Texture arrowhead = Assets.getTexture("Compass:compass-needle").get();
        // Drawing textures with rotation is not yet supported, see #1926
        // We therefore use a workaround based on mesh drawing
        // The width of the screenArea is doubled to avoid clipping issues when the texture is rotated
        int width = getPreferredContentSize().x();
        int height = getPreferredContentSize().y();
        int arrowWidth = arrowhead.getWidth() * 2;
        int arrowHeight = arrowhead.getHeight() * 2;
        int arrowX = (width - arrowWidth) / 2;
        int arrowY = (height - arrowHeight) / 2;
        Rectanglei screenArea = JomlUtil.rectangleiFromMinAndSize(arrowX, arrowY, arrowWidth, arrowHeight);

        // UITexture should be used here, but it doesn't work
        Material material = Assets.getMaterial("engine:UILitMesh").get();
        material.setTexture("texture", arrowhead);
        Mesh mesh = Assets.getMesh("engine:UIBillboard").get();
        // The scaling seems to be completely wrong - 0.8f looks ok
        Quat4f q = locationComponent.getWorldRotation();

        float rotation = q.getYaw();
        CanvasUtility.drawMesh(canvas, mesh, material, JomlUtil.from(screenArea), new Quat4f(0, 0, rotation), new Vector3f(0, 0, 0), 0.8f);
    }


    public void bindTargetEntity(Binding<EntityRef> binding) {
        targetEntityBinding = binding;
    }

    public EntityRef getTargetEntity() {
        return targetEntityBinding.get();
    }

    public void setTargetEntity(EntityRef val) {
        targetEntityBinding.set(val);
    }

    public Vector2i getPreferredContentSize() {
        return new Vector2i(50, 50);
    }

}
