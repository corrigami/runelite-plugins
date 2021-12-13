package tictac7x.rooftops;

import net.runelite.api.Client;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.geometry.RectangleUnion;
import net.runelite.api.geometry.Shapes;
import net.runelite.api.geometry.SimplePolygon;
import net.runelite.api.model.Jarvis;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Perspective class to modify clickbox plane dynamically. Copied methods were private inside the original Perspective class.
 */
public class RooftopsPerspective {
    /**
     * @param plane_modifier - How much to change the clickbox plane.
     */
    public static Shape getClickbox(@Nonnull Client client, Model model, int orientation, LocalPoint point, final int plane_modifier)
    {
        if (model == null)
        {
            return null;
        }

        int x = point.getX();
        int y = point.getY();
        int z = net.runelite.api.Perspective.getTileHeight(client, point, client.getPlane() + plane_modifier);

        SimplePolygon bounds = calculateAABB(client, model, orientation, x, y, z);

        if (bounds == null)
        {
            return null;
        }

        if (model.isClickable())
        {
            return bounds;
        }

        Shapes<SimplePolygon> bounds2d = calculate2DBounds(client, model, orientation, x, y, z);
        if (bounds2d == null)
        {
            return null;
        }

        for (SimplePolygon poly : bounds2d.getShapes())
        {
            poly.intersectWithConvex(bounds);
        }

        return bounds2d;
    }

    public static SimplePolygon calculateAABB(Client client, Model m, int jauOrient, int x, int y, int z)
    {
        int ex = m.getExtremeX();
        if (ex == -1)
        {
            // dynamic models don't get stored when they render where this normally happens
            m.calculateBoundsCylinder();
            m.calculateExtreme(0);
            ex = m.getExtremeX();
        }

        int x1 = m.getCenterX();
        int y1 = m.getCenterZ();
        int z1 = m.getCenterY();

        int ey = m.getExtremeZ();
        int ez = m.getExtremeY();

        int x2 = x1 + ex;
        int y2 = y1 + ey;
        int z2 = z1 + ez;

        x1 -= ex;
        y1 -= ey;
        z1 -= ez;

        int[] xa = new int[]{
                x1, x2, x1, x2,
                x1, x2, x1, x2
        };
        int[] ya = new int[]{
                y1, y1, y2, y2,
                y1, y1, y2, y2
        };
        int[] za = new int[]{
                z1, z1, z1, z1,
                z2, z2, z2, z2
        };

        int[] x2d = new int[8];
        int[] y2d = new int[8];

        net.runelite.api.Perspective.modelToCanvas(client, 8, x, y, z, jauOrient, xa, ya, za, x2d, y2d);

        return Jarvis.convexHull(x2d, y2d);
    }

    public static Shapes<SimplePolygon> calculate2DBounds(Client client, Model m, int jauOrient, int x, int y, int z)
    {
        int[] x2d = new int[m.getVerticesCount()];
        int[] y2d = new int[m.getVerticesCount()];
        int[] faceColors3 = m.getFaceColors3();
        Perspective.modelToCanvas(client, m.getVerticesCount(), x, y, z, jauOrient, m.getVerticesX(), m.getVerticesZ(), m.getVerticesY(), x2d, y2d);
//        int radius = true;
        int[][] tris = new int[][]{m.getFaceIndices1(), m.getFaceIndices2(), m.getFaceIndices3()};
        int vpX1 = client.getViewportXOffset();
        int vpY1 = client.getViewportXOffset();
        int vpX2 = vpX1 + client.getViewportWidth();
        int vpY2 = vpY1 + client.getViewportHeight();
        List<RectangleUnion.Rectangle> rects = new ArrayList(m.getFaceCount());

        label56:
        for(int tri = 0; tri < m.getFaceCount(); ++tri) {
            if (faceColors3[tri] != -2) {
                int minX = 2147483647;
                int minY = 2147483647;
                int maxX = -2147483648;
                int maxY = -2147483648;
                int[][] var21 = tris;
                int var22 = tris.length;

                for(int var23 = 0; var23 < var22; ++var23) {
                    int[] vertex = var21[var23];
                    int idx = vertex[tri];
                    int xs = x2d[idx];
                    int ys = y2d[idx];
                    if (xs == -2147483648 || ys == -2147483648) {
                        continue label56;
                    }

                    if (xs < minX) {
                        minX = xs;
                    }

                    if (xs > maxX) {
                        maxX = xs;
                    }

                    if (ys < minY) {
                        minY = ys;
                    }

                    if (ys > maxY) {
                        maxY = ys;
                    }
                }

                minX -= 5;
                minY -= 5;
                maxX += 5;
                maxY += 5;
                if (vpX1 <= maxX && vpX2 >= minX && vpY1 <= maxY && vpY2 >= minY) {
                    RectangleUnion.Rectangle r = new RectangleUnion.Rectangle(minX, minY, maxX, maxY);
                    rects.add(r);
                }
            }
        }

        return RectangleUnion.union(rects);
    }
}
