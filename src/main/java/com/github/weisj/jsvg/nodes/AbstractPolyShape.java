/*
 * MIT License
 *
 * Copyright (c) 2021-2022 Jannis Weis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.github.weisj.jsvg.nodes;

import java.awt.*;
import java.awt.geom.Path2D;

import org.jetbrains.annotations.NotNull;

import com.github.weisj.jsvg.attributes.FillRule;
import com.github.weisj.jsvg.geometry.AWTSVGShape;
import com.github.weisj.jsvg.geometry.FillRuleAwareAWTSVGShape;
import com.github.weisj.jsvg.geometry.MeasurableShape;
import com.github.weisj.jsvg.nodes.prototype.HasFillRule;
import com.github.weisj.jsvg.parser.AttributeNode;

public abstract class AbstractPolyShape extends ShapeNode implements HasFillRule {

    private FillRule fillRule;

    @Override
    public @NotNull FillRule fillRule() {
        return fillRule;
    }

    @Override
    protected final @NotNull MeasurableShape buildShape(@NotNull AttributeNode attributeNode) {
        fillRule = FillRule.parse(attributeNode);
        float[] points = attributeNode.getFloatList("points");
        if (points.length > 0) {
            Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD, points.length / 2);
            path.moveTo(points[0], points[1]);
            for (int i = 2; i < points.length; i += 2) {
                path.lineTo(points[i], points[i + 1]);
            }
            if (doClose()) path.closePath();
            return new FillRuleAwareAWTSVGShape(path);
        } else {
            return new AWTSVGShape<>(new Rectangle());
        }
    }

    protected abstract boolean doClose();
}
