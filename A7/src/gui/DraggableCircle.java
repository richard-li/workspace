package gui;

import java.awt.Point;
import java.awt.event.*;

import game.Board;
import game.BoardElement;
import game.Node;

/** An instance is a circle that can be dragged by the user on the gui */
public class DraggableCircle extends Circle {

	private static final long serialVersionUID = -3983152780751574074L;
	private Point clickPoint; //The point the user clicked within the circle before dragging began
	private int maxX;   //Boundary for dragging on the x
	private int maxY;   //Boundary for dragging on the y
	
	
	/** Constructor: an instance at (x, y) of diameter d that represents piece r. */
	public DraggableCircle(final BoardElement r, int x, int y, int d) {
		super(r, x, y, d);
		
		MouseListener clickListener = new MouseListener(){
			
			/** When clicked, store the initial point at which this is clicked. */
			public @Override void mousePressed(MouseEvent e) {
				maxX = r.getBoard().gui.getDrawingPanel().getWidth();
				maxY = r.getBoard().gui.getDrawingPanel().getHeight();
				clickPoint = e.getPoint();
			}
			
			/** This is the selection of a start node or end node for shortest path algorithm */
			public @Override void mouseClicked(MouseEvent e) {
			    Board bd= represents.getBoard();
			    bd.gui.setNode((Node) represents);
			}

			public @Override void mouseReleased(MouseEvent e) {}

			public @Override void mouseEntered(MouseEvent e) {}

			public @Override void mouseExited(MouseEvent e) {}	
		};
		
		MouseMotionListener motionListener = new MouseMotionListener(){

			/** When this is dragged, perform the translation from the point
			 * where this was clicked to the new dragged point. */
			public @Override void mouseDragged(MouseEvent e) {
				DraggableCircle c= (DraggableCircle)e.getSource();
				Point p= e.getPoint();
				int newX= Math.min(maxX, Math.max(0, c.getX1() + p.x - clickPoint.x));
				int newY= Math.min(maxY, Math.max(0, c.getY1() + p.y - clickPoint.y));
				c.represents.updateGUILocation(newX, newY);
			}

			public @Override void mouseMoved(MouseEvent e) {}
		};
		
		addMouseListener(clickListener);
		addMouseMotionListener(motionListener);
	}

}
