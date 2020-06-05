package visual;

import algo.BTNode;
import algo.BTree;
import javafx.animation.FillTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BTreePane extends Pane {
	private BTree<Integer> bTree;
	private double originalX, originalY;
	
	// TODO: make node size relate to pane's size
	private final int fontSize = 12;
	private final int rectangleWidth = 40;
	private final int rowSpace = 60;

	public BTreePane(double x, double y, BTree<Integer> bTree) {
		this.originalX = x;
		this.originalY = y;
		this.bTree = bTree;
	}

	/*
	 * Draw Tree & Node
	 */
	public void updatePane(BTree<Integer> bTree) {
		this.getChildren().clear();
		this.bTree = bTree;
		DrawBTree(bTree.getRoot(), originalX, originalY);
	}

	private void DrawNode(String s, double x, double y, Color color) {
		Rectangle rect = new Rectangle(x, y, rectangleWidth, 2 * fontSize);
		rect.setFill(color);
		rect.setStroke(Color.BLACK);
		this.getChildren().addAll(rect, new Text(x + 15, y + 15, s));
	}

	private void DrawBTree(BTNode<Integer> root, double x, double y) {
		if (root != null) {
			// Draw keys of node
			for (int i = 0; i < root.getSize(); i++) {
				DrawNode(root.getKey(i).toString(), x + i * rectangleWidth, y, Color.LIGHTSEAGREEN);
			}
			// Draw line
			double startY = y + 2 * fontSize;
			if (!root.isLastInternalNode()) {
				for (int i = 0; i < root.getChildren().size(); i++) {
					// startX, endX = start, end of Line
					// startX2 = start of child nodes
					double startX = x + i * rectangleWidth;
					double startX2 = 0, endX = 0;

					if ((double) i > ((double) root.getSize()) / 2) {
						// TODO: nen tao 1 ham de tinh do rong cua cay con roi cong voi middle
						startX2 = startX
								+ (bTree.getOrder() - 1) * bTree.getHeight(root.getChild(i)) * rectangleWidth / 2;
						endX = startX2 + ((double) root.getChild(i).getSize()) / 2 * rectangleWidth;
					} else if ((double) i < ((double) root.getSize()) / 2) {
						endX = startX - (bTree.getOrder() - 1) * bTree.getHeight(root.getChild(i)) * rectangleWidth / 2
								- ((double) root.getChild(i).getSize()) / 2 * rectangleWidth;
						startX2 = endX - ((double) root.getChild(i).getSize()) / 2 * rectangleWidth;
					} else {
						startX2 = startX - ((double) root.getChild(i).getSize()) / 2 * rectangleWidth;
						endX = startX;
					}
					// Chu y trong truong hop temp tree
					if (i == 0) {
						startX2 -= rectangleWidth / 2;
						endX -= rectangleWidth / 2;
					} else if (i == root.getSize()) {
						startX2 += rectangleWidth / 2;
						endX += rectangleWidth / 2;
					}

					// Draw child nodes
					if (!root.getChild(i).isNull())
						this.getChildren().add(new Line(startX, startY, endX, y + rowSpace));
					DrawBTree(root.getChild(i), startX2, y + rowSpace);
				}
			}
		}
	}

	public void searchPathColoring(BTree<Integer> bTree, int key) throws Exception {
		updatePane(bTree);
		if (!bTree.isEmpty()) {
			BTNode<Integer> currentNode = bTree.getRoot();
			double x = originalX, y = originalY;
			int delay = 0;
			while (!currentNode.equals(bTree.nullBTNode)) {
				int i = 0;
				while (i < currentNode.getSize()) {
//					DrawNode(currentNode.getKey(i).toString(), x, y, Color.RED);
					makeNodeAnimation(currentNode.getKey(i).toString(), x, y, delay);
					delay++;
					// key can tim
					if (currentNode.getKey(i).equals(key)) {
						return;
					} else if (currentNode.getKey(i).compareTo(key) > 0) {
						// di xuong key ben trai
						y += rowSpace;
						if ((double) i < ((double) currentNode.getSize()) / 2) {
							x = x - (bTree.getOrder() - 1) * bTree.getHeight(currentNode.getChild(i)) * rectangleWidth
									/ 2 - ((double) currentNode.getChild(i).getSize()) * rectangleWidth;
						} else {
							x = x - ((double) currentNode.getChild(i).getSize()) / 2 * rectangleWidth;
						}
						if (i == 0) {
							x -= rectangleWidth / 2;
						}

						currentNode = currentNode.getChild(i);
						i = 0;
					} else {
						// di den key tiep theo trong node
						i++;
						x += rectangleWidth;
					}
				}
				// di xuong key ben phai cua node
				if (!currentNode.isNull()) {
					y += rowSpace;
					x = x + (bTree.getOrder() - 1) * bTree.getHeight(currentNode.getChild(i)) * rectangleWidth / 2
							+ rectangleWidth / 2;

					currentNode = currentNode.getChild(currentNode.getSize());
				}
			}
		}
		throw new Exception("Not in the tree!");
	}

	/*
	 * Draw Animation
	 */

	// TODO: refactor
	private void makeNodeAnimation(String s, double x, double y, int delay) {
		// Draw a node
		Rectangle rect = new Rectangle(x, y, rectangleWidth, 2 * fontSize);
		rect.setFill(Color.LIGHTSEAGREEN);
		rect.setStroke(Color.BLACK);
		this.getChildren().addAll(rect, new Text(x + 15, y + 15, s));
		
		// make fill transition
		FillTransition fill = new FillTransition();
		
		fill.setAutoReverse(false);
		fill.setCycleCount(1);
		fill.setDelay(Duration.seconds(delay));
		fill.setDuration(Duration.seconds(1));
		fill.setFromValue(Color.LIGHTSEAGREEN);
		fill.setToValue(Color.RED);
		fill.setShape(rect);  
		fill.play(); 
	}

}
