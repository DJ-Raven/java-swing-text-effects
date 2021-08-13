package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import shadow.ShadowRenderer;

public class TextEffects extends JLabel {

    public int getShadowSize() {
        return shadowSize;
    }

    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    public float getShadowOpacity() {
        return shadowOpacity;
    }

    public void setShadowOpacity(float shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
    }

    public int getShadowX() {
        return shadowX;
    }

    public void setShadowX(int shadowX) {
        this.shadowX = shadowX;
    }

    public int getShadowY() {
        return shadowY;
    }

    public void setShadowY(int shadowY) {
        this.shadowY = shadowY;
    }

    public Point getMouseLocation() {
        return mouseLocation;
    }

    public void setMouseLocation(Point mouseLocation) {
        this.mouseLocation = mouseLocation;
    }

    public Color getEffectsColor() {
        return effectsColor;
    }

    public void setEffectsColor(Color effectsColor) {
        this.effectsColor = effectsColor;
    }

    private Point mouseLocation;
    private Color effectsColor = Color.RED;
    private int shadowSize = 0;
    private Color shadowColor = Color.BLACK;
    private float shadowOpacity = 0.5f;
    private int shadowX = 5;
    private int shadowY = 5;

    public TextEffects() {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent me) {
                mouseLocation = null;
                repaint();
            }

        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                mouseLocation = me.getPoint();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        int width = getWidth();
        int height = getHeight();
        if (isOpaque()) {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, width, height);
        }
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        //  For smoot Text
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(getFont());
        //  get font metrics to calculate font size and font location
        FontMetrics ft = g.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(getText(), g);
        //  Location text will draw to display
        double x = (width - r2.getWidth()) / 2;
        double y = (height - r2.getHeight()) / 2;
        g.setColor(getForeground());
        g.drawString(getText(), (int) x, (int) (y + ft.getAscent()));
        if (mouseLocation != null) {
            g.setComposite(AlphaComposite.SrcIn);
            float dist[] = {0f, 1f};
            Color colors[] = {effectsColor, getForeground()};
            double radius = r2.getWidth() / 4;
            RadialGradientPaint gra = new RadialGradientPaint(mouseLocation, (float) radius, dist, colors);
            g.setPaint(gra);
            g.fillRect(0, 0, width, height);
        }
        //  Draw Buffer Image
        if (shadowSize > 0) {
            g2.drawImage(new ShadowRenderer(shadowSize, shadowOpacity, shadowColor).createShadow(img), shadowX, shadowY, null);
        }
        g2.drawImage(img, 0, 0, null);
    }
}
