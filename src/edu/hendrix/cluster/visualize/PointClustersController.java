package edu.hendrix.cluster.visualize;

import java.util.ArrayList;
import java.util.Optional;

import edu.hendrix.cluster.BoundedSelfOrgCluster;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

public class PointClustersController {
	ToggleGroup group = new ToggleGroup();
	
	@FXML
	RadioButton plot;
	@FXML
	RadioButton classify;
	@FXML
	RadioButton train;
	
	@FXML
	TextField nodes;
	
	@FXML
	Canvas canvas;
	
	Optional<Integer> winningNode = Optional.empty();
	
	BoundedSelfOrgCluster<Point,Point> bsoc;
	ArrayList<Point> inputs;
	
	@FXML
	void generateBSOC() {
		bsoc = new BoundedSelfOrgCluster<>(Integer.parseInt(nodes.getText()), Point::distance, i -> i);
	}
	
	@FXML
	void redraw() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		for (int node: bsoc.getClusterIds()) {
			gc.setFill(Color.RED);
			winningNode.ifPresent(id -> {if (node == id) gc.setFill(Color.PURPLE);});
			Point where = bsoc.getIdealInputFor(node);
			double radius = bsoc.getNumMergesFor(node);
			gc.fillOval(where.getX(), where.getY(), radius, radius);
		}
		
		gc.setFill(Color.BLACK);
		for (Point p: inputs) {
			gc.fillOval(p.getX(), p.getY(), 3, 3);
		}
	}
	
	@FXML
	void initialize() {
		plot.setToggleGroup(group);
		classify.setToggleGroup(group);
		train.setToggleGroup(group);
		train.setSelected(true);
		
		nodes.setText("2");
		
		clear();
		
		canvas.setOnMouseClicked(mouse -> {
			Point plotted = new Point(mouse.getX(), mouse.getY());
			inputs.add(plotted);
			winningNode = Optional.empty();
			if (train.isSelected()) {
				bsoc.train(plotted);
				System.out.println(plotted);
				String clusters = String.format("%d: ", bsoc.getClusterIds().size());
				for (int id: bsoc.getClusterIds()) {
					clusters += "{" + bsoc.getIdealInputFor(id) + ";" + bsoc.getNumMergesFor(id) + "} ";
				}
				System.out.println("Clusters: " + clusters);
			} else if (classify.isSelected()) {
				winningNode = Optional.of(bsoc.getClosestMatchFor(plotted));
			}
			redraw();
		});
	}
	
	@FXML
	void clear() {
		inputs = new ArrayList<>();
		generateBSOC();
		redraw();
	}
}
