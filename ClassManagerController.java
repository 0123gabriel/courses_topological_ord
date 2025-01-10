import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button; 
import java.util.HashMap; 
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.Collection;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.List; 
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList; 
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

public class ClassManagerController {

   private HashMap<String, Materia> materias = CSVManager.readCSV(); 

   @FXML
   private TextField tfCodeQuery; 
   
   @FXML
   private TextField tfCredQuery; 
   
   @FXML
   private TextField tfCodigo;
   
   @FXML
   private TextField tfCreditos;
   
   @FXML
   private TextField tfNombre;  
   
   @FXML
   private TextField tfPrereqs; 
   
   @FXML
   private Button btnCodeQuery; 
   
   @FXML
   private Button btnOrdenTop;
   
   @FXML
   private Button btnCredQuery; 
   
   @FXML
   private Button btnDelete;
   
   @FXML
   private Button btnViewAll;
   
   @FXML
   private Button btnAdd;   
   
   @FXML
   private Button btnModReg;
   
   @FXML
   private TableView<Materia> tableView; 
   
   @FXML 
   private TableColumn<Materia, String> codCol; 
   
   @FXML 
   private TableColumn<Materia, Integer> credCol;
   
   @FXML 
   private TableColumn<Materia, String> nameCol;
   
   @FXML 
   private TableColumn<Materia, String> prereqCol;
   
   @FXML 
   private TableColumn<Materia, Integer> numPrereqCol;
   
   @FXML
   private void searchByCodePressed (ActionEvent e){
      String code = tfCodeQuery.getText();
      if (materias.containsKey(code)){
         //Se despliega la materia en la tabla
         ArrayList<Materia> query = new ArrayList<>(); 
         query.add(materias.get(code)); 
         displayTable(query);
         System.out.println(materias.get(code));
      } else{
         warningPopUp("El codigo ingresado no se encuentra en el registro"); 
      }   
   }
   
   @FXML
   private void btnCredQueryPressed(ActionEvent e)
   {
      if (!tfCredQuery.getText().equals(""))
      {
         int numCred = Integer.parseInt(tfCredQuery.getText());
         Collection<Materia> values = materias.values(); 
         ArrayList<Materia> materiasValues = new ArrayList<>(values);
         
         ArrayList<Materia> CredMat = new ArrayList<>();
         
         for (int i = 0; i < materiasValues.size(); i++)
         {
            if (materiasValues.get(i).getNumCred() == numCred)
            {
               CredMat.add(materiasValues.get(i));
            }
         }
         displayTable(CredMat);
         
         if (CredMat.isEmpty())
         {
            warningPopUp("El numero de creditos ingresado no se encuentra en el registro");
         }
      }
      else
      {
         warningPopUp("Numero de creditos invalido, ingrese de nuevo");
      }
   }
   
   @FXML
   private void btnDeletePressed (ActionEvent e){
      String code = tfCodeQuery.getText(); 
      if (materias.containsKey(code)){
         CSVManager.eliminarMateria(materias, code);
      } else{
         warningPopUp("El codigo ingresado no se encuentra en el registro"); 
      }
   }
   
   @FXML
   private void btnViewAllPressed (ActionEvent e){
      
      Collection<Materia> values = materias.values(); 
      ArrayList<Materia> materiasValues = new ArrayList<>(values);
      
      displayTable(materiasValues); 
          
   }
   
   @FXML
   private void btnAddPressed(ActionEvent e){
   
      if (tfCodigo.getText().equals("") || tfCreditos.getText().equals("") || tfNombre.getText().equals("")){
         warningPopUp("Los campos deben estar completos"); 
         return;
      }
      
      Materia nuevaMateria = new Materia(tfCodigo.getText(), Integer.valueOf(tfCreditos.getText()), tfNombre.getText());
      String text = tfPrereqs.getText();
      String[] prereqs = text.split(", "); 
      
      for (String s : prereqs){
         nuevaMateria.addPrerrequisito(s); 
      } 
      
      materias.put(nuevaMateria.getCodigo(), nuevaMateria);
      CSVManager.writeCSV(materias);
      
      tfCodigo.clear();
      tfCreditos.clear();
      tfNombre.clear();
      tfPrereqs.clear(); 
   }
   
   @FXML
   private void btnModRegPressed(ActionEvent e)
   {
      
      List<Materia> fila = tableView.getSelectionModel().getSelectedItems();
      
      if (!fila.isEmpty())
      {
         SimpleStringProperty stCode = new SimpleStringProperty(fila.get(0).getCodigo());
         SimpleStringProperty stNumCred = new SimpleStringProperty(String.valueOf(fila.get(0).getNumCred()));
         SimpleStringProperty stNombre = new SimpleStringProperty(fila.get(0).getNombre());
         SimpleStringProperty stPre = new SimpleStringProperty(fila.get(0).getPrerequisitosList());
         
         tfCodigo.textProperty().bindBidirectional(stCode);
         tfCreditos.textProperty().bindBidirectional(stNumCred);
         tfNombre.textProperty().bindBidirectional(stNombre);
         tfPrereqs.textProperty().bindBidirectional(stPre);
      }
      else
      {
         warningPopUp("Debe seleccionar una fila"); 
      }
      
   }
   
   public void displayTable(ArrayList<Materia> materiasValues){
      
      ObservableList<Materia> items = FXCollections.observableArrayList(materiasValues);
      
      tableView.setItems(items);
       
      codCol.setCellValueFactory(new PropertyValueFactory<Materia, String>("codigo"));
      credCol.setCellValueFactory(new PropertyValueFactory<Materia, Integer>("numCred"));
      nameCol.setCellValueFactory(new PropertyValueFactory<Materia, String>("nombre"));
      prereqCol.setCellValueFactory(new PropertyValueFactory<Materia, String>("prereqsList"));
      numPrereqCol.setCellValueFactory(new PropertyValueFactory<Materia, Integer>("numPrereq"));
      
   }
   
   public void btnOrdTopPressed(ActionEvent e)
   {
      Scene sc = OrdTop.ordenar();
      Stage st = new Stage();
      st.setTitle("Orden Topologico");
      st.setScene(sc);
      st.show();
   }
   
   public void warningPopUp (String msg){
   
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Ha ocurrido un error");
      alert.setContentText(msg);

      alert.showAndWait();
   }
   
}