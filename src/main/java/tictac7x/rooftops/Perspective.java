package tictac7x.rooftops;

import net.runelite.api.Client;
import net.runelite.api.Model;
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
 * @param plane_modifier - How much to change the clickbox plane.
 */
public class Perspective {
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
        final int[] faceColors3 = m.getFaceColors3();

        net.runelite.api.Perspective.modelToCanvas(client,
                m.getVerticesCount(),
                x, y, z,
                jauOrient,
                m.getVerticesX(), m.getVerticesZ(), m.getVerticesY(),
                x2d, y2d);

        final int radius = 5;

        int[][] tris = new int[][]{
                m.getTrianglesX(),
                m.getTrianglesY(),
                m.getTrianglesZ()
        };

        int vpX1 = client.getViewportXOffset();
        int vpY1 = client.getViewportXOffset();
        int vpX2 = vpX1 + client.getViewportWidth();
        int vpY2 = vpY1 + client.getViewportHeight();

        List<RectangleUnion.Rectangle> rects = new ArrayList<>(m.getTrianglesCount());

        nextTri:
        for (int tri = 0; tri < m.getTrianglesCount(); tri++)
        {
            if (faceColors3[tri] == -2)
            {
                continue;
            }

            int
                    minX = Integer.MAX_VALUE,
                    minY = Integer.MAX_VALUE,
                    maxX = Integer.MIN_VALUE,
                    maxY = Integer.MIN_VALUE;

            for (int[] vertex : tris)
            {
                final int idx = vertex[tri];
                final int xs = x2d[idx];
                final int ys = y2d[idx];

                if (xs == Integer.MIN_VALUE || ys == Integer.MIN_VALUE)
                {
                    continue nextTri;
                }

                if (xs < minX)
                {
                    minX = xs;
                }
                if (xs > maxX)
                {
                    maxX = xs;
                }
                if (ys < minY)
                {
                    minY = ys;
                }
                if (ys > maxY)
                {
                    maxY = ys;
                }
            }

            minX -= radius;
            minY -= radius;
            maxX += radius;
            maxY += radius;

            if (vpX1 > maxX || vpX2 < minX || vpY1 > maxY || vpY2 < minY)
            {
                continue;
            }

            RectangleUnion.Rectangle r = new RectangleUnion.Rectangle(minX, minY, maxX, maxY);

            rects.add(r);
        }

        return RectangleUnion.union(rects);
    }
}
