package de.bytedev.exercise2;

import de.bytedev.utility.Polygon;
import de.bytedev.utility.Vector2D;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

import java.awt.*;
import java.util.List;

/**
 * This class is used to draw a polygon into a drawing frame.
 */
public class Area implements Drawable {

    private Polygon polygon;

    /**
     * This setter can be used to set a polygon.
     *
     * @param polygon the polygon which should be drawn
     */
    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    @Override
    public void draw(DrawingPanel panel, Graphics g) {
        if( this.polygon != null ) {
            List<Vector2D> points = this.polygon.getPoints();

            int[] xPoints = new int[points.size()];
            int[] yPoints = new int[points.size()];

            for( int i = 0; i < points.size(); i++ ) {
                Vector2D point = points.get(i);

                xPoints[i] = panel.xToPix(point.getX());
                yPoints[i] = panel.yToPix(point.getY());
            }

            g.setColor(new Color(0f,1f,1f, 0.5f));
            g.fillPolygon(xPoints, yPoints, points.size());
        }
    }
}
