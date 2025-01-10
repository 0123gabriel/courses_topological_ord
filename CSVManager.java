import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import java.io.FileWriter;   

 
public class CSVManager {

    public static HashMap<String, Materia> readCSV(){

        String fileName = "materias.csv"; 
        String prereqFile = "prereqs.csv"; 
        String line = ""; 
        String splitter = ","; 

        ArrayList<Materia> materias = new ArrayList<>(); 
        ArrayList<String[]> prereqs = new ArrayList<>();  

        HashMap <String, Materia> materiasMap = new HashMap<>(); 

        //Se recupera la informacion de todas las materias
        try{
            BufferedReader rd = new BufferedReader(new FileReader(fileName));
            
            while ((line = rd.readLine()) != null){
                String[] info = line.split(splitter); 
                materias.add(new Materia(info[0], Integer.parseInt(info[1]), info[2])); 
            }
            rd.close();
        }
        catch(IOException e){
            e.printStackTrace(); 
        }

        //Se recuperan datos de todos los prerrequsitos de las materias
        try{
            BufferedReader rd = new BufferedReader(new FileReader(prereqFile));
            
            while ((line = rd.readLine()) != null){
                String[] info = line.split(splitter);  
                prereqs.add(info);  
            }
            rd.close();
        }
        catch(IOException e){
            e.printStackTrace(); 
        }

        //Se aniaden los prerrequisitos de cada materia
        for (int i = 0; i < materias.size(); i++){
            for(int j = 0; j < prereqs.size(); j++){
                if (materias.get(i).getCodigo().equals(prereqs.get(j)[0])){
                    materias.get(i).addPrerrequisito(prereqs.get(j)[1]);
                }
            }
        }
        
        for (Materia item : materias){
            materiasMap.put(item.getCodigo(), item); 
        }

        return materiasMap; 
    }


    public static void addMateria(Materia materia){
        
        try{
            FileWriter fw = new FileWriter("materias.csv",true); 
            fw.write(materia.getCodigo()+","+String.valueOf(materia.getNumCred())+","
            + materia.getNombre()+"\n"); 
            fw.close();
        }
        catch(IOException e){
            System.err.println(e);
        }

        List<String> prereqs = materia.getPrerrequisitos();

        for(int i = 0; i < prereqs.size(); i++){
            try{
                FileWriter fw = new FileWriter("prereqs.csv",true); 
                fw.write(materia.getCodigo()+","+prereqs.get(i)+"\n"); 
                fw.close();
            }
            catch(IOException e){
                System.err.println(e);
            }
        }
    }

    //Sobrescribe el archivo CSV con los nuevos cambios
    public static void writeCSV(HashMap<String, Materia> materiasMap){

        clearCSV(); 

        for (Map.Entry<String, Materia> entry : materiasMap.entrySet()){
            addMateria(entry.getValue());
        }

    }

    public static void clearCSV(){

        try{
            FileWriter fw = new FileWriter("materias.csv"); 
            fw.write(""); 
            fw.close();
        }
        catch(IOException e){
            System.err.println(e);
        }

        try{
            FileWriter fw = new FileWriter("prereqs.csv"); 
            fw.write(""); 
            fw.close();
        }
        catch(IOException e){
            System.err.println(e);
        }

    }

    public static void eliminarMateria(HashMap<String, Materia> materiasMap, String codigo){

        materiasMap.remove(codigo); 
        writeCSV(materiasMap);
    }

    /*public static void main(String[] args){
        HashMap<String, Materia> test = readCSV(); 
        System.out.println(test); 

        eliminarMateria(test, "CMP-3005");

        System.out.println(test);
        Materia prueba = new Materia("CMP-3005", 3, "Teoria de la Computacion"); 
        prueba.addPrerrequisito("MAT-2004 Matematicas Discretas"); 
        addMateria(prueba);
        System.out.println(test);
    }*/

}