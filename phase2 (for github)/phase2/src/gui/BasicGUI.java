package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presenter.BasicPresenter;

import java.util.List;

public abstract class BasicGUI implements Viewable {
    final int defaultWidth = 600;
    final int defaultHeight = 400;
    final int spacing = 10;
    BasicPresenter presenter;

    public void setPresenter(BasicPresenter presenter) {
        this.presenter = presenter;
    }

    public abstract void accessUI(Stage stage);

    /**
     * Creates a Scene object with clickable buttons given the spacing, width and height.
     * @param title the title of the menu
     * @param spacing the horizontal/vertical spacing of each button/text block
     * @param alignment the alignment of the buttons
     * @param buttons the buttons in the Scene
     * @param width the width of the screen
     * @param height the height of the screen
     * @return returns a Scene object with buttons.
     */
    public Scene createButtonScene(Text title, int spacing, Pos alignment,
                                   List<Button> buttons, int width, int height){

        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        VBox layout = new VBox(spacing);
        layout.setAlignment(alignment);
        layout.setPadding(new Insets(0, 0, 0, 0));
        layout.getChildren().add(title);
        layout.getChildren().addAll(buttons);

        return new Scene(layout, width, height);
    }

    /**
     * Creates a default Scene object with centered buttons using the default spacing, width and height.
     * @param title the title of the menu
     * @param buttons the buttons in the Scene
     * @return returns a Scene object with buttons.
     */
    public Scene createButtonScene(Text title, List<Button> buttons){
        return createButtonScene(title, spacing, Pos.CENTER, buttons, defaultWidth, defaultHeight);
    }

    /**
     * Creates a Scene with a TextField prompt given the spacing, width and height.
     * @param width the width of the screen
     * @param height the height of the screen
     * @return returns a Scene object with the text prompt.
     */
    public Scene createPromptScene(GridPane grid, int width, int height){
        return new Scene(grid, width, height);
    }

    /**
     * Creates a Scene with a TextField prompt with a default spacing, screen height and screen width.
     * @return returns a Scene object with the text prompt.
     */
    public Scene createPromptScene(GridPane grid){
        return createPromptScene(grid, defaultWidth, defaultHeight);
    }

    public Scene createSuccessScene(Stage stage, Text text){
        GridPane grid = new GridPane();
        grid.setHgap(spacing);
        grid.setVgap(spacing);
        grid.setPadding(new Insets(0,0,0,0));
        grid.setAlignment(Pos.CENTER);
        grid.add(text, 0, 0);
        grid.add(makeExitButton(stage), 0, 1);
        return new Scene(grid, defaultWidth, defaultHeight);
    }
    public Scene createSuccessScene(Stage stage){
        Text text = new Text("Success!");
        text.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 16));
        return createSuccessScene(stage, text);
    }

    public GridPane createGrid(Insets insets, Pos alignment){
        GridPane grid = new GridPane();
        grid.setHgap(spacing);
        grid.setVgap(spacing);
        grid.setPadding(insets);
        grid.setAlignment(alignment);
        return grid;
    }

    public abstract Button makeExitButton(Stage stage);

}
