package project.client.view.pooyaviewpackage;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import project.client.modelsforview.CardView;
import project.model.cardData.General.Card;

public class TroubleFlipWithTranslateTransition {
    TroubleFlipTransition troubleFlipTransition;
    DestroyCardTransition destroyCardTransition;
    RotateTransition rotateTransition;
    TranslateTransition translateTransition;
    PauseTransition pauseTransition = new PauseTransition(Duration.millis(50));

    public TroubleFlipWithTranslateTransition(TroubleFlipTransition troubleFlipTransition, DestroyCardTransition destroyCardTransition, CardView cardView, int turn) {
        this.troubleFlipTransition = troubleFlipTransition;
        this.destroyCardTransition = destroyCardTransition;
        rotateTransition = DuelView.getTransition().rotateCardMinusNintyDegreesVeryQuickly(destroyCardTransition.getCardView());
        translateTransition = DuelView.getControllerForView().sendCardToGraveyardZone(cardView, turn, 50, false);

        troubleFlipTransition.getStShowBack().setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                destroyCardTransition.play();
            }
        });
        destroyCardTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rotateTransition.play();
                translateTransition.play();
            }
        });
        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                destroyCardTransition.getCardView().setShouldBeInvisible(false);
                pauseTransition.play();
            }
        });
    }

    public PauseTransition getPauseTransition() {
        return pauseTransition;
    }
    public void play(){
        troubleFlipTransition.getStHideFront().play();
    }
}
