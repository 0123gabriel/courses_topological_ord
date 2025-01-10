import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List; 
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.IOException;

public class OrdTop
{
   public static Scene ordenar()
   {
      //Lectura de las materias
      HashMap<String, Materia> materias = CSVManager.readCSV();
      Collection<Materia> values = materias.values(); 
      ArrayList<Materia> materiasValues = new ArrayList<>(values);
      
      //Array para guardar en orden topologico las materias
      ArrayList<ArrayList<Materia>> ordenTop = new ArrayList<>();
      
      //Materias sin prerequisitos      
      ArrayList<Materia> NoPreMaterias = new ArrayList<>();
      
      for (int i = 0; i < materiasValues.size(); i++)
      {
         if (materiasValues.get(i).getPrereqsList() == "")
         {
            NoPreMaterias.add(materiasValues.get(i));
         }
      }
      
      for (int i = 0; i < NoPreMaterias.size(); i++)
      {
         materiasValues.remove(NoPreMaterias.get(i));
      }

      ordenTop.add(NoPreMaterias);

      List<String> Prereq = new ArrayList<>();
      
      for (int i = 0; i < NoPreMaterias.size(); i++)
      {
         Prereq.add(NoPreMaterias.get(i).getCodigo());
      }

      List<String> ReqTemp = new ArrayList<>();
      List<List<String>> ReqFalt = new ArrayList<>();
      
      for (int i = 0; i < materiasValues.size(); i++)
      {
         try
         {
            ReqFalt.add(materiasValues.get(i).clone());
         }
         catch(CloneNotSupportedException e)
         {
            e.printStackTrace();
         }
      }
      
      int z = 0;
      
      while (!materiasValues.isEmpty())
      {
         for (int i = 0; i < materiasValues.size(); i++)
         {
            ReqTemp = ReqFalt.get(i);
            
            for (int j = 0; j < NoPreMaterias.size(); j++)
            {
               
               if (materiasValues.get(i).getPrerrequisitos().get(0).equals(Prereq.get(j)))
               {
                  ReqTemp.remove(ReqTemp.get(0));
                  
                  if (ReqTemp.isEmpty())
                  {
                     ReqFalt.remove(i);
                     ArrayList<Materia> temp = new ArrayList<>();
                     temp.add(materiasValues.get(i));
                     ordenTop.add(temp);
                     NoPreMaterias.add(materiasValues.get(i));
                     Prereq.add(materiasValues.get(i).getCodigo());
                     materiasValues.remove(materiasValues.get(i));
                     i = 0;
                     break;
                  }
                  
               }
            }
         }
         
         z = z + 1;
         if (z > 1000)
         {
            warningPopUp("Error en el registro de materias, revise lo prerequisitos");
            break;
         }
      }
      
      List<Materia> orden_temp = new ArrayList<>();
      
      int max_pre = 0;
      
      for (int i = 0; i < ordenTop.get(0).size(); i++)
      {
         orden_temp.add(ordenTop.get(0).get(i));
         
         if (ordenTop.get(0).get(i).getPrerrequisitos().size() > max_pre)
         {
            max_pre = ordenTop.get(0).get(i).getPrerrequisitos().size();
         }
      }
      
      
      List<List<Materia>> ordenFinal = new ArrayList<>();
      
      for (int i = 0; i < max_pre + 1; i++)
      {
         List<Materia> temporal = new ArrayList<>();
         
         for (int j = 0; j < orden_temp.size(); j++)
         {
            if (orden_temp.get(j).getPrerrequisitos().size() == i)
            {
               temporal.add(orden_temp.get(j));
            }
         }
         ordenFinal.add(temporal);
      }
      
      GenerarPensum(ordenFinal);
      
      GridPane gpane = new GridPane();
      gpane.setPadding(new Insets(10, 10, 10, 10));
      gpane.setHgap(10);
      gpane.setVgap(7);
      Label semestre = new Label("Semestre");
      gpane.setHalignment(semestre, HPos.CENTER);
      Label mat = new Label("Materias");
      gpane.setHalignment(mat, HPos.CENTER);
      gpane.add(semestre, 0, 0);
      gpane.add(mat, 1, 0);
      
      HBox hb = new HBox();
      
      for (int i = 0; i < ordenFinal.size(); i++)
      {
         Label lb1 = new Label(String.valueOf(i + 1));
         String st = "";
         
         for (int j = 0; j < ordenFinal.get(i).size(); j++)
         {
            
            st = st + ordenFinal.get(i).get(j).getNombre() + "\t";
         }
         
         Label lb = new Label(st);
         gpane.add(lb1, 0, i + 1);
         gpane.setHalignment(lb1, HPos.CENTER);
         gpane.add(lb, 1, i + 1);
      }
      
      Scene sc = new Scene(gpane);
      
      return sc;
      
   }
   
   public static void warningPopUp (String msg){
   
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Ha ocurrido un error");
      alert.setContentText(msg);

      alert.showAndWait();
   }
   
   //Guia de este codigo obtenido de https://es.stackoverflow.com/questions/330387/crear-archivos-csv-en-java
   public static void GenerarPensum(List<List<Materia>> orden)
   {
      try
      {
      String currentPath = Paths.get("").toAbsolutePath().normalize().toString();
      String fileName = "pensum.csv";
      File statText = new File(currentPath + "/" + fileName);
      FileOutputStream is = new FileOutputStream(statText);
      OutputStreamWriter osw = new OutputStreamWriter(is);
      Writer w = new BufferedWriter(osw);
      
      for (int i = 0; i < orden.size(); i++) {
      
         w.write("Semestre " + String.valueOf(i + 1) + "\n");
      
         for (int j = 0; j < orden.get(i).size(); j++)
         {
                w.write(orden.get(i).get(j).toString());
                w.write("\n");
         }
         w.write("\n");
      }
      w.close();
      }
      catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}