package ch.makery.address.view

import ch.makery.address.MainApp
import javafx.event.ActionEvent
import javafx.fxml.FXML

@FXML
class RootLayoutController():

  @FXML
  def handleClose(action: ActionEvent): Unit =
    MainApp.stage.close()

  @FXML
  def handleDelete(action: ActionEvent): Unit =
    MainApp.personOverviewController.map(x => x.handleDeletePerson(action))