package com.rideshare;

import com.rideshare.TileManager.TileUtils;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class EducationalPopup {
    private final String[] walkingFacts = {
            "Completing five trips of 2km a week on foot instead of a car can decrease the amount of emissions by 86kg a year.",
            "Walking regularly can reduce the risk of cardiovascular disease by 35%",
            "Choosing to commute by walking or cycling once a week could save around 40kg of CO2 a year.",
            "By walking, cycling or wheeling you are reducing congestion and pollution, making local spaces more enjoyable, quieter and healthier.",
            "TIP: Find a good podcast to listen to on long walks.",
            "TIP: Find a friend to join you on walks." };
    private int walkingIdx = 0;
    private final String[] carFacts = {
            "60% of the emissions from the transportation sector in the US come from personal vehicles.",
            "333 million tons of carbon dioxide are released into the atmosphere from transportation annually.",
            "Transport was responsible for about a quarter of the EU's total CO2 emissions in 2019",
            "71.7% of emissions in the EU in 2019 came from road transportation.",
            "Transport is the only sector where greenhouse gas emissions have increased in the past three decades.",
            "A typical passenger vehicle emits about 4.6 metric tons of CO2 per year.",
            "The average petrol car emits around 4 times more CO2 per passenger than the average bus.",
            "Avoiding just 10 miles of driving every week would eliminate about 500 pounds of carbon dioxide emissions a year!",
            "TIP: Reduce your impact by driving efficiently - go easy on the gas pedal and brakes.",
            "TIP: Reduce your impact by maintaining your car - get regular tune-ups and use the recommended motor oil.",
            "TIP: Check if your city has a bike-share program so you can bike instead.",
            "TIP: Carpool with friends.",
            "TIP: Plan ahead and run all your errands at once to reduce time spent driving.",
            "TIP: Modern vehicles don't need to 'warm up' in the Winter - don't turn on your engine until you are ready to drive.",
            "TIP: Try carpooling - carpooling has the potential to reduce global emissions by as much as 11%!",
            "TIP: Carpooling can reduce the carbon footprint of an average household by up to 1 ton per year.",
            "TIP: If you must use a car, park the car 2-3 km from your destination and walk the remaining distance." };
    private int carIndex = 0;
    private final String[] transitFacts = {
            "A household can save more than $13,000 by taking public transportation and living with one less car!",
            "Communities that invest in public transit reduce the nation's carbon emissions by 63 million metric tons annually.",
            "People that use public transport are 44% less likely to be overweight, 27% less likely to have high blood pressure, and 34% less likely to have diabetes.",
            "Opting for the train instead of driving reduces your emissions for that journey by around 80%",
    };
    private int transitIdx = 0;
    private final String[] otherFacts = {
            "CO₂ emissions from transportation have increased by more than 70 percent since 1990.",
            "Around 3.7 million people die each year from causes directly attributable to air pollution.",
            "Every gallon of gasoline you save avoids 22 pounds of CO2 emissions." };
    private int otherIdx = 0;

    private ScrollPane contentContainer;
    private VBox factStack;

    public void EducationalPopup() {
    }

    public void render(AnchorPane root) {
        contentContainer = new ScrollPane();
        contentContainer.setStyle("-fx-background-color:transparent;");
        contentContainer.setPrefWidth(300);
        factStack = new VBox();
        factStack.setPrefHeight(UIComponentUtils.getStageHeight(root) - 20);
        AnchorPane.setLeftAnchor(contentContainer, (TileUtils.TILE_SIZE_IN_PIXELS * 30.0) + 300); // 300 = width of the
                                                                                                  // trip chooser - not
                                                                                                  // ideal!
        AnchorPane.setTopAnchor(contentContainer, 25.0);
        contentContainer.setContent(factStack);
        root.getChildren().add(contentContainer);
    }

    public void renderFact(TripType tripType) {
        String fact = "";
        String title = "";
        switch (tripType) {
            case EFFICIENT:
                if (walkingIdx < walkingFacts.length) {
                    fact = walkingFacts[walkingIdx];
                    title = "WALKING FACT";
                    walkingIdx++;
                    break;
                }
            case FAST:
                if (carIndex < carFacts.length) {
                    fact = carFacts[carIndex];
                    title = "CAR FACT";
                    carIndex++;
                    break;
                }
            case TRANSIT_ONLY:
                if (transitIdx < transitFacts.length) {
                    fact = transitFacts[transitIdx];
                    title = "TRANSIT FACT";
                    transitIdx++;
                    break;
                }
            default:
                if (otherIdx < otherFacts.length) {
                    fact = otherFacts[otherIdx];
                    title = "FACT";
                    otherIdx++;
                    break;
                } else {
                    // Break out - we're all out of facts!
                    return;
                }
        }

        AnchorPane factPane = UIComponentUtils.createStyledDialog(100, 280);
        Text titleText = new Text(title);
        titleText.setFont(new Font("Futura Bold", 14));
        titleText.setFill(Color.WHITE);
        AnchorPane.setTopAnchor(titleText, 5.0);
        AnchorPane.setLeftAnchor(titleText, 100 - (titleText.getLayoutBounds().getWidth() / 2));

        factPane.getChildren().add(titleText);

        Text f = new Text(fact);
        f.setWrappingWidth(260);
        f.setFont(new Font("Futura Bold", 10));
        AnchorPane.setTopAnchor(f, 30.0);
        AnchorPane.setLeftAnchor(f, 10.0);

        factPane.getChildren().add(f);
        VBox.setMargin(factPane, new Insets(5, 0, 0, 0));
    
        factStack.getChildren().add(0, factPane);
        factPane.setTranslateY(-factStack.getHeight());
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), factPane);
        transition.setToY(0);
        transition.play();
    }
}
