// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.compass.rendering.nui.layers;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.Direction;
import org.terasology.nui.Border;
import org.terasology.nui.Canvas;
import org.terasology.nui.CoreWidget;
import org.terasology.nui.databinding.Binding;
import org.terasology.nui.databinding.DefaultBinding;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.assets.mesh.Mesh;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.nui.CanvasUtility;
import org.terasology.utilities.Assets;

public class CompassWindow extends CoreWidget {
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

        Rectanglei screenArea = new Rectanglei(arrowX, arrowY).setSize(arrowWidth, arrowHeight);

        // UITexture should be used here, but it doesn't work
        Material material = Assets.getMaterial("engine:UILitMesh").get();
        material.setTexture("texture", arrowhead);
        Mesh mesh = Assets.getMesh("engine:UIBillboard").get();
        // The scaling seems to be completely wrong - 0.8f looks ok
        Quaternionf q = locationComponent.getWorldRotation(new Quaternionf());

        Vector3f forward = q.transform(Direction.FORWARD.asVector3f(), new Vector3f());
        Vector3f left = forward.cross(Direction.UP.asVector3f(), new Vector3f());
        Vector3f orthoForward = left.cross(Direction.UP.asVector3f(), new Vector3f());

        AxisAngle4f ax = new AxisAngle4f();
        new Quaternionf().rotationTo(Direction.FORWARD.asVector3f(), orthoForward).get(ax);
        CanvasUtility.drawMesh(canvas, mesh, material, screenArea,
            new Quaternionf().setAngleAxis(-ax.angle * Math.signum(ax.y) + Math.PI, 0, 0, 1), new Vector3f(0, 0, 0),
            0.8f);
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
