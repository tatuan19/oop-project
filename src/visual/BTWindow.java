package visual;

import java.util.LinkedList;

import algo.BTree;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class BTWindow extends BorderPane {
	private int windowHeight;
	private int windowWidth;

	private int key;
	private BTreePane btPane;
	private TextField keyText = new TextField();
	private Button previousButton = new Button("Previous");
	private Button nextButton = new Button("Next");

	private int index = 0;
	private LinkedList<BTree<Integer>> bTreeLinkedList = new LinkedList<BTree<Integer>>();
	private BTree<Integer> bTree = new BTree<Integer>(3);

	public BTWindow(int windowWidth, int windowHeight) {
		super();
		this.windowHeight = windowHeight;
		this.windowWidth = windowWidth;
	}

	public void run() {
		// Create button HBox on top
		HBox hBox = new HBox(15);
		this.setTop(hBox);
		BorderPane.setMargin(hBox, new Insets(10, 10, 10, 10));
		// TextField
		keyText.setPrefWidth(60);
		keyText.setAlignment(Pos.BASELINE_RIGHT);
		// Button
		Button insertButton = new Button("Insert");
		Button deleteButton = new Button("Delete");
		Button searchButton = new Button("Search");
		Label nullLabel = new Label();
		nullLabel.setPrefWidth(30);

		hBox.getChildren().addAll(new Label("Enter a number: "), keyText, insertButton, deleteButton, searchButton,
				nullLabel, previousButton, nextButton);
		hBox.setAlignment(Pos.CENTER);
		checkValid();

		// Create TreePane in center
		// TODO: chinh lai x, y theo size window
		btPane = new BTreePane(windowWidth / 2, 50, bTree);
		btPane.setPrefSize(windowHeight, windowWidth);
//				bTreeLinkedList.add(CloneUtils.clone(bTree));
		this.setCenter(btPane);

		insertButton.setOnMouseClicked(e -> insertValue());
		deleteButton.setOnMouseClicked(e -> deleteValue());
		searchButton.setOnMouseClicked(e -> searchValue());
		previousButton.setOnMouseClicked(e -> goPrevious());
		nextButton.setOnMouseClicked(e -> goNext());
	}
	
	private void checkValid() {
		if (index > 0 && index < bTreeLinkedList.size() - 1) {
			previousButton.setVisible(true);
			nextButton.setVisible(true);
		} else if (index > 0 && index == bTreeLinkedList.size() - 1) {
			previousButton.setVisible(true);
			nextButton.setVisible(false);
		} else if (index == 0 && index < bTreeLinkedList.size() - 1) {
			previousButton.setVisible(false);
			nextButton.setVisible(true);
		} else {
			previousButton.setVisible(false);
			nextButton.setVisible(false);
		}
	}

//	private void deleteList() {
//		for (int i = bTreeLinkedList.size() - 1; i >= index; i--) {
//			bTreeLinkedList.removeLast();
//		}
//	}

	private void insertValue() {
		try {
			key = Integer.parseInt(keyText.getText());
			keyText.setText("");
//			if (index < bTreeLinkedList.size() - 1) {
//				deleteList();
//				bTreeLinkedList.add(CloneUtils.clone(bTree));
//			}
//			btPane.insert(bTree, key);
			bTree.setStepTrees(new LinkedList<BTree<Integer>>());

			bTree.insert(key);

			index = 0;
			bTreeLinkedList = bTree.getStepTrees();
//			bTreeLinkedList.add(CloneUtils.clone(bTree));
//			index = bTreeLinkedList.size() - 1;
//			checkValid();
			btPane.updatePane(bTreeLinkedList.get(0));
			checkValid();
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input data!", ButtonType.OK);
			alert.show();
		}
	}

	private void deleteValue() {
		try {
			key = Integer.parseInt(keyText.getText());
			keyText.setText("");
//			if (index < bTreeLinkedList.size() - 1) {
//				deleteList();
//				bTreeLinkedList.add(CloneUtils.clone(bTree));
//			}
			if (bTree.getNode(key) == bTree.nullBTNode) {
				throw new Exception("Not in the tree!");
			}
			bTree.setStepTrees(new LinkedList<BTree<Integer>>());

			bTree.delete(key);

			index = 0;
			bTreeLinkedList = bTree.getStepTrees();
//			bTreeLinkedList.add(CloneUtils.clone(bTree));
//			index = bTreeLinkedList.size() - 1;
//			checkValid();
//			btPane.updatePane(bTree);
			btPane.updatePane(bTreeLinkedList.get(0));
			checkValid();
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input data!", ButtonType.OK);
			alert.show();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
			alert.show();
		}
	}

	private void searchValue() {
		try {
			key = Integer.parseInt(keyText.getText());
			keyText.setText("");

			btPane.searchPathColoring(bTree, key);
//			bTree.setStepTrees(new LinkedList<BTree<Integer>>());

//			bTree.insert(key);

//			index = 0;
//			bTreeLinkedList = bTree.getStepTrees();
//			btPane.updatePane(bTreeLinkedList.get(0));
//			checkValid();
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input data!", ButtonType.OK);
			alert.show();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
			alert.show();
		}
	}

	private void goPrevious() {
		if (index > 0) {
//			bTree = bTreeLinkedList.get(index - 1);
//			btPane.updatePane(bTree);

			index--;
			btPane.updatePane(bTreeLinkedList.get(index));

			checkValid();
		}
	}

	private void goNext() {
		if (index < bTreeLinkedList.size() - 1) {
//			bTree = bTreeLinkedList.get(index + 1);
//			btPane.updatePane(bTree);

			index++;
			System.out.println("index: " + index + " - size: " + bTreeLinkedList.size());
			btPane.updatePane(bTreeLinkedList.get(index));

			checkValid();
		}
	}

}
