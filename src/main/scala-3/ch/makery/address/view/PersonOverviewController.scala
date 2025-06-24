package ch.makery.address.view
import ch.makery.address.model.Person
import ch.makery.address.MainApp
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import scalafx.Includes.*
import javafx.scene.control.TextField
@FXML
class PersonOverviewController():
  @FXML
  private var personTable: TableView[Person] = null
  @FXML
  private var firstNameColumn: TableColumn[Person, String] = null
  @FXML
  private var lastNameColumn: TableColumn[Person, String] = null
  @FXML
  private var firstNameLabel: Label = null
  @FXML
  private var lastNameLabel: Label = null
  @FXML
  private var streetLabel: Label = null
  @FXML
  private var postalCodeLabel: Label = null
  @FXML
  private var cityLabel: Label = null
  @FXML
  private var birthdayLabel: Label = null
  @FXML
  private var mytext: TextField = null

  // initialize Table View display contents model
  def initialize() =
    personTable.items = MainApp.personData
    // initialize columns's cell values
    firstNameColumn.cellValueFactory = {_.value.firstName}
    lastNameColumn.cellValueFactory  = {_.value.lastName}

    firstNameLabel.text <== mytext.text

