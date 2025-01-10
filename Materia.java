import java.util.ArrayList;
import java.util.List;



public class Materia implements Cloneable{

    private String codigo; 
    private int numCred; 
    private String nombre; 
    private List<String> prerrequisitos; 

    public Materia(String codigo, int numCred, String nombre, List<String> prerrequisitos){
        setCodigo(codigo);
        setNumCred(numCred);
        setNombre(nombre);
        setPrerrequisitos(prerrequisitos);
    }

    public Materia(String codigo, int numCred, String nombre){
        setCodigo(codigo);
        setNumCred(numCred);
        setNombre(nombre);
        prerrequisitos = new ArrayList<String>(); 
    }

    public void setCodigo(String codigo){
        if (codigo.length() == 0){
            throw new IllegalArgumentException(); 
        }

        this.codigo = codigo; 
    }

    public void setNumCred(int numCred){
        if (numCred < 0 || numCred > 5){
            throw new IllegalArgumentException(); 
        }

        this.numCred = numCred; 
    }

    public void setNombre(String nombre){
        if (nombre.length() == 0){
            throw new IllegalArgumentException(); 
        }

        this.nombre = nombre; 
    }

    public void setPrerrequisitos(List<String> prerrequisitos){
        this.prerrequisitos = prerrequisitos; 
    }

    public String getCodigo(){
        return codigo;
    }

    public int getNumCred(){
        return numCred;
    }

    public String getNombre(){
        return nombre; 
    }

    public List<String> getPrerrequisitos(){
        return prerrequisitos; 
    }
    
    public String getPrereqsList(){
      String s = ""; 
      for (String prereq : prerrequisitos){
         s = s + prereq + "\n";
      }
      return s; 
    }
    
    public String getPrerequisitosList(){
      String s = ""; 
      for (int i = 0; i < prerrequisitos.size(); i++){
         s = s + prerrequisitos.get(i);
         if (i < prerrequisitos.size() - 1)
         {
            s = s + ", ";
         }
      }
      return s; 
    }
    
   public List<String> getPrereqsArrayList(){
      return prerrequisitos; 
    }

    public void addPrerrequisito(String prereq){
        if (prereq.equals("")) return; 
        prerrequisitos.add(prereq); 
    }

    public int getNumPrereq(){
        return prerrequisitos.size(); 
    }

    @Override public String toString(){
        return "Codigo: " + getCodigo() + ", Creditos: " + getNumCred() + ", Nombre: "
        + getNombre() + ", Prerrequisitos: " + getPrerrequisitos(); 
    }

    @Override
    public List<String> clone() throws CloneNotSupportedException
    {
        List<String> object = new ArrayList<>();
        for (int i = 0; i < prerrequisitos.size(); i++)
        {
            object.add(prerrequisitos.get(i));
        }
        return object;
    }

}
