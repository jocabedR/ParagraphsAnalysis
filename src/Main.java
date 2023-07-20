import java.util.Scanner;
import javax.swing.text.AbstractDocument.Content;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.print("Ruta del archivo de texto: ");
        String filePath = reader.next();
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
            System.out.print("Porcentaje: ");
            float percentage = reader.nextFloat();

            System.out.print("Palabra a ser buscada: ");
            String word = reader.next();

            System.out.print("Numero de palabras que tendrá el nuevo párrafo: ");
            int newLength = reader.nextInt();

            Analysis analysis = new Analysis(content, percentage, word, newLength);
            analysis.startAnalysis();
        } catch (IOException e) {
            System.out.println("Error en alguna entrada: " + e.getMessage());
        }
    }

}