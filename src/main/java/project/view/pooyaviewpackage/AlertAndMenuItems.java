package project.view.pooyaviewpackage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.CardLocation;
import project.model.modelsforview.CardView;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;

public class AlertAndMenuItems {

    public void getActivateEffectMenuItem(CardView cardView, MenuItem item) {
        item.setText("Activate Effect");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                if (cardLocationSelecting != null) {
                    DuelView.getSendingRequestsToServer().sendActivateEffectRequestToServer(cardView, cardLocationSelecting);
                }
                //label.setText("Select Menu Item 1");
            }
        });
    }

    public void getSettingMenuItem(CardView cardView, MenuItem item) {
        item.setText("Set");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                if (cardLocationSelecting != null) {
                    DuelView.getSendingRequestsToServer().sendSettingRequestToServer(cardView, cardLocationSelecting);
                }
                //label.setText("Select Menu Item 1");
            }
        });
    }

    public void getShowGraveyardMenuItem(CardView cardView, MenuItem item, DuelView duelView) {
        item.setText("Show Graveyard");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                if (cardLocationSelecting != null) {
                    DuelView.getSendingRequestsToServer().sendShowGraveyardRequestToServer(cardView, cardLocationSelecting, duelView);
                }
                //label.setText("Select Menu Item 1");
            }
        });
    }

    public void getSpecialSummoningMenuItem(CardView cardView, MenuItem item, DuelView duelView) {
        //MenuItem item = new MenuItem("Special Summon");
        item.setText("Special Summon");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                if (cardLocationSelecting != null) {
                    DuelView.getSendingRequestsToServer().sendSpecialSummoningRequestToServer(cardView, cardLocationSelecting, duelView);
                }
                //label.setText("Select Menu Item 1");
            }
        });
    }

    public void getTributeSummoningMenuItem(CardView cardView, MenuItem item, DuelView duelView) {
        item.setText("Tribute Summon");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                if (cardLocationSelecting != null) {
                    DuelView.getSendingRequestsToServer().sendTributeSummoningRequestToServer(cardView, cardLocationSelecting, duelView);
                }
                //label.setText("Select Menu Item 1");
            }
        });
    }

    public void getNormalSummoningMenuItem(CardView cardView, MenuItem item, DuelView duelView) {
        item.setText("Normal Summon");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                System.out.println();
                // System.out.println("cardLocationSelecting is now " + cardLocationSelecting.getRowOfCardLocation() + " " + cardLocationSelecting.getIndex());
                if (cardLocationSelecting != null) {
                    DuelView.getSendingRequestsToServer().sendNormalSummoningRequestToServer(cardView, cardLocationSelecting, duelView);
                }
            }
        });
    }

    public void getChangeCardPositionMenuItem(CardView cardView, MenuItem item) {
        item.setText("Change Card Position");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                System.out.println();
                // System.out.println("cardLocationSelecting is now " + cardLocationSelecting.getRowOfCardLocation() + " " + cardLocationSelecting.getIndex());
                if (cardLocationSelecting != null) {
                    DuelView.getSendingRequestsToServer().sendChangingCardPositionRequestToServer(cardView, cardLocationSelecting);
                }
            }
        });
    }

    public void getFlipSummonMenuItem(CardView cardView, MenuItem item) {
        item.setText("Flip Summon");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                System.out.println();
                System.out.println("cardLocationSelecting is now " + cardLocationSelecting.getRowOfCardLocation() + " " + cardLocationSelecting.getIndex());
                if (cardLocationSelecting != null) {
                    DuelView.getSendingRequestsToServer().sendFlipSummoningRequestToServer(cardView, cardLocationSelecting);
                }
            }
        });
    }

    public void getAttackingMonsterMenuItem(CardView cardView, MenuItem item) {
        item.setText("Attack Monster");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                System.out.println();
                System.out.println("cardLocationSelecting is now " + cardLocationSelecting.getRowOfCardLocation() + " " + cardLocationSelecting.getIndex());
                if (cardLocationSelecting != null) {
                    showAllAttackingMonsterOptions(cardLocationSelecting);
                }
            }
        });
    }

    public void getDirectAttackingMenuItem(CardView cardView, MenuItem item) {
        item.setText("Attack Direct");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DuelView.setCardLocationSelecting(DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView));
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                System.out.println();
                if (cardLocationSelecting != null) {
                    Alert alert = new Alert(CONFIRMATION, "Do you want to directly attack your opponent with your monster?", ButtonType.YES, ButtonType.NO);
                    ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
                    if (result.equals(ButtonType.YES)) {
                        DuelView.getSendingRequestsToServer().sendAttackDirectRequestToServer(cardLocationSelecting);
                    }
                }
            }
        });
    }

    public void showSetOrActivateForSpellCard(CardView cardViewBeingDragged, CardLocation initialCardLocation) {
        ButtonType setButton = new ButtonType("Set");
        ButtonType activateButton = new ButtonType("Activate");
        ButtonType cancel = new ButtonType("Cancel");
        Alert alert = new Alert(Alert.AlertType.NONE, "Please choose what to do with your card", setButton, activateButton, cancel);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Setting Or Activating Message");
        //alert.setContentText(output);
        ButtonType result = alert.showAndWait().orElse(cancel);
        if (result.equals(setButton)) {
            DuelView.getSendingRequestsToServer().sendSettingRequestToServer(cardViewBeingDragged, initialCardLocation);
        } else if (result.equals(activateButton)) {
            DuelView.getSendingRequestsToServer().sendActivateEffectRequestToServer(cardViewBeingDragged, initialCardLocation);
        }
    }

    public void showSetAlertForTrapCard(CardView cardViewBeingDragged, CardLocation initialCardLocation) {
        ButtonType setButton = new ButtonType("Set");
        ButtonType cancel = new ButtonType("Cancel");
        Alert alert = new Alert(Alert.AlertType.NONE, "Please choose what to do with your card", setButton, cancel);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Setting Message");
        //alert.setContentText(output);
        ButtonType result = alert.showAndWait().orElse(cancel);
        if (result.equals(setButton)) {
            DuelView.getSendingRequestsToServer().sendSettingRequestToServer(cardViewBeingDragged, initialCardLocation);
        }
    }

    public void showAllAttackingMonsterOptions(CardLocation cardLocationSelecting) {
        System.out.println("show all attacking monster options");
        ButtonType attack1 = new ButtonType("Attack 1");
        ButtonType attack2 = new ButtonType("Attack 2");
        ButtonType attack3 = new ButtonType("Attack 3");
        ButtonType attack4 = new ButtonType("Attack 4");
        ButtonType attack5 = new ButtonType("Attack 5");
        ButtonType cancel = new ButtonType("Cancel");
        Alert alert = new Alert(Alert.AlertType.NONE, "Please choose what card to attack.", attack1, attack2, attack3, attack4, attack5, cancel);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Attacking Monster Message");
        ButtonType result = alert.showAndWait().orElse(cancel);
        if (result.equals(attack1)) {
            DuelView.getSendingRequestsToServer().sendAttackMonsterToMonsterRequestToServer(cardLocationSelecting, null, 1);
        } else if (result.equals(attack2)) {
            DuelView.getSendingRequestsToServer().sendAttackMonsterToMonsterRequestToServer(cardLocationSelecting, null, 2);
        } else if (result.equals(attack3)) {
            DuelView.getSendingRequestsToServer().sendAttackMonsterToMonsterRequestToServer(cardLocationSelecting, null, 3);
        } else if (result.equals(attack4)) {
            DuelView.getSendingRequestsToServer().sendAttackMonsterToMonsterRequestToServer(cardLocationSelecting, null, 4);
        } else if (result.equals(attack5)) {
            DuelView.getSendingRequestsToServer().sendAttackMonsterToMonsterRequestToServer(cardLocationSelecting, null, 5);
        }
    }

    public void showAllSummonOptionsAlertForMonsterCard(CardView cardViewBeingDragged, CardLocation initialCardLocation, DuelView duelView) {
        ButtonType normalSummonButton = new ButtonType("Normal Summon");
        ButtonType tributeSummonButton = new ButtonType("Tribute Summon");
        ButtonType specialSummonButton = new ButtonType("Special Summon");
        ButtonType ritualSummonButton = new ButtonType("Ritual Summon");
        ButtonType setButton = new ButtonType("Set");
        ButtonType cancel = new ButtonType("Cancel");
        Alert alert = new Alert(Alert.AlertType.NONE, "Please choose what to do with your card.", normalSummonButton, tributeSummonButton, specialSummonButton, ritualSummonButton, setButton, cancel);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Summoning Or Setting Message");
        //alert.setContentText(output);
        ButtonType result = alert.showAndWait().orElse(cancel);
        if (result.equals(normalSummonButton)) {
            DuelView.getSendingRequestsToServer().sendNormalSummoningRequestToServer(cardViewBeingDragged, initialCardLocation, duelView);
        } else if (result.equals(tributeSummonButton)) {
            DuelView.getSendingRequestsToServer().sendTributeSummoningRequestToServer(cardViewBeingDragged, initialCardLocation, duelView);
        } else if (result.equals(specialSummonButton)) {
            DuelView.getSendingRequestsToServer().sendSpecialSummoningRequestToServer(cardViewBeingDragged, initialCardLocation, duelView);
        } else if (result.equals(ritualSummonButton)) {
            DuelView.getSendingRequestsToServer().sendShowGraveyardRequestToServer(cardViewBeingDragged, initialCardLocation, duelView);
        } else if (result.equals(setButton)) {
            DuelView.getSendingRequestsToServer().sendSettingRequestToServer(cardViewBeingDragged, initialCardLocation);
        }
    }

    public void showAttackMonsterToMonsterAlert(CardLocation cardLocationSelecting, CardLocation finalCardLocation) {
        Alert alert = new Alert(CONFIRMATION, "Do you want to attack the monster you chose with your monster?", ButtonType.YES, ButtonType.NO);
        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
        if (result.equals(ButtonType.YES)) {
            DuelView.getSendingRequestsToServer().sendAttackMonsterToMonsterRequestToServer(cardLocationSelecting, finalCardLocation, -1);
        }
    }

    public void showDirectAttackAlert(CardLocation cardLocationSelecting) {
        Alert alert = new Alert(CONFIRMATION, "Do you want to directly attack your opponent with your monster?", ButtonType.YES, ButtonType.NO);
        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
        if (result.equals(ButtonType.YES)) {
            DuelView.getSendingRequestsToServer().sendAttackDirectRequestToServer(cardLocationSelecting);
        }
    }

    public void doYouWantToAlert(String nowItWillBeTurn){
        Alert alert = new Alert(CONFIRMATION, nowItWillBeTurn, ButtonType.YES, ButtonType.NO);
        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
        if (result.equals(ButtonType.YES)) {
            String output = GameManager.getDuelControllerByIndex(0).getInput("yes", true);
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Result Message");
            alert.setContentText(output);
            alert.showAndWait();
        } else if (result.equals(ButtonType.NO)){
            String output = GameManager.getDuelControllerByIndex(0).getInput("no", true);
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Result Message");
            alert.setContentText(output);
            alert.showAndWait();
            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
        }
    }

}
